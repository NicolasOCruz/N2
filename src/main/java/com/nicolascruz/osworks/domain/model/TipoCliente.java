package com.nicolascruz.osworks.domain.model;

public enum TipoCliente {

	PessoaFisica(1, "Pessoa Física"),
	PessoaJuridica(2, "Pessoa Jurídica");
	
	private int codigo;
	private String descricao;
	
	private TipoCliente(int cod, String tipo) {
		this.codigo = cod;
		this.descricao = tipo;
	}
	
	public int getCod() {
		return this.codigo;
	}
	
	public String descricao() {
		return this.descricao;
	}
	
	public static TipoCliente toEnum(Integer cod) {
		
		if(cod == null) {
			return null;
		}
		
		for (TipoCliente x : TipoCliente.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
