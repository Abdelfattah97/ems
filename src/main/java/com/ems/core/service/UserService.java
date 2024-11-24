package com.ems.core.service;

import com.ems.core.model.AppUser;

public interface UserService {

	AppUser findByUsername(String username) ;
	
	AppUser findById(Long id);
	
	AppUser insert(AppUser appuser);
	
}
