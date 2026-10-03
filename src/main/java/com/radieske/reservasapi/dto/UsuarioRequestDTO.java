package com.radieske.reservasapi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radieske.reservasapi.enums.TipoUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
		Integer idUsuario,

		@NotBlank(message = "O campo 'nome' é obrigatório")
		@Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
		String nome,

		@NotBlank(message = "O campo 'usuario' é obrigatório")
		@Size(max = 100, message = "O usuário deve ter no máximo 100 caracteres")
		String usuario,

		@NotBlank(message = "O campo 'senha' é obrigatório")
		@JsonProperty("senha")
		@JsonAlias({ "senhaHash", "password" })
		String senha,

		TipoUsuario tipo
)
{
	public TipoUsuario resolvedTipo()
	{
		return tipo != null ? tipo : TipoUsuario.comum;
	}
}
