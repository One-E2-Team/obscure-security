package rs.ac.uns.ftn.e2.onee2team.security.pki.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;

public interface ICertificateRepository extends JpaRepository<Certificate, Long>{

	Certificate findBySerialNumber(Long serialNumber);
	
	@Query("select c from Certificate c where c.issuer.id = ?1")
	List<Certificate> findCertificatesByIssuerId(Long issuer_id);
	
}
