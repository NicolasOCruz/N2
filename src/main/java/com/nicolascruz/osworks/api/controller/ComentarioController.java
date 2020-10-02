package com.nicolascruz.osworks.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nicolascruz.osworks.api.model.ComentarioInput;
import com.nicolascruz.osworks.api.model.ComentarioModel;
import com.nicolascruz.osworks.domain.model.Comentario;
import com.nicolascruz.osworks.domain.model.OrdemServico;
import com.nicolascruz.osworks.domain.repository.OrdemServicoRepository;
import com.nicolascruz.osworks.domain.service.GestaoOrdemServicoService;
import com.nicolascruz.osworks.domain.service.exceptions.ObjectNotFoundException;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class ComentarioController {
	
	@Autowired
	private GestaoOrdemServicoService gestao;
	
	@Autowired
	private ModelMapper modelMapper; //dependencia do Spring importada para converter classes
	
	@Autowired
	private OrdemServicoRepository ordemRepositorio;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioModel adicionar(@PathVariable Long ordemServicoId,
			@Valid @RequestBody ComentarioInput comentarioInput) {
		Comentario comentario = gestao.adicionarComentario(ordemServicoId, 
				comentarioInput.getDescricao());
		
		return toModel(comentario);
	}
	
	@GetMapping
	public List<ComentarioModel> listar(@PathVariable Long ordemServicoId){
		OrdemServico ordemServico = ordemRepositorio.findById(ordemServicoId)
				.orElseThrow(() -> new ObjectNotFoundException("Ordem de Serviço não encontrada"));
		
		return toCollectionModel(ordemServico.getComentarios());	
	}
	
	private ComentarioModel toModel(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioModel.class);
		
	}
	
	private List<ComentarioModel> toCollectionModel(List<Comentario> comentarios){
		return comentarios.stream()
				.map(comentario -> toModel(comentario))
				.collect(Collectors.toList());
	}
	
}
