package rs.ac.uns.ftn.e2.onee2team.security.pki.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.UserDefinedSubject;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;

public interface IUserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	@Query("select u from User u where u.userSubject = ?1")
	User findUserByUserDefinedSubject(UserDefinedSubject uds);
	@Query(value = "select * from all_users a where a.email like %?1%", nativeQuery = true)
	List<User> getUsers(String text);
}
