package com.radieske.reservasapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.dto.SenhaTempDTO;
import com.radieske.reservasapi.integration.ProcessIntegration;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.SenhaTemporaria;
import com.radieske.reservasapi.repository.SenhaTemporariaRepository;
import com.radieske.reservasapi.service.SenhaTemporariaService;
import com.radieske.reservasapi.util.PasswordGenerator;

@Service
public class SenhaTemporariaServiceImpl implements SenhaTemporariaService
{
	@Autowired
	private SenhaTemporariaRepository senhaRepository;

	@Override
	public SenhaTempDTO save(SenhaTemporaria reserva)
	{
		return SenhaTempDTO.fromEntity(senhaRepository.save(reserva));
	}

	@Override
	public List<SenhaTempDTO> findAll()
	{
		return senhaRepository.findAll()
				.stream()
				.map(SenhaTempDTO::fromEntity).toList();
	}

	@Override
	public SenhaTempDTO findByIdReserva(Integer id)
	{
		SenhaTemporaria senha = senhaRepository.findByIdReserva(id)
				.orElseThrow(() -> new RuntimeException("Reserva não encontrada."));
		
		return SenhaTempDTO.fromEntity(senha);
	}

	@Override
	public SenhaTempDTO update(SenhaTemporaria reserva)
	{
		return SenhaTempDTO.fromEntity(senhaRepository.save(reserva));
	}

	@Override
	public void deleteById(Long id)
	{
		senhaRepository.deleteById(id);
	}

	@Override
	public SenhaTemporaria generatePasswordForReservation(Reserva reserva)
	{
		// Gera PIN aleatório de 6 dígitos
		String pin = PasswordGenerator.generateRandomDigits();

		SenhaTemporaria senha = new SenhaTemporaria();
		senha.setReserva(reserva);
		senha.setCodigo(pin);

		senha = senhaRepository.save(senha);

		// Disparar o fluxo de automação da APIs
		ProcessIntegration.processAutomation(senha);

		return senha;
	}
}
