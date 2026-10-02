package com.radieske.reservasapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.dto.LoginData;
import com.radieske.reservasapi.dto.LoginResponseDTO;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.security.JwtTokenProvider;
import com.radieske.reservasapi.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService
{
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UsuarioRepository userRepository;

	@Override
	public LoginResponseDTO login(LoginData loginData)
	{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginData.getUsuario(), loginData.getSenha())
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		Usuario user = userRepository.findByUsuario(authentication.getName())
				.orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos"));

		String token = jwtTokenProvider.generateToken(user);
		long expiresIn = jwtTokenProvider.getValidityInSeconds();

		return new LoginResponseDTO(token, "Bearer", expiresIn);
	}
}
