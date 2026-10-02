package com.radieske.reservasapi.util;

import java.security.SecureRandom;

public class PasswordGenerator
{
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	public static String generateRandomDigits()
	{
		return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
	}
}
