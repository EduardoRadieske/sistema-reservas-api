package com.radieske.reservasapi.util;

import java.util.Random;

public class PasswordGenerator
{
	public static String generateRandomDigits()
	{
		Random random = new Random();
        return String.format("%06d", random.nextInt(1_000_000));
	}
}
