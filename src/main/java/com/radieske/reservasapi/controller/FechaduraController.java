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

import com.radieske.reservasapi.dto.FechaduraRequestDTO;
import com.radieske.reservasapi.dto.FechaduraResponseDTO;
import com.radieske.reservasapi.service.FechaduraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("fechaduras")
@PreAuthorize("hasRole('admin')")
public class FechaduraController
{
	@Autowired
	private FechaduraService fechaduraService;
	
	@GetMapping
	public ResponseEntity<List<FechaduraResponseDTO>> findAll()
	{
		return ResponseEntity.status(HttpStatus.OK).body(fechaduraService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<FechaduraResponseDTO> findById(@PathVariable Integer id)
	{
		return ResponseEntity.of(fechaduraService.findById(id));
	}

	@PostMapping
	public ResponseEntity<FechaduraResponseDTO> create(@Valid @RequestBody FechaduraRequestDTO request)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(fechaduraService.save(request));
	}

	@PutMapping
	public ResponseEntity<FechaduraResponseDTO> update(@Valid @RequestBody FechaduraRequestDTO request)
	{
		return ResponseEntity.status(HttpStatus.OK).body(fechaduraService.update(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id)
	{
		fechaduraService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
