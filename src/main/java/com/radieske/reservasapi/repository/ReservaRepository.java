package com.radieske.reservasapi.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.radieske.reservasapi.enums.Status;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Integer>
{
	 @Query(value = ""
	 		+ " SELECT r.* "
	 		+ " FROM reservas as r "
	 		+ " WHERE r.id_sala = ?1 "
	 		+ " AND ( "
	 		+ "    (r.data_reserva_inicial <= ?3 AND r.data_reserva_final >= ?2) "
	 		+ " ) ", nativeQuery = true)
	 Optional<Reserva> findByBetweenDate(int idSala, LocalDateTime dataI, LocalDateTime dataF);
	 
	 Optional<List<Reserva>> findByStatusAndUsuario(Status status, Usuario idUsuario);
}
