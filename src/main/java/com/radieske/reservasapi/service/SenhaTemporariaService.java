package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.SenhaTemporaria;

public interface SenhaTemporariaService
{
	SenhaTemporaria save(SenhaTemporaria senha);

	List<SenhaTemporaria> findAll();

	Optional<SenhaTemporaria> findById(Long id);

	SenhaTemporaria update(SenhaTemporaria senha);

	void deleteById(Long id);
	
	SenhaTemporaria generatePasswordForReservation(Reserva reserva);
}
