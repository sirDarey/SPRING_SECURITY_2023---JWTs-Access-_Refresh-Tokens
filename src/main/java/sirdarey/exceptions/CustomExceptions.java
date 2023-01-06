package sirdarey.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class CustomExceptions extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomExceptions(String message) {
		super(message);
	}
	
	public static void customExceptions (String message, HttpServletResponse response) throws IOException {
		response.setHeader("Error_Message", message);
		//response.setStatus(status);
		Map <String, String> error = new HashMap<>();
		error.put("Error_Message", message);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), error);
	}
}
