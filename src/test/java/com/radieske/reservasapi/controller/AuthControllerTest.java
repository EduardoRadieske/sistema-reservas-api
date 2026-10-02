package com.radieske.reservasapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radieske.reservasapi.dto.LoginData;
import com.radieske.reservasapi.enums.TipoUsuario;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.security.JwtTokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AuthenticationManager authenticationManager;

	@MockitoBean
	private JwtTokenProvider jwtTokenProvider;

	@MockitoBean
	private UsuarioRepository userRepository;

	@Test
	@DisplayName("Deve realizar login com sucesso e retornar token Bearer e expiresIn")
	void shouldLoginSuccessfully() throws Exception
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario.teste");
		loginData.setSenha("senha123");

		Usuario usuario = new Usuario();
		usuario.setIdUsuario(1);
		usuario.setUsuario("usuario.teste");
		usuario.setTipo(TipoUsuario.comum);

		Authentication authResult = new UsernamePasswordAuthenticationToken("usuario.teste", null);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authResult);
		when(userRepository.findByUsuario("usuario.teste")).thenReturn(Optional.of(usuario));
		when(jwtTokenProvider.generateToken(usuario)).thenReturn("mocked-jwt-token");
		when(jwtTokenProvider.getValidityInSeconds()).thenReturn(3600L);

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginData)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("mocked-jwt-token"))
				.andExpect(jsonPath("$.type").value("Bearer"))
				.andExpect(jsonPath("$.expiresIn").value(3600));
	}

	@Test
	@DisplayName("Deve retornar 401 com mensagem unificada quando a senha estiver incorreta")
	void shouldReturn401WhenPasswordIsIncorrect() throws Exception
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario.teste");
		loginData.setSenha("senha_errada");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginData)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.error").value("Usuário ou senha inválidos"));
	}

	@Test
	@DisplayName("Deve retornar 401 com a MESMA mensagem unificada quando usuário não existir (anti-enumeração)")
	void shouldReturn401WhenUserDoesNotExistWithoutEnumeration() throws Exception
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario_inexistente");
		loginData.setSenha("qualquer_senha");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginData)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.error").value("Usuário ou senha inválidos"));
	}

	@Test
	@DisplayName("Deve retornar 400 Bad Request quando usuário não for informado")
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
	}

	@Test
	@DisplayName("Deve retornar 400 Bad Request quando senha não for informada")
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
	}
}
