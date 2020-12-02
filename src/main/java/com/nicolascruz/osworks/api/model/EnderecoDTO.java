package com.nicolascruz.osworks.api.model;

import java.io.Serializable;

import com.nicolascruz.osworks.domain.model.Endereco;

public class EnderecoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	

	private String logradouro;
	
	
	private String numero;
	

	private String complemento;
	
	
	private String bairro;
	
	
	private String cep;
	
	
	private Long cidadeId;
	
	
	public EnderecoDTO() {
		
	}
	
	public EnderecoDTO(Endereco end) {
		id = end.getId();
		logradouro = end.getLogradouro();
		numero = end.getNumero();
		complemento = end.getComplemento();
		bairro = end.getBairro();
		cep = end.getCep();
		cidadeId = end.getCidade().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Long getCidadeId() {
		return cidadeId;
	}

	public void setCidadeId(Long cidadeId) {
		this.cidadeId = cidadeId;
	}

}
