package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.model.Sala;

public interface SalaService
{
	Sala save(Sala sala);

	List<Sala> findAll();

	Optional<Sala> findById(Long id);

	Sala update(Sala sala);

	void deleteById(Long id);
}
