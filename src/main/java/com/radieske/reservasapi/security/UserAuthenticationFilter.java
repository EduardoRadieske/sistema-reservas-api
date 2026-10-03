package com.radieske.reservasapi.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
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
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
	{
		String token = recoveryToken(request);

		if (token != null)
		{
			try
			{
				String subject = jwtTokenProvider.getSubjectFromToken(token);
				if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null)
				{
					userRepository.findByUsuario(subject).ifPresent(user -> {
						String roleName = user.getTipo() != null ? user.getTipo().name().toLowerCase() : "comum";
						List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

						Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsuario(), null,
								authorities);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					});
				}
			} catch (JWTVerificationException ex)
			{
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}

	private String recoveryToken(HttpServletRequest request)
	{
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
		{
			return authorizationHeader.substring(7).trim();
		}
		return null;
	}
}
