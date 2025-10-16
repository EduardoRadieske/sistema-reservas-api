package com.radieske.reservasapi.integration;

import com.radieske.reservasapi.integration.tuya.Tuya;
import com.radieske.reservasapi.model.Provedor;

public class IntegrationFactory
{
	public static Integration getIntegration(Provedor provedor) 
	{
		switch (provedor.getProvedor())
		{
		case "1":
			return new Tuya(provedor);
		default:
			throw new IllegalArgumentException("Unexpected value: " + provedor.getProvedor());
		}
	}
}
