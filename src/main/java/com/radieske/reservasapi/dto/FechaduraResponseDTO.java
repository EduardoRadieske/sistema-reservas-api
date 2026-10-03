package com.radieske.reservasapi.dto;

import com.radieske.reservasapi.model.Fechadura;

public record FechaduraResponseDTO(
		Integer idFechadura,
		String chaveDispositivo,
		ProvedorResponseDTO provedor
)
{
	public static FechaduraResponseDTO fromEntity(Fechadura fechadura)
	{
		if (fechadura == null)
			return null;

		return new FechaduraResponseDTO(
				fechadura.getIdFechadura(),
				fechadura.getChaveDispositivo(),
				ProvedorResponseDTO.fromEntity(fechadura.getProvedor())
		);
	}
}
