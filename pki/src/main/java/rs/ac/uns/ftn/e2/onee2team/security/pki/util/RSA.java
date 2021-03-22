package rs.ac.uns.ftn.e2.onee2team.security.pki.util;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

	public static KeyPair generateKeys() {
        try {
			//Generator para kljuceva
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
			//Za kreiranje kljuceva neophodno je definisati generator pseudoslucajnih brojeva
			//Ovaj generator mora biti bezbedan (nije jednostavno predvideti koje brojeve ce RNG generisati)
			//U ovom primeru se koristi generator zasnovan na SHA1 algoritmu, gde je SUN provajder
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			//inicijalizacija generatora, 2048 bitni kljuc
			keyGen.initialize(2048, random);

			//generise par kljuceva koji se sastoji od javnog i privatnog kljuca
			return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static byte[] encrypt(String plainText, PublicKey key) {
		try {			
			//Kada se definise sifra potrebno je navesti njenu konfiguraciju, sto u ovom slucaju ukljucuje:
			//	- Algoritam koji se koristi (RSA)
			//	- Rezim rada tog algoritma (ECB)
			//	- Strategija za popunjavanje poslednjeg bloka (PKCS1Padding)
			//	- Provajdera kriptografskih funckionalnosti (BC)
			Cipher rsaCipherEnc = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			//inicijalizacija za sifrovanje
			rsaCipherEnc.init(Cipher.ENCRYPT_MODE, key);

			//sifrovanje
			byte[] cipherText = rsaCipherEnc.doFinal(plainText.getBytes());
			return cipherText;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decrypt(byte[] cipherText, PrivateKey key) {
		try {			
			Cipher rsaCipherDec = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			//inicijalizacija za desifrovanje
			rsaCipherDec.init(Cipher.DECRYPT_MODE, key);
			
			//desifrovanje
			byte[] plainText = rsaCipherDec.doFinal(cipherText);
			return plainText;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
