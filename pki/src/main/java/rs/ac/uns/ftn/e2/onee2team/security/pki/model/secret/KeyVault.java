package rs.ac.uns.ftn.e2.onee2team.security.pki.model.secret;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "key_vault")
public class KeyVault {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "privateKey", length = 2705, nullable = true)
	@Convert(converter = PrivateKeyConverter.class)
	private PrivateKey privateKey;
	
	@Column(name = "publicKey", length = 2705, nullable = false, unique = true)
	@Convert(converter = PublicKeyConverter.class)
	private PublicKey publicKey;
	
	@Column(name = "endDate", nullable = false)
	private Date validUntil;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

}
