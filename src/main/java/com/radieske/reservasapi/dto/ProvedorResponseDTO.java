package com.radieske.reservasapi.dto;

import com.radieske.reservasapi.enums.Status;
import com.radieske.reservasapi.model.Provedor;

public record ProvedorResponseDTO(
		Integer idProvedor,
		String provedor,
		Status status,
		String clientId,
		String secret
)
{
	public static ProvedorResponseDTO fromEntity(Provedor provedor)
	{
		if (provedor == null)
			return null;

		return new ProvedorResponseDTO(
				provedor.getIdProvedor(),
				provedor.getProvedor(),
				provedor.getStatus(),
				provedor.getClientId(),
				provedor.getSecret()
		);
	}
}
