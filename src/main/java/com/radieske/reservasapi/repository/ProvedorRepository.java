package com.radieske.reservasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radieske.reservasapi.model.Provedor;

public interface ProvedorRepository extends JpaRepository<Provedor, Integer>
{
	
}