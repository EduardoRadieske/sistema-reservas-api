package com.radieske.reservasapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginData
{
	@NotBlank(message = "O campo 'usuario' é obrigatório")
	private String usuario;

	@NotBlank(message = "O campo 'senha' é obrigatório")
	private String senha;
}
