package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.dto.ProvedorRequestDTO;
import com.radieske.reservasapi.dto.ProvedorResponseDTO;

public interface ProvedorService
{
	ProvedorResponseDTO save(ProvedorRequestDTO provedor);

	List<ProvedorResponseDTO> findAll();

	Optional<ProvedorResponseDTO> findById(Integer id);

	ProvedorResponseDTO update(ProvedorRequestDTO provedor);

	void deleteById(Integer id);
}
