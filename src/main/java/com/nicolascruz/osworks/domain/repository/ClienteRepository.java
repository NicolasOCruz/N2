package com.nicolascruz.osworks.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nicolascruz.osworks.domain.model.Cliente;

@Repository
//define que essa interface é um componente do Spring
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	//Spring Data JPA já herda os métodos padrão do JPA
	
	//Usando query method para criar as próprias consultas
	List<Cliente> findByNome(String nome);
	//Usando containing o Spring já entende que é um LIKE
	List<Cliente> findByNomeContaining(String nome);
	
	@Transactional(readOnly=true)
	Cliente findByEmail(String email);

	Cliente findByCpf(String cpf);
	
	boolean existsByCpf(String cpf);
}
