package com.nicolascruz.osworks.core;

import java.text.ParseException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nicolascruz.osworks.domain.service.EmailService;
import com.nicolascruz.osworks.domain.service.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
	
	@Bean
	public boolean instantiateDatabase() throws ParseException{
		return true;
	}
	
	@Bean //faz ficar disponivel como um componente no sistema. Se em outra classe houver uma injecao de dependencia, o Spring procurara um componente (esse, nesse caso), que retornara sua instancia
	public EmailService emailService() {
		return new SmtpEmailService(); 
	}

}