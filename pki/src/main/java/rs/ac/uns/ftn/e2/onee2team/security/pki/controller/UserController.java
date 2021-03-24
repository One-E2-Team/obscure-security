package rs.ac.uns.ftn.e2.onee2team.security.pki.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')" + "||" + "hasRole('ROLE_INTERMEDIARY_CA')")
	public List<UserDTO> allUsers() {
		return userService.getAll();
	}
}
