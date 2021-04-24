package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.util.List;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.Authority;

public interface IAuthorityService {
	List<Authority> findById(Long id);
	List<Authority> findByname(String name);
}
