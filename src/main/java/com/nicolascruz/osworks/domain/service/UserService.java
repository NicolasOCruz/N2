package com.nicolascruz.osworks.domain.service;

import org.springframework.security.core.context.SecurityContextHolder;

import com.nicolascruz.osworks.security.UserSS;

public class UserService {

	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //retorna o usuario logado
		}
		catch(Exception e) {
			return null;
		}
	}
	
}
