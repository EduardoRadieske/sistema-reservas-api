package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.dto.UsuarioRequestDTO;
import com.radieske.reservasapi.dto.UsuarioResponseDTO;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService
{
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UsuarioResponseDTO save(UsuarioRequestDTO dto)
	{
		Usuario usuario = new Usuario();
		usuario.setNome(dto.nome());
		usuario.setUsuario(dto.usuario());
		usuario.setTipo(dto.resolvedTipo());

		if (dto.senha() != null && !dto.senha().isBlank())
		{
			if (dto.senha().startsWith("$2a$") || dto.senha().startsWith("$2b$") || dto.senha().startsWith("$2y$"))
			{
				usuario.setSenhaHash(dto.senha());
			}
			else
			{
				usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));
			}
		}

		return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
	}

	@Override
	public Usuario save(Usuario usuario)
	{
		return usuarioRepository.save(usuario);
	}

	@Override
	public List<UsuarioResponseDTO> findAll()
	{
		return usuarioRepository.findAll().stream()
				.map(UsuarioResponseDTO::fromEntity)
				.toList();
	}

	@Override
	public Optional<UsuarioResponseDTO> findById(Integer id)
	{
		return usuarioRepository.findById(id).map(UsuarioResponseDTO::fromEntity);
	}

	@Override
	public UsuarioResponseDTO update(UsuarioRequestDTO dto)
	{
		Usuario usuario;
		if (dto.idUsuario() != null)
		{
			usuario = usuarioRepository.findById(dto.idUsuario()).orElse(new Usuario());
			usuario.setIdUsuario(dto.idUsuario());
		}
		else
		{
			usuario = new Usuario();
		}

		usuario.setNome(dto.nome());
		usuario.setUsuario(dto.usuario());
		if (dto.tipo() != null)
		{
			usuario.setTipo(dto.tipo());
		}

		if (dto.senha() != null && !dto.senha().isBlank())
		{
			if (dto.senha().startsWith("$2a$") || dto.senha().startsWith("$2b$") || dto.senha().startsWith("$2y$"))
			{
				usuario.setSenhaHash(dto.senha());
			}
			else
			{
				usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));
			}
		}

		return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
	}

	@Override
	public Usuario update(Usuario usuario)
	{
		return usuarioRepository.save(usuario);
	}

	@Override
	public void deleteById(Integer id)
	{
		usuarioRepository.deleteById(id);
	}

	@Override
	public Usuario findByUsuario(String usuario)
	{
		return usuarioRepository.findByUsuario(usuario)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
	}
}
