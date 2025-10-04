package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.repository.ReservaRepository;
import com.radieske.reservasapi.service.ReservaService;

@Service
public class ReservaServiceImpl implements ReservaService
{
	@Autowired
	private ReservaRepository reservaRepository;
	
	@Autowired
	private SenhaTemporariaServiceImpl senhaService;

	@Override
	public Reserva save(Reserva reserva)
	{
		Reserva novaReserva = reservaRepository.save(reserva);
        
        // Gera senha automática vinculada à reserva
		senhaService.generatePasswordForReservation(novaReserva);
        
		return novaReserva;
	}

	@Override
	public List<Reserva> findAll()
	{
		return reservaRepository.findAll();
	}

	@Override
	public Optional<Reserva> findById(Long id)
	{
		return reservaRepository.findById(id);
	}

	@Override
	public Reserva update(Reserva reserva)
	{
		return reservaRepository.save(reserva);
	}

	@Override
	public void deleteById(Long id)
	{
		reservaRepository.deleteById(id);
	}
}
