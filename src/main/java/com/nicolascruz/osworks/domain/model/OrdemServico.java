package com.nicolascruz.osworks.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.nicolascruz.osworks.domain.ValidationGroups;
import com.nicolascruz.osworks.domain.ValidationGroups.ClienteID;
import com.nicolascruz.osworks.domain.service.exceptions.DataIntegrityException;

@Entity
public class OrdemServico implements Serializable {

	private static final long serialVersionUID = 1L;

	// esta classe é de modelo, porém também é de representação, ou seja, além de
	// estar no pacote modelo ela retorna para a API requisitadora seus valores
	// caso eu não queira mostrar certos valores desta classe, é interessante criar
	// uma outra classe de representação para ajustar o que pode ser visto

	// essas validações não são necessárias, pois agora a entrada é feita via
	// OrdemServicoInput que já valida, se tiver outra forma de criar ordem de
	// serviço sem passar pela input ai sim mantem
	// OrdemServicoInput nao tem o Cliente, tem apenas o Id entao nao precisa criar
	// uma classe de validação

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Valid // Cascateamento de validação, valida também os campos dentro de Cliente
	@ConvertGroup(from = Default.class, to = ValidationGroups.ClienteID.class) // Quando fizer essa validação, não faz a
																				// default, faz essa outra
	@ManyToOne
	@NotNull
	@JsonIgnore
	// @JoinColumn(name = ) //caso queira colocar outra coluna que não seja a padrão
	// (ID)
	private Cliente cliente;

	@NotBlank
	private String descricao;

	@NotNull
	private BigDecimal preco;

	@Enumerated(EnumType.STRING)
	@JsonProperty(access = Access.READ_ONLY)
	private StatusOrdemServico status;

	@JsonProperty(access = Access.READ_ONLY)
	private OffsetDateTime dataAbertura; // LocalDateTime pega o horário do meridiano de Greenwitch

	@JsonProperty(access = Access.READ_ONLY)
	private OffsetDateTime dataFinalizacao;

	@OneToMany(mappedBy = "ordemServico") // para amarrar os relacionamentos desta classe com a classe de comentario
	private List<Comentario> comentarios = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "ordemServico") // para fazer mapeamento bidirecional 1:1
	private Pagamento pagamento;

	@ManyToOne
	// @JoinColumn(name="ENDERECO_ID")
	private Endereco endereco;

	public OrdemServico() {

	}

	public OrdemServico(Long id,
			@Valid @ConvertGroup(from = Default.class, to = ClienteID.class) @NotNull Cliente cliente,
			@NotBlank String descricao, @NotNull BigDecimal preco, StatusOrdemServico status,
			OffsetDateTime dataAbertura, OffsetDateTime dataFinalizacao, Endereco endereco) {
		super();
		this.id = id;
		this.cliente = cliente;
		this.descricao = descricao;
		this.preco = preco;
		this.status = status;
		this.dataAbertura = dataAbertura;
		this.dataFinalizacao = dataFinalizacao;
		this.endereco = endereco;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public void setDataAbertura(OffsetDateTime localDateTime) {
		this.dataAbertura = localDateTime;
	}

	public OffsetDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public BigDecimal getValorTotal() {
		return this.preco;
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
		OrdemServico other = (OrdemServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean podeSerFinalizada() {
		return StatusOrdemServico.ABERTA.equals(getStatus());
	}

	public boolean naoPodeSerFinalizada() {
		return !podeSerFinalizada();
	}

	public void finalizar() {

		if (naoPodeSerFinalizada()) {
			throw new DataIntegrityException("Ordem de serviço não pode ser finalizada");
		}
		setStatus(StatusOrdemServico.FINALIZADA);
		setDataFinalizacao(OffsetDateTime.now());
	}

	public boolean podeSerCancelada() {
		return StatusOrdemServico.ABERTA.equals(getStatus());
	}

	public boolean naoPodeSerCancelada() {
		return !podeSerCancelada();
	}

	public void cancelar() {

		if (naoPodeSerCancelada()) {
			throw new DataIntegrityException("Ordem de serviço não pode ser cancelada");
		}
		setStatus(StatusOrdemServico.CANCELADA);
	}

	@Override
	public String toString() {

		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date data = Date.from(getDataAbertura().toInstant());

		StringBuilder builder = new StringBuilder();
		builder.append("\nOrdem de Serviço:\nCódigo: ");
		builder.append(getId());
		builder.append("\nCliente: ");
		builder.append(getCliente().getNome());
		builder.append("\nDescrição: ");
		builder.append(getDescricao());
		builder.append("\nPreço: ");
		builder.append(nf.format(getPreco()));
		builder.append("\nData de Abertura: ");
		builder.append(sdf.format(data));
		builder.append("\nStatus: ");
		builder.append(getStatus());

		return builder.toString();

	}

}