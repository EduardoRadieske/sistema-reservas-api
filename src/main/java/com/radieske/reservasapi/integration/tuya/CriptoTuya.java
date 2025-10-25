package com.radieske.reservasapi.integration.tuya;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.radieske.reservasapi.util.Criptografia;

public class CriptoTuya
{
	private static final String EMPTY_HASH = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
	
	/**
	 * Constrói o conteúdo que será assinado (seguindo padrão oficial Tuya)
	 */
	public static String buildStringToSign(String method, URL url, byte[] body, Map<String, String> headers) throws Exception
	{
		List<String> lines = new ArrayList<>();

		lines.add(method.toUpperCase());

		String bodyHash = EMPTY_HASH;
		if (body != null && body.length > 0)
		{
			bodyHash = Criptografia.sha256Hex(body);
		}
		lines.add(bodyHash);

		String signHeaders = headers.getOrDefault("Signature-Headers", "");
		String headerLine = "";
		if (!signHeaders.isEmpty())
		{
			String[] sighHeaderNames = signHeaders.split("\\s*:\\s*");
			headerLine = Arrays.stream(sighHeaderNames).map(String::trim).filter(it -> it.length() > 0)
					.map(it -> it + ":" + headers.get(it)).reduce((a, b) -> a + "\n" + b).orElse("");
		}
		lines.add(headerLine);

		lines.add(getPathAndSortParam(url));

		return String.join("\n", lines);
	}

	/**
	 * Ordena os parâmetros da URL (mesmo padrão do SDK)
	 */
	public static String getPathAndSortParam(URL url) throws Exception
	{
		String path = url.getPath();
		String query = url.getQuery();

		if (query == null || query.isEmpty())
		{
			return path;
		}

		Map<String, String> kvMap = new TreeMap<>();
		for (String kv : query.split("&"))
		{
			String[] kvArr = kv.split("=");
			if (kvArr.length > 1)
			{
				kvMap.put(kvArr[0], URLDecoder.decode(kvArr[1], "utf-8"));
			} else
			{
				kvMap.put(kvArr[0], "");
			}
		}

		String sortedQuery = kvMap.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
				.reduce((a, b) -> a + "&" + b).orElse("");

		return path + "?" + sortedQuery;
	}
}
