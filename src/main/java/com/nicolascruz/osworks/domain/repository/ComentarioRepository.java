package com.nicolascruz.osworks.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicolascruz.osworks.domain.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

}
