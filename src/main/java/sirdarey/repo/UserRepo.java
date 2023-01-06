package sirdarey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sirdarey.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{
	
	User findByUsername (String username);
}
