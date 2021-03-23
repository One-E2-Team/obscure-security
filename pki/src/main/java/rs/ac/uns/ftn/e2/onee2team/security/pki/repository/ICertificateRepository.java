package rs.ac.uns.ftn.e2.onee2team.security.pki.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.UserDefinedSubject;

public interface ICertificateRepository extends JpaRepository<Certificate, Long> {

	Certificate findBySerialNumber(Long serialNumber);

	@Query("select c from Certificate c where c.issuer.id = ?1")
	public List<Certificate> findCertificatesByIssuerId(Long issuer_id);

	@Query("select c from Certificate c where (c.subject.userSubject = ?1 or c.issuer.userSubject = ?1) and "
			+ "c.revoked = false and c.endDate > CURRENT_TIMESTAMP")
	public List<Certificate> findCertificatesByUserSubject(UserDefinedSubject userSubject);

}
