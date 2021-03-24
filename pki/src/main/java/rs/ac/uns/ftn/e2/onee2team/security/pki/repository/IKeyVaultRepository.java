package rs.ac.uns.ftn.e2.onee2team.security.pki.repository;

import java.security.PublicKey;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.secret.KeyVault;

public interface IKeyVaultRepository extends JpaRepository<KeyVault, PublicKey> {

	KeyVault findByPublicKey(PublicKey pk);
}
