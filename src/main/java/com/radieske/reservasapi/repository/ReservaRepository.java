package com.radieske.reservasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radieske.reservasapi.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long>
{

}
