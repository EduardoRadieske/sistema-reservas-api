package com.radieske.reservasapi.security;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.radieske.reservasapi.enums.TipoUsuario;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest
{
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private JwtTokenProvider jwtTokenProvider;

	@MockitoBean
	private UsuarioRepository userRepository;

	@Test
	@DisplayName("Deve retornar 401 JSON padronizado ao acessar endpoint protegido sem token")
	void shouldReturn401WhenAccessingProtectedWithoutToken() throws Exception
	{
		mockMvc.perform(get("/reservas"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.error").value("Acesso não autorizado. Token ausente ou inválido."));
	}

	@Test
	@DisplayName("Deve retornar 401 JSON padronizado quando o token JWT for inválido ou expirado")
	void shouldReturn401WhenTokenIsInvalid() throws Exception
	{
		when(jwtTokenProvider.getSubjectFromToken("token_invalido"))
				.thenThrow(new JWTVerificationException("Token JWT inválido ou expirado."));

		mockMvc.perform(get("/reservas")
				.header("Authorization", "Bearer token_invalido"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.error").value("Acesso não autorizado. Token ausente ou inválido."));
	}

	@Test
	@DisplayName("Deve permitir requisições OPTIONS (preflight CORS) sem token de autenticação")
	void shouldAllowCorsPreflightOptionsRequests() throws Exception
	{
		mockMvc.perform(options("/reservas")
				.header("Origin", "http://localhost:4200")
				.header("Access-Control-Request-Method", "GET"))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deve autenticar com sucesso quando o token for válido e o usuário existir")
	void shouldAuthenticateSuccessfullyWithValidToken() throws Exception
	{
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1);
		usuario.setUsuario("admin.teste");
		usuario.setTipo(TipoUsuario.admin);

		when(jwtTokenProvider.getSubjectFromToken("token_valido")).thenReturn("admin.teste");
		when(userRepository.findByUsuario("admin.teste")).thenReturn(Optional.of(usuario));

		mockMvc.perform(get("/reservas")
				.header("Authorization", "Bearer token_valido"))
				.andExpect(status().isOk());
	}
}
