package rs.ac.uns.ftn.e2.onee2team.security.pki.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import rs.ac.uns.ftn.e2.onee2team.security.pki.auth.JwtAuthenticationRequest;
import rs.ac.uns.ftn.e2.onee2team.security.pki.auth.ResourceConflictException;
import rs.ac.uns.ftn.e2.onee2team.security.pki.auth.TokenUtils;
import rs.ac.uns.ftn.e2.onee2team.security.pki.auth.UserTokenState;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserRequestDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.EndEntity;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.IntermediaryCA;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.UserType;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.IEmailNotificationService;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.IKeyVaultService;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.IUserService;


@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController extends ValidationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private IUserService userService;
	
	//@Autowired
	//private IKeyVaultService keyVaultService;
	
	@Autowired
	private IEmailNotificationService emailNotificationService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/login") 
	public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response) {
		//keyVaultService.dothething();
		// 
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
						authenticationRequest.getPassword()));

		// Ubaci korisnika u trenutni security kontekst
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token za tog korisnika
		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getEmail());
		long expiresIn = tokenUtils.getExpiredIn();
		UserType userType = user.getUserType();
		String email = user.getEmail();

		// Vrati token kao odgovor na uspesnu autentifikaciju
		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, userType, email));
	}
	
	// Endpoint za registraciju novog korisnika
		@PostMapping("/register")
		public ResponseEntity<User> addUser(@Valid @RequestBody UserRequestDTO userRequest, UriComponentsBuilder ucBuilder) {

			User existUser = this.userService.findByEmail(userRequest.getEmail());
			if (existUser != null) {
				throw new ResourceConflictException(0L/*userRequest.getEmail()*/, "Email already exists");
			}
			User user = this.userService.createUser(userRequest);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/api/user/{userId}").buildAndExpand(user.getId()).toUri());
			this.emailNotificationService.sendNotificationAsync(user.getEmail(), "Account Validation", "Visit this link in the next 20 minutes to validate your account: https://localhost/api/auth/validate/" + user.getId() + "/" + user.getRequestUUID());
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		}
		
		@GetMapping("/validate/{id}/{uuid}")
		public ResponseEntity<String> validatePatient(@PathVariable("id") Long id, @PathVariable("uuid") String uuid, UriComponentsBuilder ucBuilder) {
			boolean check = userService.validateUser(id, uuid);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/certificates.html").build().toUri());
			if(check) return new ResponseEntity<String>("Validation succesfull, you can use your account now.", headers, HttpStatus.TEMPORARY_REDIRECT);
			else return new ResponseEntity<String>("Illegal invocation.", HttpStatus.I_AM_A_TEAPOT);
		}
}
