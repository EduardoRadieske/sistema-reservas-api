package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.model.Reserva;

public interface ReservaService
{
	Reserva save(Reserva reserva);

	List<Reserva> findAll();

	Optional<Reserva> findById(Long id);

	Reserva update(Reserva reserva);

	void deleteById(Long id);
}
