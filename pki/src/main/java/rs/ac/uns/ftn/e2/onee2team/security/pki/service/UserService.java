package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.RecoveryDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserRequestDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.UserDefinedSubject;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.EndEntity;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.IntermediaryCA;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.UserType;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.IUserRepository;

@Service
public class UserService implements IUserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	private IUserRepository userRepository;
	private IAuthorityService authorityService;
	private IEmailNotificationService emailNotificationService;

	@Autowired
	public UserService(IUserRepository userRepository, IAuthorityService authorityService,
			IEmailNotificationService emailNotificationService) {
		this.userRepository = userRepository;
		this.authorityService = authorityService;
		this.emailNotificationService = emailNotificationService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
		} else {
			return user;
		}
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDTO> getAll() {
		List<User> users = userRepository.findAll();
		List<UserDTO> ret_list = new ArrayList<UserDTO>();
		UserDTO dto;
		for (User u : users) {
			dto = new UserDTO(u.getEmail(), u.getUserSubject());
			ret_list.add(dto);
		}
		return ret_list;
	}

	@Override
	public User createUser(UserRequestDTO userRequest) {
		User ret = null;
		if (userRequest.getUserType().equals(UserType.INTERMEDIARY_CA)) {
			IntermediaryCA u = new IntermediaryCA();
			u.setUserType(UserType.INTERMEDIARY_CA);
			u.setCertificates((List<Certificate>) (new ArrayList<Certificate>()));
			u.setAuthorities(authorityService.findByname("ROLE_INTERMEDIARY_CA"));
			ret = u;
		} else if (userRequest.getUserType().equals(UserType.END_ENTITY)) {
			EndEntity u = new EndEntity();
			u.setUserType(UserType.END_ENTITY);
			u.setCertificates((List<Certificate>) (new ArrayList<Certificate>()));
			u.setAuthorities(authorityService.findByname("ROLE_END_ENTITY"));
			ret = u;
		} else
			return null;
		ret.setEmail(userRequest.getEmail());
		ret.setEnabled(false);
		ret.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		ret.setRequestUUID(UUID.randomUUID().toString());
		ret.setExpUUID(new Date((new Date()).getTime() + 1200000L)); // 20mins
		ret.setUserSubject(new UserDefinedSubject());
		ret.getUserSubject().setCountry(userRequest.getCountry());
		ret.getUserSubject().setLocality(userRequest.getLocality());
		ret.getUserSubject().setOrganization(userRequest.getOrganization());
		ret.getUserSubject().setOrganizationalUnit(userRequest.getOrganizationalUnit());
		ret.getUserSubject().setState(userRequest.getState());
		return userRepository.save(ret);
	}

	@Override
	public boolean validateUser(Long id, String uuid) {
		User p = userRepository.findById(id).orElse(null);
		if (p == null || p.isEnabled() || p.getExpUUID().getTime() < new Date().getTime()
				|| !uuid.equals(p.getRequestUUID()))
			return false;
		p.setEnabled(true);
		p.setRequestUUID(null);
		p.setExpUUID(null);
		p = userRepository.saveAndFlush(p);
		return p.isEnabled();
	}

	@Override
	public List<User> getUsers(String text) {
		List<User> aa= userRepository.getUsers(text);
		return aa;
	}

	@Override
	public Boolean requestRecovery(String email) {
		User u = userRepository.findByEmail(email);
		if (u == null) {
			return false;
		}
		u.setRequestUUID(UUID.randomUUID().toString());
		u.setExpUUID(new Date((new Date()).getTime() + 1200000L)); // 20mins
		emailNotificationService.sendNotificationAsync(email, "Account recovery",
				"Visit this link in the next 20 minutes to change your password: https://localhost/recovery.html?id="
						+ u.getId() + "&str=" + u.getRequestUUID());
		userRepository.saveAndFlush(u);
		return true;
	}

	@Override
	public Boolean recovery(RecoveryDTO dto) {
		User u = userRepository.findById(dto.getId()).orElse(null);
		if (u == null || u.getExpUUID()==null || u.getExpUUID().getTime() < new Date().getTime() || !dto.getUuid().equals(u.getRequestUUID())
				|| !u.isEnabled()) {
			return false;
		}
		u.setExpUUID(null);
		u.setRequestUUID(null);
		u.setPassword(passwordEncoder.encode(dto.getPassword()));
		userRepository.saveAndFlush(u);
		return true;
	}
}
