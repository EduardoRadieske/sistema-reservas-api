package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.model.Fechadura;

public interface FechaduraService
{
	Fechadura save(Fechadura fechadura);

	List<Fechadura> findAll();

	Optional<Fechadura> findById(Integer id);

	Fechadura update(Fechadura fechadura);

	void deleteById(Integer id);
}
