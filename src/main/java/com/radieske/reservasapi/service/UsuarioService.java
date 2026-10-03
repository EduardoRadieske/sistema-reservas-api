package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.dto.UsuarioRequestDTO;
import com.radieske.reservasapi.dto.UsuarioResponseDTO;
import com.radieske.reservasapi.model.Usuario;

public interface UsuarioService
{
	UsuarioResponseDTO save(UsuarioRequestDTO usuario);

	Usuario save(Usuario usuario);

	List<UsuarioResponseDTO> findAll();

	Optional<UsuarioResponseDTO> findById(Integer id);

	UsuarioResponseDTO update(UsuarioRequestDTO usuario);

	Usuario update(Usuario usuario);

	void deleteById(Integer id);
	
	Usuario findByUsuario(String usuario);
}
