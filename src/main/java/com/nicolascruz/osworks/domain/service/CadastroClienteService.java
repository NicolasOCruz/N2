package com.nicolascruz.osworks.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.repository.ClienteRepository;
import com.nicolascruz.osworks.domain.repository.EnderecoRepository;
import com.nicolascruz.osworks.domain.service.exceptions.DataIntegrityException;

@Service
public class CadastroClienteService {

	@Autowired
	private ClienteRepository clienteRepositorio;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Transactional
	public Cliente salvar(Cliente cliente) {
		
		Cliente clienteExistente = clienteRepositorio.findByCpf(cliente.getCpf());
	
		if (clienteExistente != null && !clienteExistente.equals(cliente)) {
			throw new DataIntegrityException("Já existe um cliente cadastrado nesse CPF");
		}

		cliente.setId(null);

		cliente = clienteRepositorio.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}
	
	public Cliente update(Cliente cliente) {
		return clienteRepositorio.save(cliente);
	}

	public void excluir(Long clienteId) {
		clienteRepositorio.deleteById(clienteId);
	}
}
