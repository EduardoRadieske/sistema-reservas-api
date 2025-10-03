package com.radieske.reservasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radieske.reservasapi.model.SenhaTemporaria;

public interface SenhaTemporariaRepository extends JpaRepository<SenhaTemporaria, Long>
{

}
