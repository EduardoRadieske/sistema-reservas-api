package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.repository.FechaduraRepository;
import com.radieske.reservasapi.service.FechaduraService;

@Service
public class FechaduraServiceImpl implements FechaduraService
{
	@Autowired
	private FechaduraRepository fechaduraRepository;

	@Override
	public Fechadura save(Fechadura fechadura)
	{
		return fechaduraRepository.save(fechadura);
	}

	@Override
	public List<Fechadura> findAll()
	{
		return fechaduraRepository.findAll();
	}

	@Override
	public Optional<Fechadura> findById(Long id)
	{
		return fechaduraRepository.findById(id);
	}

	@Override
	public Fechadura update(Fechadura fechadura)
	{
		return fechaduraRepository.save(fechadura);
	}

	@Override
	public void deleteById(Long id)
	{
		fechaduraRepository.deleteById(id);
	}
}
