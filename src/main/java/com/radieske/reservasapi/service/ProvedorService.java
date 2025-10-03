package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.model.Provedor;

public interface ProvedorService
{
	Provedor save(Provedor provedor);

	List<Provedor> findAll();

	Optional<Provedor> findById(Long id);

	Provedor update(Provedor provedor);

	void deleteById(Long id);
}
