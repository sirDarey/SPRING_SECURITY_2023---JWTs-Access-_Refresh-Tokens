package sirdarey.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.var;
import sirdarey.dto.UserData;
import sirdarey.entity.User;
import sirdarey.exceptions.CustomExceptions;
import sirdarey.repo.UserRepo;
import sirdarey.security.JWTService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;	
	private final JWTService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationResponse register(UserData registerRequest) {
		var user = User.builder()
				.name(registerRequest.getName())
				.username(registerRequest.getUsername())
				.password(passwordEncoder.encode(registerRequest.getPassword()))
				.roles(registerRequest.getRoles())
				.build();
		userRepo.save(user);
		
		var JWT = jwtService.generateToken(user, null);
		return AuthenticationResponse.builder()
				.tokens(JWT)
				.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws CustomExceptions {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
						authenticationRequest.getEmail(), 
						authenticationRequest.getPassword()
					)
				);
				
				var user = userRepo.findByUsername(authenticationRequest.getEmail());
				var JWT = jwtService.generateToken(user, null);
				return AuthenticationResponse.builder()
						.tokens(JWT)
						.build();
		} catch (Exception e) {
			throw new CustomExceptions("THIS USER DOESN'T EXIST");
		}
		
	}

}
