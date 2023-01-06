package sirdarey.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sirdarey.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer>{
	
	Role findByName(String name);
}
