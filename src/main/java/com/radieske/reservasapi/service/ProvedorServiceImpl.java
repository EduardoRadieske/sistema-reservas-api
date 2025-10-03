package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.repository.ProvedorRepository;

@Service
public class ProvedorServiceImpl implements ProvedorService
{
	@Autowired
	private ProvedorRepository usuarioRepository;

	@Override
	public Provedor save(Provedor provedor)
	{
		return usuarioRepository.save(provedor);
	}

	@Override
	public List<Provedor> findAll()
	{
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Provedor> findById(Long id)
	{
		return usuarioRepository.findById(id);
	}

	@Override
	public Provedor update(Provedor provedor)
	{
		return usuarioRepository.save(provedor);
	}

	@Override
	public void deleteById(Long id)
	{
		usuarioRepository.deleteById(id);
	}
}
