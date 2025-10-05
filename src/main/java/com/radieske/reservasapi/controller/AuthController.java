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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.repository.UsuarioRepository;
import com.radieske.reservasapi.security.JwtTokenProvider;

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
    public ResponseEntity<?> login(@RequestParam String usuario, @RequestParam String senha) {
		Usuario user = userRepository.findByUsuario(usuario).get(); 
		
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario, senha));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
