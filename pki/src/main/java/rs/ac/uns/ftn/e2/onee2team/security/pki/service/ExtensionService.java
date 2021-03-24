package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.AvailableExtension;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.IExtensionRepository;


@Service
public class ExtensionService implements IExtensionService{
	IExtensionRepository extensionRepository;

	
	@Autowired
	public ExtensionService(IExtensionRepository extensionRepository) {
		this.extensionRepository = extensionRepository;
	}

	@Override
	public List<AvailableExtension> getAll() {
		return extensionRepository.findAll();
	}

}
