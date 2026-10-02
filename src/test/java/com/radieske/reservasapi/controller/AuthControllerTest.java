package com.radieske.reservasapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radieske.reservasapi.dto.LoginData;
import com.radieske.reservasapi.dto.LoginResponseDTO;
import com.radieske.reservasapi.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AuthService authService;

	@Test
	@DisplayName("Deve delegar ao AuthService e retornar 200 OK com LoginResponseDTO")
	void shouldLoginSuccessfully() throws Exception
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario.teste");
		loginData.setSenha("senha123");

		LoginResponseDTO responseDTO = new LoginResponseDTO("mocked-jwt-token", "Bearer", 3600L);

		when(authService.login(any(LoginData.class))).thenReturn(responseDTO);

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginData)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("mocked-jwt-token"))
				.andExpect(jsonPath("$.type").value("Bearer"))
				.andExpect(jsonPath("$.expiresIn").value(3600));

		verify(authService).login(any(LoginData.class));
	}

	@Test
	@DisplayName("Deve retornar 401 Unauthorized quando AuthService lançar BadCredentialsException")
	void shouldReturn401WhenAuthServiceThrowsBadCredentials() throws Exception
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario.teste");
		loginData.setSenha("senha_errada");

		when(authService.login(any(LoginData.class)))
				.thenThrow(new BadCredentialsException("Usuário ou senha inválidos"));

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginData)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.error").value("Usuário ou senha inválidos"));

		verify(authService).login(any(LoginData.class));
	}

	@Test
	@DisplayName("Deve retornar 400 Bad Request e NÃO chamar AuthService quando 'usuario' for em branco")
	void shouldReturn400WhenUsuarioIsBlank() throws Exception
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("");
		loginData.setSenha("senha123");

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginData)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("O campo 'usuario' é obrigatório"));

		verify(authService, never()).login(any());
	}

	@Test
	@DisplayName("Deve retornar 400 Bad Request e NÃO chamar AuthService quando 'senha' for em branco")
	void shouldReturn400WhenSenhaIsBlank() throws Exception
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario.teste");
		loginData.setSenha("");

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginData)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("O campo 'senha' é obrigatório"));

		verify(authService, never()).login(any());
	}
}
