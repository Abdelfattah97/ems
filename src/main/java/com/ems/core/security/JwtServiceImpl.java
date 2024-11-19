package com.ems.core.security;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtServiceImpl implements JwtService {

//	@Value("${JWT.SECRET}")
	private String secret;
//	@Value("${JWT.EXPIRATION_MILLIS}")
	private Long expireMillis;
	
//	@Value("${JWT.SECRET.ALGORITHM}") String algorithm ;
	
	private SecretKeySpec secretKeySpec;
	

    public  JwtServiceImpl(@Value("${JWT.EXPIRATION_MILLIS}") Long expireMillis , @Value("${JWT.SECRET}") String secret,@Value("${JWT.SECRET.ALGORITHM}") String algorithm ) {
    	this.expireMillis=expireMillis;
    	this.secret=secret;
    	this.secretKeySpec= new SecretKeySpec(secret.getBytes(), algorithm);
    }
	
	@Override
	public boolean validateToken(String token) {
		return token!=null&&!isTokenExpired(token);
	}

	@Override
	public String generateToken(String username) {
		return Jwts.builder()
		.subject(username)
		.issuedAt(new Date())
		.expiration(new Date(System.currentTimeMillis()+expireMillis))
		.signWith(secretKeySpec)
		.compact();
	}

	@Override
	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}
	
	private Claims extractClaims(String token) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return Jwts.parser()
        		.verifyWith(secretKeySpec)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload();
    }

	@Override
	public boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}

}
