package com.radieske.reservasapi.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.radieske.reservasapi.config.SecurityConfig;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter
{
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UsuarioRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		if (checkIfEndpointIsNotPublic(request))
		{
			String token = recoveryToken(request);
			if (token != null)
			{
				String subject = jwtTokenProvider.getSubjectFromToken(token);
				Usuario user = userRepository.findByUsuario(subject).get();
				
				List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + user.getTipo().name().toLowerCase()));

				Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsuario(), null, roles);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else
			{
				throw new RuntimeException("O token está ausente.");
			}
		}
		filterChain.doFilter(request, response);
	}

	private String recoveryToken(HttpServletRequest request)
	{
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null)
		{
			return authorizationHeader.replace("Bearer ", "");
		}
		return null;
	}

	private boolean checkIfEndpointIsNotPublic(HttpServletRequest request)
	{
		String requestURI = request.getRequestURI();
		return !Arrays.asList(SecurityConfig.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
	}
}
