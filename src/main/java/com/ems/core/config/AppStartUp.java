package com.ems.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ems.core.model.AppRole;
import com.ems.core.model.AppUser;
import com.ems.core.repository.AppRoleRepository;
import com.ems.core.repository.AppUserRepository;

@Configuration
public class AppStartUp implements CommandLineRunner {

	@Autowired
	private AppUserRepository userRepo;
	@Autowired
	private AppRoleRepository roleRepo;
	@Autowired
	private PasswordEncoder encoder;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void run(String... args) throws Exception {
		AppRole roleAdmin = roleRepo.findByRoleName("admin").orElseGet(() -> {
			AppRole role = new AppRole();
			role.setRoleName("admin");
			return roleRepo.save(role);
		});
		AppRole roleUser = roleRepo.findByRoleName("user").orElseGet(() -> {
			AppRole role = new AppRole();
			role.setRoleName("user");
			return roleRepo.save(role);
		});

		String adm = "admin";
		AppUser admin = userRepo.findByUsername(adm).orElseGet(() -> {
			AppUser appUser = new AppUser();
			appUser.setUsername(adm);
			appUser.setPassword(encoder.encode("password"));
			appUser.addRole(roleAdmin);
			return userRepo.save(appUser);
		});
		
		String usr = "user";
		AppUser user = userRepo.findByUsername(usr).orElseGet(() -> {
			AppUser appUser = new AppUser();
			appUser.setUsername(usr);
			appUser.setPassword(encoder.encode("password"));
			appUser.addRole(roleUser);
			return userRepo.save(appUser);
		});
		
		
		logger.info("Initial Data has been inserted to database");
		
	}

}
