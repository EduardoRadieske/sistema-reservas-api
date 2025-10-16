package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.repository.ProvedorRepository;
import com.radieske.reservasapi.service.ProvedorService;

@Service
public class ProvedorServiceImpl implements ProvedorService
{
	@Autowired
	private ProvedorRepository provedorRepository;

	@Override
	public Provedor save(Provedor provedor)
	{
		return provedorRepository.save(provedor);
	}

	@Override
	public List<Provedor> findAll()
	{
		return provedorRepository.findAll();
	}

	@Override
	public Optional<Provedor> findById(Integer id)
	{
		return provedorRepository.findById(id);
	}

	@Override
	public Provedor update(Provedor provedor)
	{
		return provedorRepository.save(provedor);
	}

	@Override
	public void deleteById(Integer id)
	{
		provedorRepository.deleteById(id);
	}
}
