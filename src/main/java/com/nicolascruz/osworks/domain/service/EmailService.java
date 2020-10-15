package com.nicolascruz.osworks.domain.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.OrdemServico;

public interface EmailService {

	void sendOrderConfirmationEmail(OrdemServico obj);
	
	void sendOrderConfirmationEmail(Cliente obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(OrdemServico obj);
	
	void sendOrderConfirmationHtmlEmail(Cliente obj);
	
	void sendHtmlEmail(MimeMessage msg);
	
	void sendNewPasswordHtmlEmail(Cliente cliente, String newPass);
	
}
