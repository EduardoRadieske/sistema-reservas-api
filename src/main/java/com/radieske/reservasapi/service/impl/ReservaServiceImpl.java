package com.radieske.reservasapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.dto.ReservaDTO;
import com.radieske.reservasapi.enums.Status;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.Sala;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.ReservaRepository;
import com.radieske.reservasapi.repository.SalaRepository;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.service.ReservaService;
import com.radieske.reservasapi.service.SenhaTemporariaService;

@Service
public class ReservaServiceImpl implements ReservaService
{
	@Autowired
	private ReservaRepository reservaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private SalaRepository salaRepository;
	
	@Autowired
	private SenhaTemporariaService senhaService;

	@Override
	public ReservaDTO save(Reserva reserva)
	{
		validateReserva(reserva);
		
		Reserva novaReserva = reservaRepository.save(reserva);
        
        // Gera senha automática vinculada à reserva
		senhaService.generatePasswordForReservation(novaReserva);
        
		return ReservaDTO.fromEntity(novaReserva);
	}

	@Override
	public List<ReservaDTO> findAll()
	{
		return reservaRepository.findAll()
				.stream()
			    .map(ReservaDTO::fromEntity)
			    .toList();
	}

	@Override
	public Optional<ReservaDTO> findById(Integer id)
	{
		Optional<ReservaDTO> retorno = Optional.empty();
		Optional<Reserva> reserva = reservaRepository.findById(id);
		
		if (reserva.isPresent())
		{
			retorno = Optional.of(ReservaDTO.fromEntity(reserva.get()));
		}
		
		return retorno;
	}

	@Override
	public ReservaDTO update(Reserva reserva)
	{
		return ReservaDTO.fromEntity(reservaRepository.save(reserva));
	}

	@Override
	public void deleteById(Integer id)
	{
		reservaRepository.deleteById(id);
	}
	
	@Override
	public List<ReservaDTO> findActive(Usuario usuario)
	{    
		List<ReservaDTO> listaReservas = new ArrayList<>();
		
		Optional<List<Reserva>> reservas = reservaRepository.findByStatusAndUsuario(Status.S, usuario);
		
		if (reservas.isPresent())
		{
			for (Reserva reserva : reservas.get())
			{
				listaReservas.add(ReservaDTO.fromEntity(reserva));
			}
		}
		
		return listaReservas;
	}
	
	private void validateReserva(Reserva reserva) 
	{
	    Usuario usuario = usuarioRepository.findById(reserva.getUsuario().getIdUsuario())
	            .orElseThrow(() -> new RuntimeException("Usuário " + reserva.getUsuario().getIdUsuario() + " não encontrado"));
	    
	    Sala sala = salaRepository.findById(reserva.getSala().getIdSala())
	            .orElseThrow(() -> new RuntimeException("Sala " + reserva.getSala().getIdSala() + " não encontrada"));

	    reserva.setUsuario(usuario);
	    reserva.setSala(sala);
	    
		Optional<Reserva> tempReserva = reservaRepository.findByBetweenDate(reserva.getSala().getIdSala(), reserva.getDataReservaInicial(), reserva.getDataReservaFinal());
		
		if (tempReserva.isPresent())
		{
			throw new RuntimeException("Já existe uma reserva para este horário!");
		}
	}
}
