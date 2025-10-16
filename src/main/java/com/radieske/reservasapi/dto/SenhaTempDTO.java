package com.radieske.reservasapi.dto;

import com.radieske.reservasapi.model.SenhaTemporaria;

public record SenhaTempDTO(Integer id, Integer idReserva, String codigo)
{
	public static SenhaTempDTO fromEntity(SenhaTemporaria senha)
	{
		if (senha == null)
			return null;

		return new SenhaTempDTO(senha.getIdSenha(), 
				senha.getReserva().getIdReserva(), 
				senha.getCodigo());
	}
}
