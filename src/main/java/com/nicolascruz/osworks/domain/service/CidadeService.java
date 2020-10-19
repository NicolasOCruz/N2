package com.nicolascruz.osworks.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nicolascruz.osworks.domain.model.Cidade;
import com.nicolascruz.osworks.domain.repository.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository cidade;
	
	public List<Cidade> findByEstado(Long estadoId){
		return cidade.findCidades(estadoId);
	}

}
