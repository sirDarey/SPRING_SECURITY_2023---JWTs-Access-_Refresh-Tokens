package sirdarey.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import sirdarey.dto.GetALL_UsersRequest;
import sirdarey.dto.UserData;
import sirdarey.entity.Role;
import sirdarey.entity.User;
import sirdarey.exceptions.CustomExceptions;
import sirdarey.security.JWTService;
import sirdarey.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final JWTService jwtService;
	
	@GetMapping("/users")
	public ResponseEntity<GetALL_UsersRequest> getUsers () throws CustomExceptions {
		try {
			return ResponseEntity.ok().body(userService.getUsers());
		} catch (Exception e) {
			throw new CustomExceptions(e.getMessage());
		}	
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<UserData> getUser (@PathVariable Long id) throws CustomExceptions {
		try {
			return ResponseEntity.ok(userService.getUser(id));
		} catch (Exception e) {
			throw new CustomExceptions(e.getMessage());
		}		
	}
	
	@PostMapping("/users")
	public ResponseEntity<UserData> addUser (@RequestBody User newUser) throws CustomExceptions {
		//URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
		try {
			return ResponseEntity.status(201).body(userService.addUser(newUser));
		} catch (Exception e) {
			throw new CustomExceptions(e.getMessage());
		}
	}
	
	@PostMapping("/roles")
	public ResponseEntity<Role> addRole (@RequestBody Role newRole) {
		return ResponseEntity.ok().body(userService.addRole(newRole));
	}
	
	@PostMapping("/roles/addtouser")
	public ResponseEntity<UserData> addRoleToUser (@RequestBody RoleToUserForm newRole) {
		userService.addRoleToUser(newRole.getUsername(), newRole.getRolename());
		return ResponseEntity.ok(userService.getUserData(newRole.getUsername()));
	}
	
	@GetMapping("/token/refresh")
	public void refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException, CustomExceptions {
		final String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new CustomExceptions("Refresh Token NOT FOUND");
		}
		
		try {
			final String refresh_token = authHeader.substring(7);
			final String username = jwtService.extractUsername(refresh_token);
			User user = userService.getUser(username);
			
			Map <String, String> tokens = jwtService.generateToken(user, refresh_token);
			
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			
		} catch (Exception e) {
			CustomExceptions.customExceptions(e.getMessage(), response);
		}
		
	}	
}

@Data
class RoleToUserForm {
	private String username;
	private String rolename;
}
