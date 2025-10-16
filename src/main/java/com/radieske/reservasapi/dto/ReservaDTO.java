package com.radieske.reservasapi.dto;

import com.radieske.reservasapi.enums.Status;
import com.radieske.reservasapi.model.Reserva;

public record ReservaDTO(
		Integer idReserva, 
		UsuarioDTO usuario, 
		SalaDTO sala, 
		String dataReservaInicial,
		String dataReservaFinal, 
		Status status
)
{
	public static ReservaDTO fromEntity(Reserva reserva)
	{
		if (reserva == null)
			return null;

		SalaDTO salaDTO = SalaDTO.fromEntity(reserva.getSala());
		UsuarioDTO ususarioDTO = UsuarioDTO.fromEntity(reserva.getUsuario());

		return new ReservaDTO(reserva.getIdReserva(),
				ususarioDTO, salaDTO,
				reserva.getDataReservaInicial() != null ? reserva.getDataReservaInicial().toString() : null,
				reserva.getDataReservaFinal() != null ? reserva.getDataReservaFinal().toString() : null,
				reserva.getStatus());
	}
}
