package sirdarey.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sirdarey.dto.UserData;
import sirdarey.exceptions.CustomExceptions;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;

	@PostMapping ("/register")
	public ResponseEntity<AuthenticationResponse> register (@RequestBody UserData registerRequest) {
		return ResponseEntity.status(201).body(authenticationService.register(registerRequest));
	}
	
	@PostMapping ("/authenticate")
	public ResponseEntity<AuthenticationResponse> register (@RequestBody AuthenticationRequest authenticationRequest) throws CustomExceptions {
		return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
	}
	
}
