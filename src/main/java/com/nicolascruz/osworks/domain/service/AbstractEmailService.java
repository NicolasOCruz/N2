package com.nicolascruz.osworks.domain.service;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.OrdemServico;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

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
		sm.setSubject("Zurc Tech - Ordem de Serviço gerada!" + obj.toString());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromOrdemServico(Cliente obj) {

		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getEmail());
		sm.setFrom(sender);
		sm.setSubject("Zurc Tech - Olá " + obj.getNome() + "!" + " Seu cadastro em nossa plataforma foi gerado!");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}

	protected String htmlFromTemplatePedido(OrdemServico obj) {
		Context context = new Context(); // precisa desse objeto pra acessar o template HTML
		context.setVariable("ordem", obj); // para povoar o template
		return templateEngine.process("email/confirmacaoOrdem", context);
	}

	protected String htmlFromTemplatePedido(Cliente obj) {
		Context context = new Context(); // precisa desse objeto pra acessar o template HTML
		context.setVariable("cliente", obj); // para povoar o template
		return templateEngine.process("email/confirmacaoCliente", context);
	}

	@Override
	public void sendOrderConfirmationHtmlEmail(OrdemServico obj) {
		try {
			MimeMessage sm = prepareMimeMessageFromOrdemServico(obj);
			sendHtmlEmail(sm);
		} catch(MessagingException e) {
			sendOrderConfirmationEmail(obj);
		}
	}

	@Override
	public void sendOrderConfirmationHtmlEmail(Cliente obj) {
		try {
			MimeMessage sm = prepareMimeMessageFromOrdemServico(obj);
			sendHtmlEmail(sm);
		} catch(MessagingException e) {
			sendOrderConfirmationEmail(obj);
		}
		
	}

	protected MimeMessage prepareMimeMessageFromOrdemServico(OrdemServico obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Zurc Tech - Ordem de Serviço gerada! Código: " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		return mimeMessage;
	}

	protected MimeMessage prepareMimeMessageFromOrdemServico(Cliente obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Zurc Tech - Olá " + obj.getNome() + "!" + " Seu cadastro em nossa plataforma foi gerado!");
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		return mimeMessage;
	}
}
