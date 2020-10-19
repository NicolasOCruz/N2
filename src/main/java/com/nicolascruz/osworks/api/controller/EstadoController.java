package com.nicolascruz.osworks.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicolascruz.osworks.api.model.CidadeDTO;
import com.nicolascruz.osworks.api.model.EstadoDTO;
import com.nicolascruz.osworks.domain.model.Cidade;
import com.nicolascruz.osworks.domain.model.Estado;
import com.nicolascruz.osworks.domain.service.CidadeService;
import com.nicolascruz.osworks.domain.service.EstadoService;

@RestController
@RequestMapping("/estado")
public class EstadoController {

	@Autowired
	private EstadoService service;
	
	@Autowired
	private CidadeService cidade;
	
	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll(){
		List<Estado> lista = service.findAll();
		List<EstadoDTO> listaDto = lista.stream().map(x -> new EstadoDTO(x)).collect(Collectors.toList());
		return ResponseEntity.ok(listaDto);
	}
	@GetMapping("/{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Long estadoId){
		List<Cidade> lista = cidade.findByEstado(estadoId);
		List<CidadeDTO> listaDto = lista.stream().map(x -> new CidadeDTO(x)).collect(Collectors.toList());
		return ResponseEntity.ok(listaDto);
	}
}
