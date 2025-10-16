package com.radieske.reservasapi.service;

import java.util.List;

import com.radieske.reservasapi.dto.SenhaTempDTO;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.SenhaTemporaria;

public interface SenhaTemporariaService
{
	SenhaTempDTO save(SenhaTemporaria senha);

	List<SenhaTempDTO> findAll();

	SenhaTempDTO findByIdReserva(Integer id);

	SenhaTempDTO update(SenhaTemporaria senha);

	void deleteById(Long id);
	
	SenhaTemporaria generatePasswordForReservation(Reserva reserva);
}
