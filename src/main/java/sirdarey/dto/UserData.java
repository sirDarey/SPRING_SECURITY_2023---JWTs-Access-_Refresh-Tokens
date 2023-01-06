package sirdarey.dto;

import java.util.ArrayList;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sirdarey.entity.Role;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserData {
	
	private Long id;
	private String name;
	private String username;
	private String password;
	private Collection<Role> roles = new ArrayList<>();
}