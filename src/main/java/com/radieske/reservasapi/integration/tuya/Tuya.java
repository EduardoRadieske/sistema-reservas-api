package com.radieske.reservasapi.integration.tuya;

import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import org.json.JSONObject;

import com.radieske.reservasapi.integration.Integration;
import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.SenhaTemporaria;
import com.radieske.reservasapi.util.Criptografia;
import com.radieske.reservasapi.util.PasswordEncryptUtil;

public class Tuya implements Integration
{
	private Provedor provedor;
	private AuthTuya auth;
	
	private final String URL_API = "https://openapi.tuyaus.com";
	private final String SIGN_METHOD = "HMAC-SHA256";
	
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
			
		try 
		{
			String strTicket = sendPost("/v1.0/devices/" + fechadura.getChaveDispositivo() 
				+ "/door-lock/password-ticket", "{}");
			
			JSONObject jsonTicket = new JSONObject(strTicket);
			
			String passwordEncrypted = PasswordEncryptUtil.encryptPassword(senha.getCodigo(), provedor.getSecret(), jsonTicket.getString("ticket_key"));
			
			LocalDateTime dataReservaInicial = reserva.getDataReservaInicial();
			LocalDateTime dataReservaFinal   = reserva.getDataReservaFinal();

			long effectiveTime = dataReservaInicial.toEpochSecond(ZoneOffset.UTC);
			long invalidTime   = dataReservaFinal.toEpochSecond(ZoneOffset.UTC);
			
			JSONObject tempPassword = new JSONObject();
			tempPassword.put("device_id", fechadura.getChaveDispositivo());
			tempPassword.put("name", "SenhaTemporariaExemplo");
			tempPassword.put("password", passwordEncrypted);
			tempPassword.put("password_type", "ticket");
	        tempPassword.put("ticket_id", jsonTicket.getString("ticket_id"));
	        tempPassword.put("effective_time", effectiveTime);
	        tempPassword.put("invalid_time", invalidTime);
	        
	        sendPost("/v1.0/devices/" + fechadura.getChaveDispositivo() 
				+ "/door-lock/temp-password", tempPassword.toString());
		} 
		catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String sendPost(String endpoint, String body) throws Exception
	{
		String ak = provedor.getClientId();
		String sk = provedor.getSecret();

		String t = String.valueOf(System.currentTimeMillis());
		String nonce = UUID.randomUUID().toString().replace("-", "");
		
		String urlRequest = URL_API + endpoint;
		System.out.println(body);
		String stringToSign = ak + auth.getToken() + t + nonce + CriptoTuya.buildStringToSign("POST", new URL(urlRequest), body.getBytes(), new HashMap<>());
		String sign = Criptografia.signHmacSHA256(stringToSign, sk);
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(urlRequest))
				.timeout(Duration.ofSeconds(10))
				.header("client_id", ak)
				.header("access_token", auth.getToken())
				.header("sign", sign)
				.header("t", t)
				.header("nonce", nonce)
				.header("sign_method", SIGN_METHOD)
				.POST(BodyPublishers.ofString(body))
				.build();
		
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() == 200)
		{
			JSONObject json = new JSONObject(response.body());
			if (json.getBoolean("success"))
			{
				return json.getJSONObject("result").toString();
			} else
			{
				System.err.println("Erro Tuya: " + json.toString());
			}
		} else
		{
			System.err.println("Erro Tuya HTTP: " + response.statusCode() 
				+ "\nBody: " + response.body());
		}
		
		return null;
	}
}
