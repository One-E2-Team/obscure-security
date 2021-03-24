package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.IUserRepository;

@Service
public class UserService implements IUserService {

	private IUserRepository userRepository;
	
	@Autowired
	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
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
	public List<UserDTO> getAll(){
		List<User> users =  userRepository.findAll();
		List<UserDTO> ret_list = new ArrayList<UserDTO>();
		UserDTO dto;
		for (User u : users) {
			dto = new UserDTO(u.getEmail(), u.getUserSubject());
			ret_list.add(dto);
		}
		return ret_list;
	}
}
