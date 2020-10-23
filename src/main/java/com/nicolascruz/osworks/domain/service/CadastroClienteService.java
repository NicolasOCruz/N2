package com.nicolascruz.osworks.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nicolascruz.osworks.api.model.ClienteDTO;
import com.nicolascruz.osworks.api.model.EnderecoDTO;
import com.nicolascruz.osworks.domain.model.Cidade;
import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.Endereco;
import com.nicolascruz.osworks.domain.repository.CidadeRepository;
import com.nicolascruz.osworks.domain.repository.ClienteRepository;
import com.nicolascruz.osworks.domain.repository.EnderecoRepository;
import com.nicolascruz.osworks.domain.service.exceptions.DataIntegrityException;
import com.nicolascruz.osworks.domain.service.exceptions.ObjectNotFoundException;

@Service
public class CadastroClienteService {

	@Autowired
	private ClienteRepository clienteRepositorio;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Transactional
	public Cliente salvar(Cliente cliente) {
		
		Cliente clienteExistente = clienteRepositorio.findByCpf(cliente.getCpf());
	
		if (clienteExistente != null && !clienteExistente.equals(cliente)) {
			throw new DataIntegrityException("Existe um cliente cadastrado nesse CPF/CNPJ");
		}

		cliente.setId(null);

		cliente = clienteRepositorio.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		emailService.sendOrderConfirmationHtmlEmail(cliente);
		return cliente;
	}
	
	public Cliente update(Cliente cliente) {
		
		Cliente newObj = clienteRepositorio.findById(cliente.getId()).orElse(null);
		updateData(newObj, cliente);
		
		return clienteRepositorio.save(newObj);
	}

	public void excluir(Long clienteId) {
		clienteRepositorio.deleteById(clienteId);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null, null);
	}
	
	private void updateData(Cliente newObj, Cliente cliente) {
		newObj.setNome(cliente.getNome());
		newObj.setEmail(cliente.getEmail());
	}

	public Endereco adicionarEndereco(Long clienteId, EnderecoDTO endereco) {
		
		Cliente cliente = clienteRepositorio.findById(clienteId)
				.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado"));
		
		Cidade cidade = cidadeRepository.findById(endereco.getCidadeId())
				.orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada"));
		
		Endereco end = new Endereco();
		end.setLogradouro(endereco.getLogradouro());
		end.setBairro(endereco.getBairro());
		end.setCep(endereco.getCep());
		end.setComplemento(endereco.getComplemento());
		end.setNumero(endereco.getNumero());
		end.setCidade(cidade);
		end.setCliente(cliente);

		return enderecoRepository.save(end);
	}
}
