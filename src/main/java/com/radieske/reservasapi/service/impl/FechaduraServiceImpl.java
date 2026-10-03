package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.dto.FechaduraRequestDTO;
import com.radieske.reservasapi.dto.FechaduraResponseDTO;
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
	public FechaduraResponseDTO save(FechaduraRequestDTO request)
	{
		return FechaduraResponseDTO.fromEntity(fechaduraRepository.save(toEntity(request)));
	}

	@Override
	public List<FechaduraResponseDTO> findAll()
	{
		return fechaduraRepository.findAll().stream()
				.map(FechaduraResponseDTO::fromEntity)
				.toList();
	}

	@Override
	public Optional<FechaduraResponseDTO> findById(Integer id)
	{
		return fechaduraRepository.findById(id).map(FechaduraResponseDTO::fromEntity);
	}

	@Override
	public FechaduraResponseDTO update(FechaduraRequestDTO request)
	{
		return FechaduraResponseDTO.fromEntity(fechaduraRepository.save(toEntity(request)));
	}

	@Override
	public void deleteById(Integer id)
	{
		fechaduraRepository.deleteById(id);
	}

	private Fechadura toEntity(FechaduraRequestDTO request)
	{
		Integer idProvedor = request.resolvedIdProvedor();
		Provedor provedor = provedorRepository.findById(idProvedor)
				.orElseThrow(() -> new RuntimeException("Provedor " + idProvedor + " não encontrado"));

		Fechadura fechadura = new Fechadura();
		fechadura.setIdFechadura(request.idFechadura());
		fechadura.setChaveDispositivo(request.chaveDispositivo());
		fechadura.setProvedor(provedor);
		return fechadura;
	}
}
