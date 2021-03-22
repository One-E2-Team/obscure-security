package rs.ac.uns.ftn.e2.onee2team.security.pki.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
public class Administrator extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
