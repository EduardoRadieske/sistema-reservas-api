package com.radieske.reservasapi.dto;

import com.radieske.reservasapi.model.Usuario;

public record UsuarioDTO(Integer idUsuario, String nome)
{
	public static UsuarioDTO fromEntity(Usuario usuario)
	{
		if (usuario == null)
			return null;

		return new UsuarioDTO(usuario.getIdUsuario(), usuario.getNome());
	}
}
