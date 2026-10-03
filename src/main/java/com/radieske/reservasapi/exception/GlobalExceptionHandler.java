package com.radieske.reservasapi.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class, AuthenticationException.class })
	public ResponseEntity<Map<String, Object>> handleAuthenticationException(Exception ex)
	{
		Map<String, Object> error = new HashMap<>();
		error.put("error", "Usuário ou senha inválidos");
		error.put("status", HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex)
	{
		Map<String, Object> error = new HashMap<>();
		String message = ex.getBindingResult().getAllErrors().stream()
				.findFirst()
				.map(errorObj -> errorObj.getDefaultMessage())
				.orElse("Erro de validação nos campos informados.");

		error.put("error", message);
		error.put("status", HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(RuntimeException exception)
	{
		Map<String, Object> error = new HashMap<>();
		error.put("error", exception.getMessage());
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}