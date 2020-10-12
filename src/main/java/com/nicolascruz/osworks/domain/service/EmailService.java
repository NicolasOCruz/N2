package com.nicolascruz.osworks.domain.service;

import org.springframework.mail.SimpleMailMessage;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.OrdemServico;

public interface EmailService {

	void sendOrderConfirmationEmail(OrdemServico obj);
	
	void sendOrderConfirmationEmail(Cliente obj);
	
	void sendEmail(SimpleMailMessage msg);
	
}
