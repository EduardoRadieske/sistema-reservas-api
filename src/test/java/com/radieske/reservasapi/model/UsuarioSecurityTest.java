package com.radieske.reservasapi.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radieske.reservasapi.enums.TipoUsuario;

public class UsuarioSecurityTest
{
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("Deve omitir senhaHash ao serializar Usuario para JSON (proteção contra vazamento de credenciais)")
	void shouldNotSerializeSenhaHashToJson() throws Exception
	{
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(10);
		usuario.setNome("João Teste");
		usuario.setUsuario("joao.teste");
		usuario.setSenhaHash("$2a$10$abcdefghijklmnopqrstuvwx");
		usuario.setTipo(TipoUsuario.comum);

		String json = objectMapper.writeValueAsString(usuario);

		assertTrue(json.contains("\"usuario\":\"joao.teste\""));
		assertTrue(json.contains("\"nome\":\"João Teste\""));
		assertFalse(json.contains("senhaHash"), "senhaHash não deve estar presente no JSON serializado!");
		assertFalse(json.contains("senha_hash"), "senha_hash não deve estar presente no JSON serializado!");
		assertFalse(json.contains("$2a$10$"), "O hash bcrypt da senha não deve vazar no JSON!");
	}
}
