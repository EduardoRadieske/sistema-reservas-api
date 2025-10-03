package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService
{
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Usuario save(Usuario usuario)
	{
		return usuarioRepository.save(usuario);
	}

	@Override
	public List<Usuario> findAll()
	{
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Usuario> findById(Long id)
	{
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario update(Usuario usuario)
	{
		return usuarioRepository.save(usuario);
	}

	@Override
	public void deleteById(Long id)
	{
		usuarioRepository.deleteById(id);
	}
}
