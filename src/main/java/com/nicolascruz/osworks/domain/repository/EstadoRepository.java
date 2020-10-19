package com.nicolascruz.osworks.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.nicolascruz.osworks.domain.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	
	@Transactional(readOnly=true)
	public List<Estado> findAllByOrderByNome();

}
