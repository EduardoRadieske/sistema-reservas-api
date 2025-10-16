package com.radieske.reservasapi.integration.tuya;

import com.radieske.reservasapi.model.Provedor;

public class AuthTuya
{
	private String token;
	
	public AuthTuya(Provedor provedor)
	{
		// TODO implements auth
		this.token = "";
	}
	
	public String getToken() 
	{
		return this.token;
	}
}
