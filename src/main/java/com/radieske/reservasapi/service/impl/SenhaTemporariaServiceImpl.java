package com.radieske.reservasapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public SenhaTemporaria save(SenhaTemporaria reserva)
	{
		return senhaRepository.save(reserva);
	}

	@Override
	public List<SenhaTemporaria> findAll()
	{
		return senhaRepository.findAll();
	}

	@Override
	public Optional<SenhaTemporaria> findById(Long id)
	{
		return senhaRepository.findById(id);
	}

	@Override
	public SenhaTemporaria update(SenhaTemporaria reserva)
	{
		return senhaRepository.save(reserva);
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
