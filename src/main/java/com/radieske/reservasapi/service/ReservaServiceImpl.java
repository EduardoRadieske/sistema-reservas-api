package com.radieske.reservasapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.repository.ReservaRepository;

@Service
public class ReservaServiceImpl implements ReservaService
{
	@Autowired
	private ReservaRepository usuarioRepository;

	@Override
	public Reserva save(Reserva reserva)
	{
		return usuarioRepository.save(reserva);
	}

	@Override
	public List<Reserva> findAll()
	{
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Reserva> findById(Long id)
	{
		return usuarioRepository.findById(id);
	}

	@Override
	public Reserva update(Reserva reserva)
	{
		return usuarioRepository.save(reserva);
	}

	@Override
	public void deleteById(Long id)
	{
		usuarioRepository.deleteById(id);
	}
}
