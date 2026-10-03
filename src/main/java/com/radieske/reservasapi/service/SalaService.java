package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.dto.SalaDTO;
import com.radieske.reservasapi.dto.SalaRequestDTO;

public interface SalaService
{
	SalaDTO save(SalaRequestDTO sala);

	List<SalaDTO> findAll();

	Optional<SalaDTO> findById(Integer id);

	SalaDTO update(SalaRequestDTO sala);

	void deleteById(Integer id);
}
