package com.radieske.reservasapi.dto;

import java.time.LocalDateTime;

import com.radieske.reservasapi.enums.Status;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record ReservaRequestDTO(
		Integer idReserva,

		Integer idUsuario,

		UsuarioReferenceDTO usuario,

		Integer idSala,

		SalaReferenceDTO sala,

		@NotNull(message = "O campo 'dataReservaInicial' é obrigatório")
		LocalDateTime dataReservaInicial,

		@NotNull(message = "O campo 'dataReservaFinal' é obrigatório")
		LocalDateTime dataReservaFinal,

		Status status
)
{
	public Integer resolvedIdUsuario()
	{
		if (idUsuario != null)
		{
			return idUsuario;
		}
		if (usuario != null && usuario.idUsuario() != null)
		{
			return usuario.idUsuario();
		}
		return null;
	}

	public Integer resolvedIdSala()
	{
		if (idSala != null)
		{
			return idSala;
		}
		if (sala != null && sala.idSala() != null)
		{
			return sala.idSala();
		}
		return null;
	}

	public Status resolvedStatus()
	{
		return status != null ? status : Status.S;
	}

	@AssertTrue(message = "O campo 'idUsuario' é obrigatório")
	public boolean isUsuarioValido()
	{
		return resolvedIdUsuario() != null;
	}

	@AssertTrue(message = "O campo 'idSala' é obrigatório")
	public boolean isSalaValida()
	{
		return resolvedIdSala() != null;
	}
}
