package com.radieske.reservasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radieske.reservasapi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{

}
