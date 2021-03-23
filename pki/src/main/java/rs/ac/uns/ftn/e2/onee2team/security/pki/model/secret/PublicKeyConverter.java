package rs.ac.uns.ftn.e2.onee2team.security.pki.model.secret;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.persistence.AttributeConverter;

import rs.ac.uns.ftn.e2.onee2team.security.pki.util.Base64Utility;

public class PublicKeyConverter implements AttributeConverter<PublicKey, String> {

	@Override
	public String convertToDatabaseColumn(PublicKey attribute) {
		String data = new String(Base64Utility.encode(attribute.getEncoded()));
		return data;
	}

	@Override
	public PublicKey convertToEntityAttribute(String dbData) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64Utility.decode(dbData));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			publicKey = kf.generatePublic(spec);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

}
