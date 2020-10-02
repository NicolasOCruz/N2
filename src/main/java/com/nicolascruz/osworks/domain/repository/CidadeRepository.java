package com.nicolascruz.osworks.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicolascruz.osworks.domain.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

}
