package com.radieske.reservasapi.controller;

import java.util.List;

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

import com.radieske.reservasapi.dto.SalaDTO;
import com.radieske.reservasapi.dto.SalaRequestDTO;
import com.radieske.reservasapi.service.SalaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("salas")
public class SalaController
{
	@Autowired
	private SalaService salaService;

	@GetMapping
	public ResponseEntity<List<SalaDTO>> findAll()
	{
		return ResponseEntity.status(HttpStatus.OK).body(salaService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SalaDTO> findById(@PathVariable Integer id)
	{
		return ResponseEntity.of(salaService.findById(id));
	}

	@PostMapping
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<SalaDTO> create(@Valid @RequestBody SalaRequestDTO request)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(salaService.save(request));
	}

	@PutMapping
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<SalaDTO> update(@Valid @RequestBody SalaRequestDTO request)
	{
		return ResponseEntity.status(HttpStatus.OK).body(salaService.update(request));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<?> delete(@PathVariable Integer id)
	{
		salaService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
