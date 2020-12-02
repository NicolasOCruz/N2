package com.nicolascruz.osworks.domain.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nicolascruz.osworks.domain.model.OrdemServico;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long>{
	
	@Transactional(readOnly=true) //reduz o locking
	Page<OrdemServico> findByClienteId(Long id, Pageable pageRequest);
	
}