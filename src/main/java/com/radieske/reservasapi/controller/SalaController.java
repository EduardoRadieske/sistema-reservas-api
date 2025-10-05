package com.radieske.reservasapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radieske.reservasapi.model.Sala;
import com.radieske.reservasapi.service.SalaService;

@RestController
@RequestMapping("salas")
@PreAuthorize("hasRole('admin')")
public class SalaController
{
	@Autowired
	private SalaService salaService;

	@GetMapping
	public ResponseEntity<List<Sala>> findAll()
	{
		return ResponseEntity.status(HttpStatus.OK).body(salaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Sala>> findById(@PathVariable Long id)
	{
		return ResponseEntity.status(HttpStatus.OK).body(salaService.findById(id));
	}

	@PostMapping
	public ResponseEntity<Sala> create(@RequestBody Sala usuario)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(salaService.save(usuario));
	}

	@PutMapping
	public ResponseEntity<Sala> update(@RequestBody Sala usuario)
	{
		return ResponseEntity.status(HttpStatus.OK).body(salaService.update(usuario));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id)
	{
		salaService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
