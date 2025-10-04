package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Sala;
import com.radieske.reservasapi.repository.SalaRepository;
import com.radieske.reservasapi.service.SalaService;

@Service
public class SalaServiceImpl implements SalaService
{
	@Autowired
	private SalaRepository salaRepository;

	@Override
	public Sala save(Sala sala)
	{
		return salaRepository.save(sala);
	}

	@Override
	public List<Sala> findAll()
	{
		return salaRepository.findAll();
	}

	@Override
	public Optional<Sala> findById(Long id)
	{
		return salaRepository.findById(id);
	}

	@Override
	public Sala update(Sala sala)
	{
		return salaRepository.save(sala);
	}

	@Override
	public void deleteById(Long id)
	{
		salaRepository.deleteById(id);
	}
}
