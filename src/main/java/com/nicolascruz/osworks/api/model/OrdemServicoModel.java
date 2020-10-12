package com.nicolascruz.osworks.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
	private OffsetDateTime dataAbertura;
	private OffsetDateTime dataFinalizacao;

	public OrdemServicoModel(OrdemServico obj) {
		id = (obj.getId() == null) ? null : obj.getId();
		preco = obj.getPreco();
		descricao = obj.getDescricao();
		cliente = toResumo(obj.getCliente());
		status = obj.getStatus();
		dataAbertura = obj.getDataAbertura();
		dataFinalizacao = (obj.getDataFinalizacao() == null) ? null : obj.getDataFinalizacao();
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

	public OffsetDateTime getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(OffsetDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public OffsetDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
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