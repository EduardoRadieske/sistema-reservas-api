package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Sala;
import com.radieske.reservasapi.repository.SalaRepository;

@Service
public class SalaServiceImpl implements SalaService
{
	@Autowired
	private SalaRepository usuarioRepository;

	@Override
	public Sala save(Sala sala)
	{
		return usuarioRepository.save(sala);
	}

	@Override
	public List<Sala> findAll()
	{
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Sala> findById(Long id)
	{
		return usuarioRepository.findById(id);
	}

	@Override
	public Sala update(Sala sala)
	{
		return usuarioRepository.save(sala);
	}

	@Override
	public void deleteById(Long id)
	{
		usuarioRepository.deleteById(id);
	}
}
