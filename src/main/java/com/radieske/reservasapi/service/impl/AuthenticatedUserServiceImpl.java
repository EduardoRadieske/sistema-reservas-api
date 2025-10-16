package com.radieske.reservasapi.service.impl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.service.AuthenticatedUserService;

@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService
{

	@Autowired
    private UsuarioRepository usuarioRepository;
	
	@Override
	public Usuario getAuthenticatedUser()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || Strings.isEmpty(auth.getName())) {
            throw new RuntimeException("Usuário não autenticado!");
        }

        return usuarioRepository.findByUsuario(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não localizado!"));
	}

}
