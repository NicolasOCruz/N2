package com.nicolascruz.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.Comentario;
import com.nicolascruz.osworks.domain.model.OrdemServico;
import com.nicolascruz.osworks.domain.model.StatusOrdemServico;
import com.nicolascruz.osworks.domain.repository.ClienteRepository;
import com.nicolascruz.osworks.domain.repository.ComentarioRepository;
import com.nicolascruz.osworks.domain.repository.OrdemServicoRepository;
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
	
	public OrdemServico criar(OrdemServico ordemServico) {
		
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new DataIntegrityException("Cliente não encontrado"));
		
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordem.save(ordemServico);
		
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
}
