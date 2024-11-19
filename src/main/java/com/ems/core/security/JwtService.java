package com.ems.core.security;

public interface JwtService {

	public boolean validateToken(String token) ;
	
	public String generateToken(String username) ;
	
	public String extractUsername(String token);

	public boolean isTokenExpired(String token);
}
