package com.nicolascruz.osworks.api.model;

import java.math.BigDecimal;
import java.util.Date;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.OrdemServico;
import com.nicolascruz.osworks.domain.model.StatusOrdemServico;

//Classe de modelo de representação do nosso recurso
public class OrdemServicoModel {

	private Long id;
	private ClienteResumoModel cliente;
	private String descricao;
	private BigDecimal preco;
	private StatusOrdemServico status;
	private Date dataAbertura;
	private Date dataFinalizacao;

	public OrdemServicoModel(OrdemServico obj) {
		id = (obj.getId() == null) ? null : obj.getId();
		preco = obj.getPreco();
		descricao = obj.getDescricao();
		cliente = toResumo(obj.getCliente());
		status = obj.getStatus();
		
		if (obj.formatDate(obj.getDataAbertura()) == null)
			dataAbertura = null;
		else
			dataAbertura = obj.formatDate(obj.getDataAbertura());
		
		if (obj.formatDate(obj.getDataFinalizacao()) == null)
			dataFinalizacao = null;
		else
			dataFinalizacao = obj.formatDate(obj.getDataFinalizacao());
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

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public StatusOrdemServico getStatus() {
		return status;
	}

	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public Date getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(Date dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public ClienteResumoModel getCliente() {
		return cliente;
	}

	public void setCliente(ClienteResumoModel cliente) {
		this.cliente = cliente;
	}
	
	private ClienteResumoModel toResumo(Cliente cliente) {
		ClienteResumoModel cli = new ClienteResumoModel();
		cli.setId(cliente.getId());
		cli.setNome(cliente.getNome());
		return cli;
	}
	
}