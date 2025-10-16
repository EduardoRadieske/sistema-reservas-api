package com.radieske.reservasapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.security.JwtTokenProvider;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
    public ResponseEntity<?> login(@RequestBody @Valid LoginData loginData) {
        Usuario user = userRepository.findByUsuario(loginData.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginData.getUsuario(), loginData.getSenha())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
	
	@Data
    public static class LoginData {
        @NotBlank(message = "O campo 'usuario' é obrigatório")
        private String usuario;

        @NotBlank(message = "O campo 'senha' é obrigatório")
        private String senha;
    }
}
