package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.repository.FechaduraRepository;
import com.radieske.reservasapi.repository.ProvedorRepository;
import com.radieske.reservasapi.service.FechaduraService;

@Service
public class FechaduraServiceImpl implements FechaduraService
{
	@Autowired
	private FechaduraRepository fechaduraRepository;
	
	@Autowired
	private ProvedorRepository provedorRepository;

	@Override
	public Fechadura save(Fechadura fechadura)
	{
		Provedor provedor = provedorRepository.findById(fechadura.getProvedor().getIdProvedor())
	            .orElseThrow(() -> new RuntimeException("Provedor " + fechadura.getProvedor().getIdProvedor() + " não encontrado"));
		fechadura.setProvedor(provedor);
		
		return fechaduraRepository.save(fechadura);
	}

	@Override
	public List<Fechadura> findAll()
	{
		return fechaduraRepository.findAll();
	}

	@Override
	public Optional<Fechadura> findById(Integer id)
	{
		return fechaduraRepository.findById(id);
	}

	@Override
	public Fechadura update(Fechadura fechadura)
	{
		return fechaduraRepository.save(fechadura);
	}

	@Override
	public void deleteById(Integer id)
	{
		fechaduraRepository.deleteById(id);
	}
}
