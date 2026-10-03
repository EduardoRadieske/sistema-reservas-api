package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import com.radieske.reservasapi.dto.ReservaDTO;
import com.radieske.reservasapi.dto.ReservaRequestDTO;
import com.radieske.reservasapi.model.Usuario;

public interface ReservaService
{
	ReservaDTO save(ReservaRequestDTO reserva);

	List<ReservaDTO> findAll();

	Optional<ReservaDTO> findById(Integer id);

	ReservaDTO update(ReservaRequestDTO reserva);

	void deleteById(Integer id);
	
	List<ReservaDTO> findActive(Usuario usuario);
}
