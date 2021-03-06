package rs.ac.uns.ftn.e2.onee2team.security.pki.model.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.UserDefinedSubject;

@Entity
@Table(name = "all_users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "userType", nullable = false)
	private UserType userType;
	
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userSubject_id", referencedColumnName = "id")
	private UserDefinedSubject userSubject;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled = false;
	
	@Column(name = "requestUUID", nullable = true)
	private String requestUUID;
	
	@Column(name = "expUUID", nullable = true)
	private Date expUUID;

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setUsername(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setType(UserType userType) {
		this.userType = userType;
	}
	
	public UserDefinedSubject getUserSubject() {
		return userSubject;
	}

	public void setUserSubject(UserDefinedSubject userSubject) {
		this.userSubject = userSubject;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> usersAuthorities = new ArrayList<GrantedAuthority>();
		usersAuthorities.addAll(this.authorities);
		
		for(Authority a : this.authorities) {
			List<Permission> permissions = a.getPermissions();
			for(Permission p : permissions) {
				usersAuthorities.add(p);
			}
		}
		
		return usersAuthorities;
	}
	
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public String getUsername() {
		return this.getEmail();
	}

	public String getRequestUUID() {
		return requestUUID;
	}

	public void setRequestUUID(String requestUUID) {
		this.requestUUID = requestUUID;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getExpUUID() {
		return expUUID;
	}

	public void setExpUUID(Date expUUID) {
		this.expUUID = expUUID;
	}
	
}
