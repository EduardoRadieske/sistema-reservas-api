package com.radieske.reservasapi.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest
{
	@Test
	@DisplayName("Deve gerar senha de exatamente 6 dígitos numéricos")
	void shouldGenerateSixDigitNumericPassword()
	{
		for (int i = 0; i < 50; i++)
		{
			String pin = PasswordGenerator.generateRandomDigits();
			assertNotNull(pin);
			assertEquals(6, pin.length(), "PIN deve conter exatamente 6 caracteres");
			assertTrue(pin.matches("\\d{6}"), "PIN deve conter apenas dígitos numéricos");
		}
	}
}
