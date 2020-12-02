package com.nicolascruz.osworks.api.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nicolascruz.osworks.api.model.EmailDTO;
import com.nicolascruz.osworks.domain.service.AuthService;
import com.nicolascruz.osworks.domain.service.UserService;
import com.nicolascruz.osworks.security.JWTUtil;
import com.nicolascruz.osworks.security.UserSS;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService auth;

	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) { //método para gerar outro token enquanto o usuario esta logado
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}

	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDto) { //método para gerar outro token enquanto o usuario esta logado
		auth.sendNewPassword(objDto.getEmail());
		return ResponseEntity.noContent().build();
	}
	
}
