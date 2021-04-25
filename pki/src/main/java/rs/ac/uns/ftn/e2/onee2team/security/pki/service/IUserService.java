package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.util.List;


import org.springframework.security.core.userdetails.UserDetailsService;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.RecoveryDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserRequestDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;

public interface IUserService extends UserDetailsService {

	User findByEmail(String email);

	List<UserDTO> getAll();

	User createUser(UserRequestDTO userRequest);

	boolean validateUser(Long id, String uuid);
	
	Boolean requestRecovery(String email);
	
	Boolean recovery(RecoveryDTO dto);
	
}
