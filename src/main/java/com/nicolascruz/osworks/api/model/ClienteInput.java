package com.nicolascruz.osworks.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ClienteInput implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@Size(max = 60)
	@Column
	private String nome;
	
	@NotBlank
	@Email
	@Size(max = 255)
	private String email;
	
	@Column
	@NotBlank
	@Size(max = 20)
	private String telefone;
	
	@Column
	@NotBlank
	@Size(max = 15)
	private String cpf;
	
	@Column
	@NotNull
	private Integer tipo;
	
	@NotBlank
	private String logradouro;
	
	@NotBlank
	private String numero;
	private String complemento;
	private String bairro;
	
	@NotBlank
	private String cep;
	
	private Long cidadeId;
	
	
	public ClienteInput() {
		
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
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

	@Override
	public String toString() {
		return "ClienteInput [nome=" + nome + ", email=" + email + ", telefone=" + telefone + ", cpf=" + cpf + ", tipo="
				+ tipo + ", logradouro=" + logradouro + ", numero=" + numero + ", complemento=" + complemento
				+ ", bairro=" + bairro + ", cep=" + cep + ", cidadeId=" + cidadeId + "]";
	}
	
}
