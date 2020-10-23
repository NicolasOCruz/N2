package com.nicolascruz.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nicolascruz.osworks.api.model.ClienteDTO;
import com.nicolascruz.osworks.api.model.ClienteInput;
import com.nicolascruz.osworks.api.model.ClienteModel;
import com.nicolascruz.osworks.domain.model.Cidade;
import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.Endereco;
import com.nicolascruz.osworks.domain.model.Perfil;
import com.nicolascruz.osworks.domain.model.TipoCliente;
import com.nicolascruz.osworks.domain.repository.CidadeRepository;
import com.nicolascruz.osworks.domain.repository.ClienteRepository;
import com.nicolascruz.osworks.domain.service.CadastroClienteService;
import com.nicolascruz.osworks.domain.service.PageClienteService;
import com.nicolascruz.osworks.domain.service.UserService;
import com.nicolascruz.osworks.domain.service.exceptions.AuthorizationException;
import com.nicolascruz.osworks.domain.service.exceptions.DataIntegrityException;
import com.nicolascruz.osworks.domain.service.exceptions.ObjectNotFoundException;
import com.nicolascruz.osworks.security.UserSS;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CadastroClienteService cadastroCliente;

	@Autowired
	private PageClienteService paginaCliente;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private BCryptPasswordEncoder bc;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public List<ClienteModel> listar() {
		return toCollectionModel(clienteRepository.findAll());
	}

	@GetMapping("/{clienteId}")
	public ResponseEntity<ClienteModel> buscar(@PathVariable Long clienteId) {
		authenticated(clienteId);
		Optional<Cliente> cliente = clienteRepository.findById(clienteId); 																	
		if (cliente.isPresent()) {
			ClienteModel clienteModel = toModel(cliente.get());
			return ResponseEntity.ok(clienteModel);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{clienteId}/enderecos") 
	public List<Endereco> listarEnderecos(@PathVariable Long clienteId){
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado"));;
		return cliente.getEnderecos();	
	}
		
	@GetMapping("/cpf/{clienteCpf}")
	public ResponseEntity<ClienteModel> buscarCpf(@PathVariable String clienteCpf) {
		Cliente cliente = clienteRepository.findByCpf(clienteCpf);
		if (cliente != null) {
			ClienteModel clienteModel = toModel(cliente);
			return ResponseEntity.ok(clienteModel);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/email")
	public ResponseEntity<ClienteModel> buscarEmail(@RequestParam(value="value") String clienteEmail) {
		authenticated(clienteEmail);
		Cliente cliente = clienteRepository.findByEmail(clienteEmail);
		if (cliente != null) {
			ClienteModel clienteModel = toModel(cliente);
			return ResponseEntity.ok(clienteModel);
		}
		return ResponseEntity.notFound().build();
	}

	//@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteModel adicionar(@Valid @RequestBody ClienteInput clienteInput) {																
		Cliente cliente = fromDTO(clienteInput);
		return toModel(cadastroCliente.salvar(cliente));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/{clienteId}")
	public ResponseEntity<ClienteModel> atualizar(@Valid @RequestBody ClienteDTO clienteDTO,
			@PathVariable Long clienteId) {
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		Cliente cliente = cadastroCliente.fromDTO(clienteDTO);
		cliente.setId(clienteId);
		cliente = cadastroCliente.update(cliente);
		ClienteModel clienteMdl = toModel(cliente);
		return ResponseEntity.ok(clienteMdl);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> remover(@PathVariable Long clienteId) {
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		try {
			cadastroCliente.excluir(clienteId);
			return ResponseEntity.noContent().build();
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Há ordens de serviço vinculadas a esse cliente");
		}

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/page")
	public ResponseEntity<Page<ClienteModel>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		Page<Cliente> listCliente = paginaCliente.findPage(page, linesPerPage, orderBy, direction);

		Page<ClienteModel> listClienteModel = listCliente.map(obj -> new ClienteModel(obj));

		return ResponseEntity.ok().body(listClienteModel);
	}

	private ClienteModel toModel(Cliente cliente) {
		return new ClienteModel(cliente);
	}

	private List<ClienteModel> toCollectionModel(List<Cliente> clientes) {
		return clientes.stream() // strem retorna um fluxo de elementos que suportam operacoes de
									// agregacao/tranforacao
				.map(cliente -> toModel(cliente)) // map vai aplicar uma função a cada elemento um a um do stream e
													// retornar um novo stream como resultado
				.collect(Collectors.toList()); // vai reduzir o stream anterior para uma coleção
	}

	private Cliente fromDTO(ClienteInput cliente) {
		Cliente cli = new Cliente(null, cliente.getNome(), cliente.getEmail(), cliente.getTelefone(), cliente.getCpf(),
				TipoCliente.toEnum(cliente.getTipo()), bc.encode(cliente.getSenha()));
		Cidade city = cidadeRepository.findById(cliente.getCidadeId()).orElse(null);

		Endereco end = new Endereco(null, cliente.getLogradouro(), cliente.getNumero(), cliente.getComplemento(),
				cliente.getBairro(), cliente.getCep(), cli, city);
		cli.getEnderecos().add(end);
		return cli;
	}

	private void authenticated(Long clienteId) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.TECNICO) && !clienteId.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado");
		}
	}
	private void authenticated(String clienteEmail) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.TECNICO) && !clienteEmail.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso Negado");
		}
	}
}