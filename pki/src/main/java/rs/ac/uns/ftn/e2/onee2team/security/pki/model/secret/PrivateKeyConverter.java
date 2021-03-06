package rs.ac.uns.ftn.e2.onee2team.security.pki.model.secret;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Autowired;

import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.IKeyVaultRepository;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.AES;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.Base64Utility;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.RSA;

public class PrivateKeyConverter implements AttributeConverter<PrivateKey, String> {

	private static SecretKey key;
	private static IvParameterSpec iv;
	
	static {
		/*try {
			System.out.println("AES SK:" + Base64Utility.encode(AES.generateKey().getEncoded()));
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("IV: " + Base64Utility.encode(AES.generateIv().getIV()));*/
		try {
			key = new SecretKeySpec(Base64Utility.decode(System.getenv("FTN_SECURITY_AES_SHARED_KEY")), "AES");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			iv = new IvParameterSpec(Base64Utility.decode(System.getenv("FTN_SECURITY_AES_IV")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String convertToDatabaseColumn(PrivateKey attribute) {
		if(attribute == null) return null;
		String data = null;
		try {
			data = new String(AES.encrypt(new String(Base64Utility.encode(attribute.getEncoded())), key, iv));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public PrivateKey convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		byte[] data = null;
		try {
			data = Base64Utility.decode(new String(AES.decrypt(dbData.getBytes(), key, iv)));
		} catch (IOException | GeneralSecurityException e2) {
			e2.printStackTrace();
		}
		PrivateKey pk = null;
		PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(data);
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			pk = kf.generatePrivate(ks);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return pk;
	}


}
