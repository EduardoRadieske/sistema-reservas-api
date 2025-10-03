package com.radieske.reservasapi.controller;

import java.util.List;
import java.util.Optional;

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

import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.service.ReservaService;

@RestController
@RequestMapping("reservas")
public class ReservaController
{
	@Autowired
	private ReservaService reservaService;

	@GetMapping
	public ResponseEntity<List<Reserva>> findAll()
	{
		return ResponseEntity.status(HttpStatus.OK).body(reservaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Reserva>> findById(@PathVariable Long id)
	{
		return ResponseEntity.status(HttpStatus.OK).body(reservaService.findById(id));
	}

	@PostMapping
	public ResponseEntity<Reserva> create(@RequestBody Reserva usuario)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.save(usuario));
	}

	@PutMapping
	public ResponseEntity<Reserva> update(@RequestBody Reserva usuario)
	{
		return ResponseEntity.status(HttpStatus.OK).body(reservaService.update(usuario));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id)
	{
		reservaService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
