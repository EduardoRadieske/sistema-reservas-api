package com.radieske.reservasapi.integration.tuya;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.util.Criptografia;

public class AuthTuya
{

	private final String SIGN_METHOD = "HMAC-SHA256";
	
	private final String URL_API = "https://openapi.tuyaus.com/v1.0/token?grant_type=1";

	private final Provedor provedor;
	private String token;

	public AuthTuya(Provedor provedor)
	{
		this.provedor = provedor;
		this.token = requestToken();
	}

	public String getToken()
	{
		return token;
	}

	private String requestToken()
	{
		try
		{
			String ak = provedor.getClientId();
			String sk = provedor.getSecret();

			String t = String.valueOf(System.currentTimeMillis());
			String nonce = UUID.randomUUID().toString().replace("-", "");

			String stringToSign = ak + t + nonce + CriptoTuya.buildStringToSign("GET", new URL(URL_API), null, new HashMap<>());
			String sign = Criptografia.signHmacSHA256(stringToSign, sk);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(URL_API))
					.timeout(Duration.ofSeconds(10))
					.header("client_id", ak)
					.header("sign", sign)
					.header("t", t)
					.header("nonce", nonce)
					.header("sign_method", SIGN_METHOD)
					.GET()
					.build();
			
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200)
			{
				JSONObject json = new JSONObject(response.body());
				if (json.getBoolean("success"))
				{
					String accessToken = json.getJSONObject("result").getString("access_token");
					System.out.println("✅ Token Tuya obtido com sucesso: " + accessToken);
					return accessToken;
				} else
				{
					System.err.println("Erro Tuya: " + json.toString());
				}
			} else
			{
				System.err.println("Erro HTTP: " + response.statusCode());
				System.err.println("Body: " + response.body());
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}