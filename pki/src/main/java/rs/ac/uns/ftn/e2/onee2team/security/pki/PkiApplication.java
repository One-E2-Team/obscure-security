package rs.ac.uns.ftn.e2.onee2team.security.pki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
@EnableAsync
public class PkiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PkiApplication.class, args);
	}
}
