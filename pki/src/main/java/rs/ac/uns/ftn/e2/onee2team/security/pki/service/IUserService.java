package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;

public interface IUserService extends UserDetailsService {

	User findByEmail(String email);

}
