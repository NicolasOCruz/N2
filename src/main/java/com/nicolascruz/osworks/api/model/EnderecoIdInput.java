package com.nicolascruz.osworks.api.model;

import javax.validation.constraints.NotNull;

public class EnderecoIdInput {
	
	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}