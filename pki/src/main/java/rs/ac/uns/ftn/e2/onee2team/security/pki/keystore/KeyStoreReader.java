package rs.ac.uns.ftn.e2.onee2team.security.pki.keystore;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class KeyStoreReader {

	private KeyStore keyStore;
	
	public KeyStoreReader() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}
	
	public X509Certificate[] readCertificateChain(String keyStoreFile, String keyStorePass, String alias) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, keyStorePass.toCharArray());
			X509Certificate[] ret;
			if(keyStoreFile.contains("root")) {
				ret = new X509Certificate[1];
				ret[0] = (X509Certificate) keyStore.getCertificate(alias);
			}else {
				Certificate[] certChain = keyStore.getCertificateChain(alias);
				ret = new X509Certificate[certChain.length];
				for(int i = 0; i < certChain.length; i++) {
					ret[i] = (X509Certificate)certChain[i];
				}
			}
			return ret;
			
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
