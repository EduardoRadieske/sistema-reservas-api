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

import com.radieske.reservasapi.integration.tuya.Tuya;
import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.service.ProvedorService;

@RestController
@RequestMapping("provedor")
@PreAuthorize("hasRole('admin')")
public class ProvedorController
{
	@Autowired
	private ProvedorService provedorService;

	@GetMapping
	public ResponseEntity<List<Provedor>> findAll()
	{
		// TODO - PARA TESTES
		try 
		{
			Provedor prov = provedorService.findById(3).get();
			
			new Tuya(prov);
		} catch(Exception ex) {
			System.err.println("Erro ao autenticar Tuya: " + ex.getMessage());
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(provedorService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Provedor>> findById(@PathVariable Integer id)
	{
		return ResponseEntity.status(HttpStatus.OK).body(provedorService.findById(id));
	}

	@PostMapping
	public ResponseEntity<Provedor> create(@RequestBody Provedor usuario)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(provedorService.save(usuario));
	}

	@PutMapping
	public ResponseEntity<Provedor> update(@RequestBody Provedor usuario)
	{
		return ResponseEntity.status(HttpStatus.OK).body(provedorService.update(usuario));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id)
	{
		provedorService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
