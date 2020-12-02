package com.nicolascruz.osworks.api.model;

import java.util.Date;

import com.nicolascruz.osworks.domain.model.Comentario;

public class ComentarioModel {

	private Long id;
	private String descricao;
	private Date dataEnvio;
	
	public ComentarioModel() {
		
	}
	
	public ComentarioModel(Comentario comentario) {
		id = comentario.getId();
		descricao = comentario.getDescricao();
		
		if (comentario.formatDate(comentario.getDataEnvio()) == null)
			dataEnvio = null;
		else
			dataEnvio = comentario.formatDate(comentario.getDataEnvio());
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getDataEnvio() {
		return dataEnvio;
	}
	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}
}