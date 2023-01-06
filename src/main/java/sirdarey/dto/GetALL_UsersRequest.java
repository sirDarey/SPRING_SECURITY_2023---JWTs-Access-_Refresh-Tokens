package sirdarey.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter 
public class GetALL_UsersRequest {

	private List<UserData> ALL_USERS;
}
