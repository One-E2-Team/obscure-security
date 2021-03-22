package rs.ac.uns.ftn.e2.onee2team.security.pki.util;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

//Primer simetricnog sifrovanja
//Moze da se sifruje podatak prozivoljne duzine
public class AES {
	private static String algorithm = "AES/CBC/PKCS5Padding";
	
	public static SecretKey generateKey() throws GeneralSecurityException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(256);
	    SecretKey key = keyGenerator.generateKey();
	    return key;
	}
	
	public static IvParameterSpec generateIv() {
	    byte[] iv = new byte[16];
	    new SecureRandom().nextBytes(iv);
	    return new IvParameterSpec(iv);
	}
	
	public static byte[] encrypt(String plainText, SecretKey key, IvParameterSpec iv) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(algorithm);
	    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
	    byte[] cipherText = cipher.doFinal(plainText.getBytes());
	    return Base64.getEncoder().encodeToString(cipherText).getBytes();
	}
	
	public static byte[] decrypt(byte[] cipherText, SecretKey key, IvParameterSpec iv) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(algorithm);
	    cipher.init(Cipher.DECRYPT_MODE, key, iv);
	    byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
	    return plainText;
	}
}
