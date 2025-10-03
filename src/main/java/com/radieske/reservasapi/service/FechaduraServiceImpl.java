package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.repository.FechaduraRepository;

@Service
public class FechaduraServiceImpl implements FechaduraService
{
	@Autowired
	private FechaduraRepository usuarioRepository;

	@Override
	public Fechadura save(Fechadura fechadura)
	{
		return usuarioRepository.save(fechadura);
	}

	@Override
	public List<Fechadura> findAll()
	{
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Fechadura> findById(Long id)
	{
		return usuarioRepository.findById(id);
	}

	@Override
	public Fechadura update(Fechadura fechadura)
	{
		return usuarioRepository.save(fechadura);
	}

	@Override
	public void deleteById(Long id)
	{
		usuarioRepository.deleteById(id);
	}
}
