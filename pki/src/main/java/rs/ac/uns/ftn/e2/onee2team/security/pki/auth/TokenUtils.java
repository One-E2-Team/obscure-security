package rs.ac.uns.ftn.e2.onee2team.security.pki.auth;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtils {

	@Value("obscure-pki-security")
	private String APP_NAME;

	@Value("somesecret")
	public String SECRET;

	@Value("86400000")
	private int EXPIRES_IN;

	@Value("Authorization")
	private String AUTH_HEADER;
	
	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
	
	public String generateToken(String email) {
		return Jwts.builder()
				.setIssuer(APP_NAME)
				.setSubject(email)
				.setAudience(generateAudience())
				.setIssuedAt(new Date())
				.setExpiration(generateExpirationDate())
				.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
	}
	
	private String generateAudience() {
		return "web";
	}
	
	private Date generateExpirationDate() {
		return new Date(new Date().getTime() + EXPIRES_IN);
	}
	
	// Funkcija za validaciju JWT tokena
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String email = getEmailFromToken(token);
		return (email != null && email.equals(userDetails.getUsername()));
	}

	public String getEmailFromToken(String token) {
		String email;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			email = claims.getSubject();
		} catch (Exception e) {
			email = null;
		}
		return email;
	}

	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	public int getExpiredIn() {
		return EXPIRES_IN;
	}

	public String getToken(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromHeader(request);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}

	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	
}
