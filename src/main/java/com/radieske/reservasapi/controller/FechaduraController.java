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

import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.service.FechaduraService;

@RestController
@RequestMapping("fechaduras")
@PreAuthorize("hasRole('admin')")
public class FechaduraController
{
	@Autowired
	private FechaduraService fechaduraService;
	
	@GetMapping
	public ResponseEntity<List<Fechadura>> findAll()
	{
		return ResponseEntity.status(HttpStatus.OK).body(fechaduraService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Fechadura>> findById(@PathVariable Long id)
	{
		return ResponseEntity.status(HttpStatus.OK).body(fechaduraService.findById(id));
	}

	@PostMapping
	public ResponseEntity<Fechadura> create(@RequestBody Fechadura usuario)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(fechaduraService.save(usuario));
	}

	@PutMapping
	public ResponseEntity<Fechadura> update(@RequestBody Fechadura usuario)
	{
		return ResponseEntity.status(HttpStatus.OK).body(fechaduraService.update(usuario));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id)
	{
		fechaduraService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
