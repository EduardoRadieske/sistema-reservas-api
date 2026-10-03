package com.radieske.reservasapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radieske.reservasapi.dto.ReservaDTO;
import com.radieske.reservasapi.dto.ReservaRequestDTO;
import com.radieske.reservasapi.dto.SenhaTempDTO;
import com.radieske.reservasapi.service.AuthenticatedUserService;
import com.radieske.reservasapi.service.ReservaService;
import com.radieske.reservasapi.service.SenhaTemporariaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("reservas")
public class ReservaController
{
	@Autowired
	private ReservaService reservaService;
	
	@Autowired
	private SenhaTemporariaService senhaService;
	
	@Autowired
	private AuthenticatedUserService authUserService;

	@GetMapping
	public ResponseEntity<List<ReservaDTO>> findActive()
	{	
		return ResponseEntity.status(HttpStatus.OK)
				.body(reservaService.findActive(authUserService.getAuthenticatedUser()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReservaDTO> findById(@PathVariable Integer id)
	{
		return ResponseEntity.of(reservaService.findById(id));
	}

	@PostMapping
	public ResponseEntity<ReservaDTO> create(@Valid @RequestBody ReservaRequestDTO request)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.save(request));
	}

	@PutMapping
	public ResponseEntity<ReservaDTO> update(@Valid @RequestBody ReservaRequestDTO request)
	{
		return ResponseEntity.status(HttpStatus.OK).body(reservaService.update(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id)
	{
		reservaService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/{id}/senha")
	public ResponseEntity<SenhaTempDTO> findPasswordById(@PathVariable Integer id)
	{	
		return ResponseEntity.status(HttpStatus.OK).body(senhaService.findByIdReserva(id));
	}
}
