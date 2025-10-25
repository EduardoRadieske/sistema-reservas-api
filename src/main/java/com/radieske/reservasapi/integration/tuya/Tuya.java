package com.radieske.reservasapi.integration.tuya;

import com.radieske.reservasapi.integration.Integration;
import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.SenhaTemporaria;

public class Tuya implements Integration
{
	private Provedor provedor;
	private AuthTuya auth;
	
	public Tuya(Provedor provedor)
	{
		this.provedor = provedor;
		this.auth = new AuthTuya(provedor);
	}
	
	@Override
	public void registerPassword(SenhaTemporaria senha)
	{
		Reserva reserva = senha.getReserva();
		Fechadura fechadura = reserva.getSala().getFechadura();
		
		// TODO implements methods
		
		String tokenTeste = auth.getToken();
		
		provedor.getSecret();
		
		fechadura.getChaveDispositivo();
	}
}
