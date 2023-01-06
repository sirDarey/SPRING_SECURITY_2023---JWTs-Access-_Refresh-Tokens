package sirdarey.service;

import sirdarey.dto.GetALL_UsersRequest;
import sirdarey.dto.UserData;
import sirdarey.entity.Role;
import sirdarey.entity.User;

public interface UserService {

	UserData addUser (User newUser);
	Role addRole (Role newRole);
	void addRoleToUser (String username, String rolename);
	User getUser (String username);
	UserData getUser (Long id);
	UserData getUserData (String username);
	GetALL_UsersRequest getUsers();
}
