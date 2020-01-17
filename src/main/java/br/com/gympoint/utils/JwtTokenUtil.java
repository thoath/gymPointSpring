package br.com.gympoint.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.gympoint.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = -224721893136986598L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	@Value("${jwt.secret}")
	private String secret;
	

	/**
	 * Get the email from token
	 * @param token JWT token
	 * @return
	 */
	public String getEmailFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	/**
	 * Get if provider from token
	 * @param token JWT token
	 * @return
	 */
	public boolean getProviderFromToken(String token) {
		return (boolean) getAllClaimsFromToken(token).get("isProvider");
	}
	
	/**
	 * Get if provider from token
	 * @param token JWT token
	 * @return
	 */
	public Integer getUserIdToken(String token) {
		return (Integer) getAllClaimsFromToken(token).get("userId");
	}
	
	/**
	 * Get expiration date from JWT token
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	/**
	 * Get an property from JWT token
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    
	/*
	 * Get all properties from JWT token using teh secret key
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	/*
	 * Validate JWT token if token is expired
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	/**
	 * Generate a JWt tokjeb 
	 * @param userDetails
	 * @return
	 */
	public String generateToken(User userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId",userDetails.getId());
		claims.put("isProvider", userDetails.isProvider());
		
		return doGenerateToken(claims, userDetails.getEmail());
	}
	
	/*
	 * Generate a token
	 */
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return 
			Jwts
			.builder()
			.setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
			.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	/**
	 * validate the JWT token
	 * @param token
	 * @param userDetails
	 * @return
	 */
	public Boolean validateToken(String token, User userDetails) {
		final String email = getEmailFromToken(token);
		return (email.equals(userDetails.getEmail()) && !isTokenExpired(token));
	}
	
}
