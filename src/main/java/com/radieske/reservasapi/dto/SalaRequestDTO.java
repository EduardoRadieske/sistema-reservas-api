package com.radieske.reservasapi.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SalaRequestDTO(
		Integer idSala,

		@NotBlank(message = "O campo 'nome' é obrigatório")
		@Size(max = 100, message = "O campo 'nome' deve ter no máximo 100 caracteres")
		String nome,

		@Size(max = 1000, message = "O campo 'descricao' deve ter no máximo 1000 caracteres")
		String descricao,

		Integer idFechadura,

		FechaduraReferenceDTO fechadura
)
{
	public Integer resolvedIdFechadura()
	{
		if (idFechadura != null)
		{
			return idFechadura;
		}
		if (fechadura != null && fechadura.idFechadura() != null)
		{
			return fechadura.idFechadura();
		}
		return null;
	}

	@AssertTrue(message = "O campo 'idFechadura' é obrigatório")
	public boolean isIdFechaduraValido()
	{
		return resolvedIdFechadura() != null;
	}
}
