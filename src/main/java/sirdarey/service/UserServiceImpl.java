package sirdarey.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.extern.slf4j.Slf4j;
import sirdarey.dto.GetALL_UsersRequest;
import sirdarey.dto.UserData;
import sirdarey.entity.Role;
import sirdarey.entity.User;
import sirdarey.repo.RoleRepo;
import sirdarey.repo.UserRepo;

@Service @Transactional @Slf4j @RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService{
	
	private final UserRepo userRepo;
	private final RoleRepo roleRepo;
	private final PasswordEncoder passwordEncoder;	

	@Override
	public UserData addUser(User newUser) {
		log.info("Saving new User {} to the Database", newUser.getName());
		
		var user = User.builder()
				.name(newUser.getName())
				.username(newUser.getUsername())
				.password(passwordEncoder.encode(newUser.getPassword()))
				.roles(newUser.getRoles())
				.build();
		return userData(userRepo.save(user));
	}

	@Override
	public Role addRole(Role newRole) {
		log.info("Saving new Role {} to the Database", newRole.getName());
		return roleRepo.save(newRole);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByName(rolename);
		
		log.info("Saving new Role {} to User {}", rolename, username);
		user.getRoles().add(role);
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			log.info("User {} NOT FOUND in the Database", username);
			throw new UsernameNotFoundException("User NOT FOUND in the Database");
		}
		
		log.info("User {} IS FOUND in the Database", username);
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		
		return new org.springframework.security.core.userdetails.User (
					user.getUsername(),
					user.getPassword(),
					authorities
				);
	}
	
	@Override
	public User getUser(String username) {
		log.info("Retrievng User {} from the Database", username);
		return userRepo.findByUsername(username);
	}

	@Override
	public GetALL_UsersRequest getUsers() {
		log.info("Retreiving ALL USERS from the Database");
		
		List<UserData> users = new ArrayList<>();
		userRepo.findAll().forEach(user -> {
			users.add(userData(user));
		});
		return new GetALL_UsersRequest(users);
	}

	@Override
	public UserData getUser(Long id) {
		return userData(userRepo.findById(id).get());
	}
	
	//Returns a USER_DATA from a USER object
	private UserData userData (User user) {
		return new UserData(user.getId(), user.getName(), user.getUsername(), user.getPassword(), user.getRoles());
	}

	@Override
	public UserData getUserData(String username) {
		return userData(userRepo.findByUsername(username));
	}

}
