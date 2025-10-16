package com.radieske.reservasapi.dto;

import com.radieske.reservasapi.model.Sala;

public record SalaDTO(Integer idSala, String nome, String descricao, Integer idFechadura)
{
	public static SalaDTO fromEntity(Sala sala)
	{
		if (sala == null)
			return null;

		return new SalaDTO(sala.getIdSala(), sala.getNome(), sala.getDescricao(), sala.getFechadura().getIdFechadura());
	}
}
