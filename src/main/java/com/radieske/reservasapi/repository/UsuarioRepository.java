package com.radieske.reservasapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radieske.reservasapi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>
{
	Optional<Usuario> findByUsuario(String usuario);
}
