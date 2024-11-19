package com.ems.core.security.filter;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ems.core.model.AppUser;
import com.ems.core.repository.AppUserRepository;
import com.ems.core.security.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private AppUserRepository userRepo;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.info("JWT FILTER START!");
		String token = getTokenFromRequest(request);
		logger.info(token);
		if (token != null && jwtService.validateToken(token)) {
			AppUser user = null;
			try {
				user = userRepo.findByUsername(jwtService.extractUsername(token))
						.orElseThrow(() -> new UsernameNotFoundException("Token's username was not found!"));
				Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
						user.getRoles().stream().map(r->{ return new SimpleGrantedAuthority( "ROLE_"+ r.getAuthority());}).toList());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (UsernameNotFoundException ex) {
				logger.info("Invalid token, username was not found! PROCEEDING TO NEXT FILTER...");
//				throw ex;
				throw new AuthenticationException("Authentication Failed!");
			}
		}
		filterChain.doFilter(request, response);

	}

	private String getTokenFromRequest(HttpServletRequest request) {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}

}
