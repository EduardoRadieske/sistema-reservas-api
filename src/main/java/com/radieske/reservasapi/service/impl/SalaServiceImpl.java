package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.dto.SalaDTO;
import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.model.Sala;
import com.radieske.reservasapi.repository.FechaduraRepository;
import com.radieske.reservasapi.repository.SalaRepository;
import com.radieske.reservasapi.service.SalaService;

@Service
public class SalaServiceImpl implements SalaService
{
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private FechaduraRepository fechaduraRepository;

	@Override
	public SalaDTO save(Sala sala)
	{
		Fechadura fechadura = fechaduraRepository.findById(sala.getFechadura().getIdFechadura())
	            .orElseThrow(() -> new RuntimeException("Fechadura " + sala.getFechadura().getIdFechadura() + " não encontrada"));
		sala.setFechadura(fechadura);
		
		return SalaDTO.fromEntity(salaRepository.save(sala));
	}

	@Override
	public List<SalaDTO> findAll()
	{
		return salaRepository.findAll()
				.stream()
				.map(SalaDTO::fromEntity)
				.toList();
	}

	@Override
	public Optional<SalaDTO> findById(Integer id)
	{
		return Optional.of(SalaDTO.fromEntity(salaRepository.findById(id).get()));
	}

	@Override
	public SalaDTO update(Sala sala)
	{
		return SalaDTO.fromEntity(salaRepository.save(sala));
	}

	@Override
	public void deleteById(Integer id)
	{
		salaRepository.deleteById(id);
	}
}
