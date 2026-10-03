package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.dto.FechaduraRequestDTO;
import com.radieske.reservasapi.dto.FechaduraResponseDTO;

public interface FechaduraService
{
	FechaduraResponseDTO save(FechaduraRequestDTO fechadura);

	List<FechaduraResponseDTO> findAll();

	Optional<FechaduraResponseDTO> findById(Integer id);

	FechaduraResponseDTO update(FechaduraRequestDTO fechadura);

	void deleteById(Integer id);
}
