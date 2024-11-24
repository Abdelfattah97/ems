package com.ems.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ems.core.exceptions.ResourceNotFoundException;
import com.ems.core.model.AppUser;
import com.ems.core.repository.AppUserRepository;

@Service
public class UserServiceImpl implements UserService , UserDetailsService {

	@Autowired
	private AppUserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AppUser appUser = userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Invalid Credentials!"));
		
		String[] roles = appUser.getRoles().stream().map(r->r.getAuthority()).toArray(String[]::new);
		
		return User.builder().username(username).password(appUser.getPassword()).roles(roles).build();
	}

	@Override
	public AppUser findByUsername(String username) {
		return userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(String.format("Username (%s) not found!")));
	}

	@Override
	public AppUser findById(Long id) {
		return userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format("No User with id: %s found!", id)));
	}

	@Override
	public AppUser insert(AppUser appuser) {
		appuser.setPassword(passwordEncoder.encode(appuser.getPassword()));
		return userRepo.save(appuser);
	}

}
