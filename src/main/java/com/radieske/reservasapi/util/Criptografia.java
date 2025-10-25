package com.radieske.reservasapi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Criptografia
{
	public static String signHmacSHA256(String data, String key) throws Exception
	{
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		mac.init(secretKey);
		byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

		StringBuilder hex = new StringBuilder();
		for (byte b : hash)
		{
			String h = Integer.toHexString(0xff & b);
			if (h.length() == 1)
				hex.append('0');
			hex.append(h);
		}
		return hex.toString().toUpperCase();
	}
	
	public static String sha256Hex(byte[] data) throws Exception
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(data);
		StringBuilder hex = new StringBuilder();
		for (byte b : hash)
		{
			String h = Integer.toHexString(0xff & b);
			if (h.length() == 1)
				hex.append('0');
			hex.append(h);
		}
		return hex.toString();
	}
}
