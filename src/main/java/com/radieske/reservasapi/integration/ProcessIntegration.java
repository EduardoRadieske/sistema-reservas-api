package com.radieske.reservasapi.integration;

import org.springframework.scheduling.annotation.Async;

import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.SenhaTemporaria;

public class ProcessIntegration
{
	@Async
	public static void processAutomation(SenhaTemporaria senha)
	{
		Reserva reserva = senha.getReserva();
		
		Fechadura fechadura = reserva.getSala().getFechadura();
		Provedor provedor = fechadura.getProvedor();
		
		Integration integracao = IntegrationFactory.getIntegration(provedor);
		integracao.registerPassword(senha);
	}

}
