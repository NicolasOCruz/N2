package com.nicolascruz.osworks.domain.model;

public enum Perfil {
	
	TECNICO(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int codigo;
	private String descricao;
	
	private Perfil(int cod, String tipo) {
		this.codigo = cod;
		this.descricao = tipo;
	}
	
	public int getCod() {
		return this.codigo;
	}
	
	public String descricao() {
		return this.descricao;
	}
	
	public static Perfil toEnum(Integer cod) {
		
		if(cod == null) {
			return null;
		}
		
		for (Perfil x : Perfil.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}

}
