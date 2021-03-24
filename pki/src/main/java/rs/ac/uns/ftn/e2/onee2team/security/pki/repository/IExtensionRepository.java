package rs.ac.uns.ftn.e2.onee2team.security.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.AvailableExtension;

public interface IExtensionRepository extends JpaRepository<AvailableExtension, String>{

}
