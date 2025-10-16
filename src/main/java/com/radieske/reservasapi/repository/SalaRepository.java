package com.radieske.reservasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radieske.reservasapi.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Integer>
{
	
}
