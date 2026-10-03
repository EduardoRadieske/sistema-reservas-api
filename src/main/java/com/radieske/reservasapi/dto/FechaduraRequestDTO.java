package com.radieske.reservasapi.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FechaduraRequestDTO(
		Integer idFechadura,

		@NotBlank(message = "O campo 'chaveDispositivo' é obrigatório")
		@Size(max = 500, message = "O campo 'chaveDispositivo' deve ter no máximo 500 caracteres")
		String chaveDispositivo,

		Integer idProvedor,

		ProvedorReferenceDTO provedor
)
{
	public Integer resolvedIdProvedor()
	{
		if (idProvedor != null)
		{
			return idProvedor;
		}
		if (provedor != null && provedor.idProvedor() != null)
		{
			return provedor.idProvedor();
		}
		return null;
	}

	@AssertTrue(message = "O campo 'idProvedor' é obrigatório")
	public boolean isIdProvedorValido()
	{
		return resolvedIdProvedor() != null;
	}
}
