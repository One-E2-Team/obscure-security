package rs.ac.uns.ftn.e2.onee2team.security.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.Authority;

public interface IAuthorityRepository extends JpaRepository<Authority, Long> {
	
	Authority findByName(String name);

}
