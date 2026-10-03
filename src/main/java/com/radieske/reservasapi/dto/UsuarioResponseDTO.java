package com.radieske.reservasapi.dto;

import java.time.LocalDateTime;

import com.radieske.reservasapi.enums.TipoUsuario;
import com.radieske.reservasapi.model.Usuario;

public record UsuarioResponseDTO(
		Integer idUsuario,
		String nome,
		String usuario,
		TipoUsuario tipo,
		LocalDateTime createdAt
)
{
	public static UsuarioResponseDTO fromEntity(Usuario usuario)
	{
		if (usuario == null)
			return null;

		return new UsuarioResponseDTO(
				usuario.getIdUsuario(),
				usuario.getNome(),
				usuario.getUsuario(),
				usuario.getTipo(),
				usuario.getCreatedAt()
		);
	}
}
