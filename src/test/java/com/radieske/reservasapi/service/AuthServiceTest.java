package com.radieske.reservasapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.radieske.reservasapi.dto.LoginData;
import com.radieske.reservasapi.dto.LoginResponseDTO;
import com.radieske.reservasapi.enums.TipoUsuario;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.security.JwtTokenProvider;
import com.radieske.reservasapi.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest
{
	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private UsuarioRepository userRepository;

	@InjectMocks
	private AuthServiceImpl authService;

	@BeforeEach
	void setUp()
	{
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("Deve autenticar com sucesso e retornar LoginResponseDTO")
	void shouldAuthenticateAndReturnToken()
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario.teste");
		loginData.setSenha("senha123");

		Usuario usuario = new Usuario();
		usuario.setIdUsuario(10);
		usuario.setUsuario("usuario.teste");
		usuario.setTipo(TipoUsuario.comum);

		Authentication authentication = new UsernamePasswordAuthenticationToken("usuario.teste", null);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		when(userRepository.findByUsuario("usuario.teste")).thenReturn(Optional.of(usuario));
		when(jwtTokenProvider.generateToken(usuario)).thenReturn("mocked.jwt.token");
		when(jwtTokenProvider.getValidityInSeconds()).thenReturn(3600L);

		LoginResponseDTO response = authService.login(loginData);

		assertNotNull(response);
		assertEquals("mocked.jwt.token", response.token());
		assertEquals("Bearer", response.type());
		assertEquals(3600L, response.expiresIn());
		assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());

		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userRepository).findByUsuario("usuario.teste");
		verify(jwtTokenProvider).generateToken(usuario);
	}

	@Test
	@DisplayName("Deve repassar BadCredentialsException quando a autenticação falhar")
	void shouldThrowBadCredentialsExceptionWhenAuthFails()
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario.teste");
		loginData.setSenha("senha_errada");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
			authService.login(loginData);
		});

		assertEquals("Bad credentials", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lançar BadCredentialsException unificada caso o usuário não seja localizado após autenticar")
	void shouldThrowBadCredentialsExceptionWhenUserNotFoundInRepository()
	{
		LoginData loginData = new LoginData();
		loginData.setUsuario("usuario.fantasma");
		loginData.setSenha("senha123");

		Authentication authentication = new UsernamePasswordAuthenticationToken("usuario.fantasma", null);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		when(userRepository.findByUsuario("usuario.fantasma")).thenReturn(Optional.empty());

		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
			authService.login(loginData);
		});

		assertEquals("Usuário ou senha inválidos", exception.getMessage());
	}
}
