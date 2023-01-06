package sirdarey;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import sirdarey.entity.Role;
import sirdarey.service.UserService;

@SpringBootApplication
@EnableTransactionManagement
public class SpringSECURTY3 {

	public static void main(String[] args) {
		SpringApplication.run(SpringSECURTY3.class, args);
	}
	
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			
			userService.addRole(new Role(null, "ROLE_USER"));
			userService.addRole(new Role(null, "ROLE_ADMIN"));
			userService.addRole(new Role(null, "ROLE_SUPERADMIN"));
			userService.addRole(new Role(null, "ROLE_SUPERVISOR"));
		};
	}
}
