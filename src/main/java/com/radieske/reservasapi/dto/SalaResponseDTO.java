package com.radieske.reservasapi.dto;

import com.radieske.reservasapi.model.Sala;

public record SalaResponseDTO(
		Integer idSala,
		String nome,
		String descricao,
		Integer idFechadura
)
{
	public static SalaResponseDTO fromEntity(Sala sala)
	{
		if (sala == null)
			return null;

		Integer fechaduraId = sala.getFechadura() != null ? sala.getFechadura().getIdFechadura() : null;

		return new SalaResponseDTO(
				sala.getIdSala(),
				sala.getNome(),
				sala.getDescricao(),
				fechaduraId
		);
	}
}
