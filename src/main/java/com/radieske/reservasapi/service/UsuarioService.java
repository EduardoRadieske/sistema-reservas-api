package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.model.Usuario;

public interface UsuarioService
{
	Usuario save(Usuario usuario);

	List<Usuario> findAll();

	Optional<Usuario> findById(Long id);

	Usuario update(Usuario usuario);

	void deleteById(Long id);

}
