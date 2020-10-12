package com.nicolascruz.osworks.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nicolascruz.osworks.domain.ValidationGroups;
import com.nicolascruz.osworks.domain.ValidationGroups.ClienteID;

@Entity
public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(groups = ValidationGroups.ClienteID.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
/*	@Column	
	@NotBlank
	@Size(max = 60)*/ //tirei porque Cliente Input já faz essas validações
	private String nome;
	
	/*@NotBlank
	@Email
	@Size(max = 255)*/
	@Column(unique = true)
	private String email;
	
	/*@Column
	@NotBlank
	@Size(max = 20)*/
	private String telefone;
	
	
	private String cpf;
	
	private Integer tipo;
	
	//@JsonManagedReference //(nao mais utilizado) como a associação entre os dois é bidirecional, para não criar uma serialização ciclica, onde um fica buscando o outro constantemente, é necessária essa anotação
    @OneToMany(mappedBy = "cliente", cascade=CascadeType.ALL) //para amarrar os relacionamentos desta classe com a classe de endereco, cascade para informar que, ao apagar o cliente, apague os enderecos tbm
	private List<Endereco> enderecos = new ArrayList<>();
	
    @JsonIgnore
	@OneToMany(mappedBy = "cliente") 
	private List<OrdemServico> ordensServicos = new ArrayList<>();
	
	public Cliente() {
		
	}
	public Cliente(@NotNull(groups = ClienteID.class) Long id, String nome, String email, String telefone, String cpf,
			TipoCliente tipo) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.cpf = cpf;
		this.tipo = (tipo==null) ? null : tipo.getCod();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}
	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();
	}
	public List<Endereco> getEnderecos() {
		return enderecos;
	}
	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}
	public List<OrdemServico> getOrdensServicos() {
		return ordensServicos;
	}
	public void setOrdensServicos(List<OrdemServico> ordensServicos) {
		this.ordensServicos = ordensServicos;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		Integer count = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("\nCliente: Código: ");
		builder.append(getId());
		builder.append("\nEmail: ");
		builder.append(getEmail());
		if (cpf.length() > 11) { 
			builder.append("\nCNPJ: ");
		} else {
			builder.append("\nCPF: ");
		}
		builder.append(getCpf());
		for(Endereco e : getEnderecos()) {
			count = count + 1;
			builder.append("\nEndereço " + count + ":\n");
			builder.append("CEP: " + e.getCep() + " - " + e.getLogradouro() + " - " + e.getNumero());
		}
		return builder.toString();
	}
	
}
