package com.radieske.reservasapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.dto.ReservaDTO;
import com.radieske.reservasapi.dto.ReservaRequestDTO;
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
	public ReservaDTO save(ReservaRequestDTO request)
	{
		Reserva reserva = toEntity(request);
		validateReserva(reserva);
		
		Reserva novaReserva = reservaRepository.save(reserva);
        
        // Gera senha automática vinculada à reserva
		senhaService.generatePasswordForReservation(novaReserva);
        
		return ReservaDTO.fromEntity(novaReserva);
	}
	
	private void validateReserva(Reserva reserva)
	{
		resolveReferences(reserva);
		Optional<Reserva> tempReserva = reservaRepository.findByBetweenDate(
				reserva.getSala().getIdSala(), reserva.getDataReservaInicial(), reserva.getDataReservaFinal());

		if (tempReserva.isPresent())
		{
			throw new RuntimeException("Já existe uma reserva para este horário!");
		}
	}

	private void resolveReferences(Reserva reserva)
	{
		Integer idUsuario = reserva.getUsuario().getIdUsuario();
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new RuntimeException("Usuário " + idUsuario + " não encontrado"));

		Integer idSala = reserva.getSala().getIdSala();
		Sala sala = salaRepository.findById(idSala)
				.orElseThrow(() -> new RuntimeException("Sala " + idSala + " não encontrada"));

		reserva.setUsuario(usuario);
		reserva.setSala(sala);
	}

	private Reserva toEntity(ReservaRequestDTO request)
	{
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(request.resolvedIdUsuario());
		Sala sala = new Sala();
		sala.setIdSala(request.resolvedIdSala());

		Reserva reserva = new Reserva();
		reserva.setIdReserva(request.idReserva());
		reserva.setUsuario(usuario);
		reserva.setSala(sala);
		reserva.setDataReservaInicial(request.dataReservaInicial());
		reserva.setDataReservaFinal(request.dataReservaFinal());
		reserva.setStatus(request.resolvedStatus());
		return reserva;
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
	public ReservaDTO update(ReservaRequestDTO request)
	{
		Reserva reserva = toEntity(request);
		resolveReferences(reserva);
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
}
