package com.astarel.school.auth;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Autowired
	private ObjectMapper objectMapper;

	private static final String SECRET_KEY = "Df+dH83Ix9N00U7zYNNf5DWMTrAQRiu5R84sMArjVoo=";

	private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 1 days

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public List<String> extractRoles(String token) {
		Claims claims = extractAllClaims(token);
		Object rolesObj = claims.get("roles");
		return objectMapper.convertValue(rolesObj, new TypeReference<List<String>>() {
		});
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	@SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String generateToken(String username, List<String> roles) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", roles); // e.g., List.of("ROLE_ADMIN")
		return createToken(claims, username);
	}

	@SuppressWarnings("deprecation")
	private String createToken(Map<String, Object> claims, String username) {

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, getSignKey()).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
