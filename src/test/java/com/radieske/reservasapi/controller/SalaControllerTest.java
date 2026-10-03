package com.radieske.reservasapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radieske.reservasapi.dto.SalaDTO;
import com.radieske.reservasapi.dto.SalaRequestDTO;
import com.radieske.reservasapi.service.SalaService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "admin")
class SalaControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private SalaService salaService;

	@Test
	@DisplayName("Deve listar salas como DTOs de resposta")
	void shouldReturnSalasAsResponseDtos() throws Exception
	{
		when(salaService.findAll()).thenReturn(List.of(response()));

		mockMvc.perform(get("/salas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].idSala").value(1))
				.andExpect(jsonPath("$[0].idFechadura").value(5));
	}

	@Test
	@DisplayName("Deve retornar uma sala como DTO de resposta")
	void shouldReturnSalaById() throws Exception
	{
		when(salaService.findById(1)).thenReturn(Optional.of(response()));

		mockMvc.perform(get("/salas/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idSala").value(1))
				.andExpect(jsonPath("$.nome").value("Sala A"));
	}

	@Test
	@DisplayName("Deve criar sala a partir de DTO validado")
	void shouldCreateSalaFromRequestDto() throws Exception
	{
		when(salaService.save(any(SalaRequestDTO.class))).thenReturn(response());

		mockMvc.perform(post("/salas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.idSala").value(1))
				.andExpect(jsonPath("$.nome").value("Sala A"));

		verify(salaService).save(request());
	}

	@Test
	@DisplayName("Deve atualizar sala a partir de DTO validado")
	void shouldUpdateSalaFromRequestDto() throws Exception
	{
		when(salaService.update(any(SalaRequestDTO.class))).thenReturn(response());

		mockMvc.perform(put("/salas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idSala").value(1));

		verify(salaService).update(request());
	}

	@Test
	@DisplayName("Deve rejeitar sala sem nome")
	void shouldRejectSalaWithoutName() throws Exception
	{
		SalaRequestDTO invalidRequest = new SalaRequestDTO(1, " ", null, 5, null);

		mockMvc.perform(post("/salas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("O campo 'nome' é obrigatório"));

		verify(salaService, never()).save(any(SalaRequestDTO.class));
	}

	@Test
	@DisplayName("Deve excluir sala")
	void shouldDeleteSala() throws Exception
	{
		mockMvc.perform(delete("/salas/1"))
				.andExpect(status().isOk());

		verify(salaService).deleteById(1);
	}

	private SalaRequestDTO request()
	{
		return new SalaRequestDTO(1, "Sala A", "Descricao", 5, null);
	}

	private SalaDTO response()
	{
		return new SalaDTO(1, "Sala A", "Descricao", 5);
	}
}
