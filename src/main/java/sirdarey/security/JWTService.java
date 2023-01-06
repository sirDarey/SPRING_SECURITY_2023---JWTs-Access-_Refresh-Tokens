package sirdarey.security;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JWTService {
	
	private final static String SECRET_KEY = "2A462D4A614E645267556B58703273357638792F413F4428472B4B6250655368";
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Map <String, String> generateToken (UserDetails userDetails, String refresh_token) {
		Map<String, List<String>> claims = new HashMap<>();
		List <String> roles = userDetails.getAuthorities()
								.stream().map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList());
		claims.put("Role", roles);
		
		String access_token = Jwts
								.builder()
								.setClaims(claims)
								.setSubject(userDetails.getUsername())
								.setIssuedAt(new Date(System.currentTimeMillis()))
								.setExpiration(new Date(System.currentTimeMillis() + (1000*60*60)))
								.signWith(getSigningKey(), SignatureAlgorithm.HS256)
								.compact();
		
		if (refresh_token == null) {
			refresh_token = Jwts
					.builder()
					.setSubject(userDetails.getUsername())
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + (1000*60*60*24)))
					.signWith(getSigningKey(), SignatureAlgorithm.HS256)
					.compact();
		}
		
		Map <String, String> tokens = new HashMap<>();
		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);
		
		return tokens;
	}
	
	public Boolean isTokenValid (String token, UserDetails userDetails, HttpServletResponse response) throws IOException {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token, response);
	}
	
	private boolean isTokenExpired(String token, HttpServletResponse response) throws IOException {
		return extractExpiration(token).before(new Date());			
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims (String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSigningKey() {
		byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}

