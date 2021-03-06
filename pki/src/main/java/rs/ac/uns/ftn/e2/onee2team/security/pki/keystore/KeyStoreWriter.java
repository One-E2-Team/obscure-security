package rs.ac.uns.ftn.e2.onee2team.security.pki.keystore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyStoreWriter {

	private KeyStore keyStore;
	
	public KeyStoreWriter() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}
	
	public void loadKeyStore(String fileName, char[] password) {
		try {
			if(fileName != null) 
				keyStore.load(new FileInputStream(fileName), password);
			else {
				keyStore.load(null , password); // kreira se novi KeyStore
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveKeyStore(String fileName, char[] password) {
		try {
			keyStore.store(new FileOutputStream(fileName), password);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(String alias, PrivateKey privateKey, char[] password, X509Certificate certificate, List<X509Certificate> certChain) {
		try {
			if(certChain == null)
				keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
			else {
				Certificate[] certChainArray = new Certificate[certChain.size()+1];
				for(int i = 0; i < certChain.size(); i++) {
					certChainArray[i] = (Certificate) certChain.get(i);
				}
				certChainArray[certChainArray.length-1] = (Certificate) certificate;
				keyStore.setKeyEntry(alias, privateKey, password, certChainArray);
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}
	
	public void writeEnd(String alias, char[] password, X509Certificate certificate) {
		try {
			keyStore.setCertificateEntry(alias, certificate);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}
}
