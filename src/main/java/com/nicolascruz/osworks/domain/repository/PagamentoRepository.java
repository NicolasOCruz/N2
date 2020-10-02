package com.nicolascruz.osworks.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicolascruz.osworks.domain.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
