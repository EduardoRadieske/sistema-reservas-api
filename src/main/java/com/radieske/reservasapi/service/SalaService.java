package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.dto.SalaDTO;
import com.radieske.reservasapi.model.Sala;

public interface SalaService
{
	SalaDTO save(Sala sala);

	List<SalaDTO> findAll();

	Optional<SalaDTO> findById(Integer id);

	SalaDTO update(Sala sala);

	void deleteById(Integer id);
}
