package com.radieske.reservasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radieske.reservasapi.model.Fechadura;

public interface FechaduraRepository extends JpaRepository<Fechadura, Integer>
{
	
}
