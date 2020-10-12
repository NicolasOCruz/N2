package com.nicolascruz.osworks.domain.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.OrdemServico;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Override
	public void sendOrderConfirmationEmail(OrdemServico obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromOrdemServico(obj);
		sendEmail(sm);
	}

	@Override
	public void sendOrderConfirmationEmail(Cliente obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromOrdemServico(obj);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromOrdemServico(OrdemServico obj) {

		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Ordem de Serviço gerada! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromOrdemServico(Cliente obj) {

		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getEmail());
		sm.setFrom(sender);
		sm.setSubject("Seu cadastro em nossa plataforma foi gerado! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}
}
