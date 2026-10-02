package com.radieske.reservasapi.security;

import java.time.Instant;

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

	private static final String ISSUER = "sistema-reservas-api";

	public JwtTokenProvider(@Value("${jwt.secret:5ad7de25bbc2dccef1c6ddbfa19ceb3dbac67003c84644b7aa68cb54325e1c030954067745048e56776f0e1cbf91e183dd9042490c275cd174aa60b7a5fde75e}") String secretKey,
			@Value("${jwt.expiration:3600}") long validityInSeconds)
	{
		this.SECRET_KEY = secretKey;
		this.validityInSeconds = validityInSeconds;
	}

	public long getValidityInSeconds()
	{
		return validityInSeconds;
	}

	public String generateToken(Usuario user)
	{
		try
		{
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			Instant now = Instant.now();
			var builder = JWT.create()
					.withIssuer(ISSUER)
					.withIssuedAt(now)
					.withExpiresAt(now.plusSeconds(validityInSeconds))
					.withSubject(user.getUsuario());

			if (user.getTipo() != null)
			{
				builder.withClaim("ROLE", user.getTipo().name());
			}

			if (user.getIdUsuario() != null)
			{
				builder.withJWTId(user.getIdUsuario().toString());
			}

			return builder.sign(algorithm);
		} catch (JWTCreationException exception)
		{
			throw new JWTCreationException("Erro ao gerar token JWT.", exception);
		}
	}

	public String getSubjectFromToken(String token)
	{
		try
		{
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			return JWT.require(algorithm).withIssuer(ISSUER)
					.build().verify(token)
					.getSubject();
		} catch (JWTVerificationException exception)
		{
			throw new JWTVerificationException("Token JWT inválido ou expirado.");
		}
	}
}
