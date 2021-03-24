package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import java.util.Date;

public class PublicKeysDTO {

	private String publicKey;
	private Date validUntil;
	private boolean hasPrivate;
	
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public Date getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}
	public boolean isHasPrivate() {
		return hasPrivate;
	}
	public void setHasPrivate(boolean hasPrivate) {
		this.hasPrivate = hasPrivate;
	}
	
}
