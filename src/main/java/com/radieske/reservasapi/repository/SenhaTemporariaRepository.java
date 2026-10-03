package com.radieske.reservasapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.radieske.reservasapi.enums.StatusIntegracao;
import com.radieske.reservasapi.model.SenhaTemporaria;

public interface SenhaTemporariaRepository extends JpaRepository<SenhaTemporaria, Long>
{
	@Query(value = " SELECT * FROM senhas_temporarias WHERE id_reserva = ?1 ", nativeQuery = true)
	Optional<SenhaTemporaria> findByIdReserva(int idReserva);

	@EntityGraph(attributePaths = {
			"reserva",
			"reserva.sala",
			"reserva.sala.fechadura",
			"reserva.sala.fechadura.provedor"
	})
	@Query("SELECT s FROM SenhaTemporaria s WHERE s.idSenha = :id")
	Optional<SenhaTemporaria> findForIntegrationById(@Param("id") Integer id);

	@Modifying
	@Transactional
	@Query("UPDATE SenhaTemporaria s SET s.statusIntegracao = :status, "
			+ "s.mensagemIntegracao = :mensagem WHERE s.idSenha = :id")
	int updateIntegrationResult(
			@Param("id") Integer id,
			@Param("status") StatusIntegracao status,
			@Param("mensagem") String mensagem);
}
