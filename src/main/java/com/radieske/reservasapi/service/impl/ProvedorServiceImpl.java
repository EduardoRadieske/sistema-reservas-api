package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.dto.ProvedorRequestDTO;
import com.radieske.reservasapi.dto.ProvedorResponseDTO;
import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.repository.ProvedorRepository;
import com.radieske.reservasapi.service.ProvedorService;

@Service
public class ProvedorServiceImpl implements ProvedorService
{
	@Autowired
	private ProvedorRepository provedorRepository;

	@Override
	public ProvedorResponseDTO save(ProvedorRequestDTO provedor)
	{
		return ProvedorResponseDTO.fromEntity(provedorRepository.save(toEntity(provedor)));
	}

	@Override
	public List<ProvedorResponseDTO> findAll()
	{
		return provedorRepository.findAll().stream()
				.map(ProvedorResponseDTO::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ProvedorResponseDTO> findById(Integer id)
	{
		return provedorRepository.findById(id).map(ProvedorResponseDTO::fromEntity);
	}

	@Override
	public ProvedorResponseDTO update(ProvedorRequestDTO provedor)
	{
		return ProvedorResponseDTO.fromEntity(provedorRepository.save(toEntity(provedor)));
	}

	@Override
	public void deleteById(Integer id)
	{
		provedorRepository.deleteById(id);
	}

	private Provedor toEntity(ProvedorRequestDTO request)
	{
		Provedor provedor = new Provedor();
		provedor.setIdProvedor(request.idProvedor());
		provedor.setProvedor(request.provedor());
		provedor.setStatus(request.resolvedStatus());
		provedor.setClientId(request.clientId());
		provedor.setSecret(request.secret());
		return provedor;
	}
}
