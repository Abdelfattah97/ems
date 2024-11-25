package com.ems.core.security.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

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

	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver exceptionResolver;
	

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.debug("JWT FILTER START!");
		
		String token = getTokenFromRequest(request);
		if(token==null) {
			logger.debug("JWT FILTER: No token found. Proceeding to next filter without authentication");
            filterChain.doFilter(request, response);
            return;
		}
		try {
			if (jwtService.validateToken(token)) {
				AppUser user = null;
				user = userRepo.findByUsername(jwtService.extractUsername(token))
						.orElseThrow(() -> new UsernameNotFoundException("Token's owner was not found!"));
				Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
						user.getPassword(), user.getRoles().stream().map(r -> {
							return new SimpleGrantedAuthority("ROLE_" + r.getAuthority());
						}).toList());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			logger.debug("JWT FILTER: Proceeding to next filter ");
			filterChain.doFilter(request, response);
		} catch (RuntimeException ex) {
			logger.debug("Exception caught while validating the jwt token!",ex);
			exceptionResolver.resolveException(request, response, null, new BadCredentialsException("provided Token was invalid!"));
		} 

	}

	private String getTokenFromRequest(HttpServletRequest request) {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}

}
