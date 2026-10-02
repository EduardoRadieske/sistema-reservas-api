package com.radieske.reservasapi.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.radieske.reservasapi.enums.TipoUsuario;
import com.radieske.reservasapi.model.Usuario;

public class JwtTokenProviderTest
{
	private JwtTokenProvider jwtTokenProvider;
	private final String secret = "teste-secret-key-com-pelo-menos-256-bits-de-comprimento-para-hmac256";
	private final long expiration = 3600L;

	@BeforeEach
	void setUp()
	{
		jwtTokenProvider = new JwtTokenProvider(secret, expiration);
	}

	@Test
	@DisplayName("Deve gerar token JWT com subject e claims válidos e permitir extração do subject")
	void shouldGenerateAndExtractSubjectFromToken()
	{
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(42);
		usuario.setUsuario("carlos.silva");
		usuario.setTipo(TipoUsuario.admin);

		String token = jwtTokenProvider.generateToken(usuario);

		assertNotNull(token);
		String subject = jwtTokenProvider.getSubjectFromToken(token);
		assertEquals("carlos.silva", subject);
	}

	@Test
	@DisplayName("Deve lançar JWTVerificationException ao tentar extrair subject de token corrompido ou adulterado")
	void shouldThrowExceptionWhenTokenIsTampered()
	{
		String tamperedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalidpayload.invalidsignature";

		assertThrows(JWTVerificationException.class, () -> {
			jwtTokenProvider.getSubjectFromToken(tamperedToken);
		});
	}

	@Test
	@DisplayName("Deve retornar o tempo de validade configurado")
	void shouldReturnValidityInSeconds()
	{
		assertEquals(3600L, jwtTokenProvider.getValidityInSeconds());
	}
}
