package com.radieske.reservasapi.dto;

import com.radieske.reservasapi.enums.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProvedorRequestDTO(
		Integer idProvedor,

		@NotBlank(message = "O campo 'provedor' é obrigatório")
		@Size(max = 10, message = "O campo 'provedor' deve ter no máximo 10 caracteres")
		String provedor,

		Status status,

		@Size(max = 500, message = "O campo 'clientId' deve ter no máximo 500 caracteres")
		String clientId,

		@Size(max = 500, message = "O campo 'secret' deve ter no máximo 500 caracteres")
		String secret
)
{
	public Status resolvedStatus()
	{
		return status != null ? status : Status.S;
	}
}
