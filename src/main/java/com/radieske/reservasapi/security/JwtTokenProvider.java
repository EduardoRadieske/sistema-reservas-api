package com.radieske.reservasapi.security;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.radieske.reservasapi.model.Usuario;

@Service
public class JwtTokenProvider
{
	private final String SECRET_KEY;
	private final long validityInSeconds;

	private static final String ISSUER = "pizzurg-api"; // Emissor do token

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
			@Value("${jwt.expiration}") long validityInSeconds)
	{
		this.SECRET_KEY = secretKey;
		this.validityInSeconds = validityInSeconds;
	}

	public String generateToken(Usuario user)
	{
		try
		{
			// Define o algoritmo HMAC SHA256 para criar a assinatura do token passando a
			// chave secreta definida
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			return JWT.create().withIssuer(ISSUER) // Define o emissor do token
					.withIssuedAt(creationDate()) // Define a data de emissão do token
					.withExpiresAt(expirationDate()) // Define a data de expiração do token
					.withSubject(user.getUsuario()) // Define o assunto do token (neste caso, o nome de usuário)
					.sign(algorithm); // Assina o token usando o algoritmo especificado
		} catch (JWTCreationException exception)
		{
			throw new JWTCreationException("Erro ao gerar token JWT.", exception);
		}
	}

	public String getSubjectFromToken(String token)
	{
		try
		{
			// Define o algoritmo HMAC SHA256 para verificar a assinatura do token passando
			// a chave secreta definida
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			return JWT.require(algorithm).withIssuer(ISSUER) // Define o emissor do token
					.build().verify(token) // Verifica a validade do token
					.getSubject(); // Obtém o assunto (neste caso, o nome de usuário) do token
		} catch (JWTVerificationException exception)
		{
			throw new JWTVerificationException("Token JWT inválido ou expirado.");
		}
	}

	private Instant creationDate()
	{
		return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
	}

	private Instant expirationDate()
	{
		return ZonedDateTime.now(ZoneId.of("America/Recife")).plusSeconds(validityInSeconds).toInstant();
	}
}
