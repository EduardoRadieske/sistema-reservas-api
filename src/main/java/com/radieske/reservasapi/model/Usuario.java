package com.radieske.reservasapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;

	@Column(name = "nome", length = 100)
	private String nome;

	@Column(name = "usuario", length = 100, nullable = false, unique = true)
	private String usuario;

	@Column(name = "senha_hash", length = 1000, nullable = false)
	private String senhaHash;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false)
	private TipoUsuario tipo = TipoUsuario.comum;

	@Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "timestamp default now()")
	private LocalDateTime createdAt;
}
