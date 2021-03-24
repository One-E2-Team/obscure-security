package rs.ac.uns.ftn.e2.onee2team.security.pki.util;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public class ExtensionFormat {

	private byte[] encodedValue;
	private boolean critical = false;
	private ASN1ObjectIdentifier field;
	public byte[] getValue() {
		return encodedValue;
	}
	public void setValue(byte[] value) {
		this.encodedValue = value;
	}
	public boolean isCritical() {
		return critical;
	}
	public void setCritical(boolean critical) {
		this.critical = critical;
	}
	public ASN1ObjectIdentifier getField() {
		return field;
	}
	public void setField(ASN1ObjectIdentifier field) {
		this.field = field;
	}
	
}
