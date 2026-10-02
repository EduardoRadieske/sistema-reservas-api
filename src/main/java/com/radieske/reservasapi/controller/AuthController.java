package com.radieske.reservasapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radieske.reservasapi.dto.LoginData;
import com.radieske.reservasapi.dto.LoginResponseDTO;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.security.JwtTokenProvider;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController
{
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UsuarioRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginData loginData)
	{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginData.getUsuario(), loginData.getSenha())
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		Usuario user = userRepository.findByUsuario(authentication.getName())
				.orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos"));

		String token = jwtTokenProvider.generateToken(user);
		long expiresIn = jwtTokenProvider.getValidityInSeconds();

		return ResponseEntity.ok(new LoginResponseDTO(token, "Bearer", expiresIn));
	}
}
