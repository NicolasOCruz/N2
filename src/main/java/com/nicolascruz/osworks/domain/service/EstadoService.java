package com.nicolascruz.osworks.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nicolascruz.osworks.domain.model.Estado;
import com.nicolascruz.osworks.domain.repository.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository estado;
	
	public List<Estado> findAll(){
		return estado.findAllByOrderByNome();
	}

}
