package com.nicolascruz.osworks.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.repository.ClienteRepository;

@Service
public class PageClienteService {
	
	@Autowired
	private ClienteRepository clienteRepositorio;
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepositorio.findAll(pageRequest);
	}

}
