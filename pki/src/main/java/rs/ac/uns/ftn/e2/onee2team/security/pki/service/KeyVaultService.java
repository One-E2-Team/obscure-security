package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.security.KeyPair;
import java.util.Date;

import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.secret.KeyVault;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.IKeyVaultRepository;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.RSA;

@Service
public class KeyVaultService implements IKeyVaultService {

	private IKeyVaultRepository keyVaultRepository;
	
	public KeyVaultService(IKeyVaultRepository keyVaultRepository) {
		this.keyVaultRepository = keyVaultRepository;
	}
	
	public KeyVault saveKeys(KeyVault kv) {
		return keyVaultRepository.save(kv);
	}

	@Override
	public void dothething() {
		for(int i = 0; i<10; i++) {
			KeyPair kp = RSA.generateKeys();
			KeyVault kv = new KeyVault();
			kv.setPublicKey(kp.getPublic());
			kv.setPrivateKey(kp.getPrivate());
			kv.setValidUntil(new Date((new Date()).getTime() + 315360000000L));
			this.keyVaultRepository.save(kv);
		}
	}
}
