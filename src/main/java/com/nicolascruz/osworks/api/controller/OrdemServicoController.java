package com.nicolascruz.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nicolascruz.osworks.api.model.ClienteResumoModel;
import com.nicolascruz.osworks.api.model.OrdemServicoInput;
import com.nicolascruz.osworks.api.model.OrdemServicoModel;
import com.nicolascruz.osworks.domain.model.Cliente;
import com.nicolascruz.osworks.domain.model.Endereco;
import com.nicolascruz.osworks.domain.model.OrdemServico;
import com.nicolascruz.osworks.domain.repository.ClienteRepository;
import com.nicolascruz.osworks.domain.repository.EnderecoRepository;
import com.nicolascruz.osworks.domain.repository.OrdemServicoRepository;
import com.nicolascruz.osworks.domain.service.GestaoOrdemServicoService;
import com.nicolascruz.osworks.domain.service.exceptions.DataIntegrityException;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {
	
	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServico;
	
	@Autowired
	private OrdemServicoRepository osRepositorio;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@PostMapping
	/*@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoModel criar(@Valid @RequestBody OrdemServico ordemServico) {
		return toModel(gestaoOrdemServico.criar(ordemServico));
	}*/
	
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoModel criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		
		//converte a ordemInput numa entidade ordemServico
		OrdemServico ordemServico = toEntity(ordemServicoInput);
		return toModel(gestaoOrdemServico.criar(ordemServico));
	}
	
	@GetMapping
	public List<OrdemServicoModel> listar(){
		return toCollectionModel(osRepositorio.findAll());
	}
	
	@GetMapping("/{ordemServicoId}")
	public ResponseEntity<OrdemServicoModel> buscar(@PathVariable Long ordemServicoId){
		Optional<OrdemServico> ordemServico = osRepositorio.findById(ordemServicoId);
		
		if (ordemServico.isPresent()){
			
			//não quero retornar a ordem de serviço completa, quero retornar sua classe de represenção apenas
			//isso isola ainda mais as camadas do sistema, pois posso fazer alteracoes na classe de entidade
			//que a integridade da classe de representacao sera mantida
			
			/*para nao ter que instanciar um OrdemServicoModel e ficar setando seus atributos manualmente
			 * usaremos a biblioteca modelmapper, que mapeia entidades para DTO 
			 * (padrao de tranferencia de dados entre as camadas do projeto)
			 */
			OrdemServicoModel ordemServicoModel = toModel(ordemServico.get());
			return ResponseEntity.ok(ordemServicoModel);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/page")
	public ResponseEntity<Page<OrdemServicoModel>> findPage(
			// Atributos opcionais na rewuisição, com valores padrões caso não sejam
			// informados
			@RequestParam(value = "clienteId", defaultValue = "") Long clienteId,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) {
		
		Page<OrdemServico> list = gestaoOrdemServico.search(clienteId, page, linesPerPage, orderBy, direction);

		Page<OrdemServicoModel> listOsModel = list.map(obj -> new OrdemServicoModel(obj));

		return ResponseEntity.ok().body(listOsModel);
	}
	
	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT) //tipo 200 ok, mas nao tem corpo
	public void finalizar(@PathVariable Long ordemServicoId) {
		gestaoOrdemServico.finalizar(ordemServicoId);
	}
	
	@PutMapping("/{ordemServicoId}/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long ordemServicoId) {
		gestaoOrdemServico.cancelar(ordemServicoId);
	}
	//converte 1 ordem de servico em 1 ordem de servico model
	private OrdemServicoModel toModel(OrdemServico ordemServico) {
		//return modelMapper.map(ordemServico, OrdemServicoModel.class);
		ClienteResumoModel cli = new ClienteResumoModel();
		cli.setId(ordemServico.getCliente().getId());
		cli.setNome(ordemServico.getCliente().getNome());
		OrdemServicoModel model = new OrdemServicoModel(ordemServico);
		
		return model;
	}
	
	//converte uma lista de ordem de servico em uma lista de ordem de servico model
	private List<OrdemServicoModel> toCollectionModel(List<OrdemServico> ordensServico){
		return ordensServico.stream()        //strem retorna um fluxo de elementos que suportam operacoes de agregacao/tranforacao
				.map(ordemServico -> toModel(ordemServico)) //map vai aplicar uma função a cada elemento um a um do stream e retornar um novo stream como resultado
				.collect(Collectors.toList()); //vai reduzir o stream anterior para uma coleção
	}
	//converte uma ordem de servico input em uma ordem de serviço
	private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
		
		OrdemServico ordemServico = new OrdemServico();
		ordemServico.setDescricao(ordemServicoInput.getDescricao());
		ordemServico.setPreco(ordemServicoInput.getPreco());
		
		Cliente cli = clienteRepository.findById(ordemServicoInput.getCliente().getId())
				.orElseThrow(() -> new DataIntegrityException("Cliente não encontrado"));
		
		Endereco end = enderecoRepository.findById(ordemServicoInput.getEndereco().getId())
				.orElseThrow(() -> new DataIntegrityException("Endereço não encontrado"));
		
		ordemServico.setEndereco(end);
		ordemServico.setPagamento(ordemServicoInput.getPagamento());
		ordemServico.setCliente(cli);
		
		return ordemServico;
	}
	
}