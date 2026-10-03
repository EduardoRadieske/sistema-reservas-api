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

import com.radieske.reservasapi.dto.ProvedorRequestDTO;
import com.radieske.reservasapi.dto.ProvedorResponseDTO;
import com.radieske.reservasapi.service.ProvedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("provedor")
@PreAuthorize("hasRole('admin')")
public class ProvedorController
{
	@Autowired
	private ProvedorService provedorService;

	@GetMapping
	public ResponseEntity<List<ProvedorResponseDTO>> findAll()
	{
		return ResponseEntity.status(HttpStatus.OK).body(provedorService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProvedorResponseDTO> findById(@PathVariable Integer id)
	{
		return ResponseEntity.of(provedorService.findById(id));
	}

	@PostMapping
	public ResponseEntity<ProvedorResponseDTO> create(@Valid @RequestBody ProvedorRequestDTO provedor)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(provedorService.save(provedor));
	}

	@PutMapping
	public ResponseEntity<ProvedorResponseDTO> update(@Valid @RequestBody ProvedorRequestDTO provedor)
	{
		return ResponseEntity.status(HttpStatus.OK).body(provedorService.update(provedor));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id)
	{
		provedorService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
