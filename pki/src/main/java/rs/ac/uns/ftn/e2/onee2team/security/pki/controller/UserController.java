package rs.ac.uns.ftn.e2.onee2team.security.pki.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.RecoveryDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.IUserService;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	private IUserService userService;

	public UserController(IUserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "")
	@PreAuthorize("hasAuthority('READ_USERS')")
	public List<UserDTO> allUsers() {
		return userService.getAll();
	}

	@PostMapping(value = "/request-recovery")
	public ResponseEntity<String> requestRecovery(@RequestBody String email) {
		Boolean check =  userService.requestRecovery(email);
		if(check)
			return new ResponseEntity<String>("Successful request. Check email!", HttpStatus.OK);
		else
			return new ResponseEntity<String>("Bad email.", HttpStatus.I_AM_A_TEAPOT);
	}

	@PutMapping(value = "/recovery")
	public ResponseEntity<String> recovery(@Valid @RequestBody RecoveryDTO dto, UriComponentsBuilder ucBuilder) {
		if (userService.recovery(dto))
			return new ResponseEntity<String>("Recovery successful, you can login with new password.", HttpStatus.OK);
		else
			return new ResponseEntity<String>( "Illegal invocation.", HttpStatus.I_AM_A_TEAPOT);
	}
}
