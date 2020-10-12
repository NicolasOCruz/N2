package com.nicolascruz.osworks.domain.service;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.Comentario;
import com.nicolascruz.osworks.domain.model.EstadoPagamento;
import com.nicolascruz.osworks.domain.model.OrdemServico;
import com.nicolascruz.osworks.domain.model.PagamentoComBoleto;
import com.nicolascruz.osworks.domain.model.StatusOrdemServico;
import com.nicolascruz.osworks.domain.repository.ClienteRepository;
import com.nicolascruz.osworks.domain.repository.ComentarioRepository;
import com.nicolascruz.osworks.domain.repository.OrdemServicoRepository;
import com.nicolascruz.osworks.domain.repository.PagamentoRepository;
import com.nicolascruz.osworks.domain.service.exceptions.DataIntegrityException;
import com.nicolascruz.osworks.domain.service.exceptions.ObjectNotFoundException;

@Service
public class GestaoOrdemServicoService {

	@Autowired
	private OrdemServicoRepository ordem;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ComentarioRepository comentarioRepository;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private EmailService emailService;

	public Optional<OrdemServico> find(Long id) {
		Optional<OrdemServico> obj = ordem.findById(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Ordem de serviço não encontrada");
		}
		return obj;
	}

	public OrdemServico criar(OrdemServico ordemServico) {

		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new DataIntegrityException("Cliente não encontrado"));

		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		//
		ordemServico.setId(null);
		ordemServico.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		ordemServico.getPagamento().setOrdemServico(ordemServico);

		if (ordemServico.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pgto = (PagamentoComBoleto) ordemServico.getPagamento();
			boletoService.preencherPagamentoComBoleto(pgto, ordemServico.getDataAbertura());
		}

		ordemServico = ordem.save(ordemServico);

		pagamentoRepository.save(ordemServico.getPagamento());
		emailService.sendOrderConfirmationEmail(ordemServico);
		return ordemServico;

	}

	public void finalizar(Long ordemServicoId) {

		OrdemServico ordemServico = buscar(ordemServicoId);

		ordemServico.finalizar();

		ordem.save(ordemServico);
	}

	public void cancelar(Long ordemServicoId) {

		OrdemServico ordemServico = buscar(ordemServicoId);

		ordemServico.cancelar();

		ordem.save(ordemServico);
	}

	private OrdemServico buscar(Long ordemServicoId) {
		return ordem.findById(ordemServicoId)
				.orElseThrow(() -> new ObjectNotFoundException("Ordem de Serviço não encontrada"));
	}

	public Comentario adicionarComentario(Long ordemServicoId, String descricao) {

		OrdemServico ordemServico = buscar(ordemServicoId);

		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);

		return comentarioRepository.save(comentario);
	}

	public Page<OrdemServico> search(Long clienteId, Integer page, Integer linesPerPage, String orderBy,
			String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado"));
		
		//return ordem.findById(cliente.getId(), pageRequest);
		
		return ordem.findByClienteId(cliente.getId(), pageRequest);
	}

}