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
import com.radieske.reservasapi.dto.FechaduraRequestDTO;
import com.radieske.reservasapi.dto.FechaduraResponseDTO;
import com.radieske.reservasapi.dto.ProvedorResponseDTO;
import com.radieske.reservasapi.enums.Status;
import com.radieske.reservasapi.service.FechaduraService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "admin")
class FechaduraControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private FechaduraService fechaduraService;

	@Test
	@DisplayName("Deve listar fechaduras como DTOs de resposta")
	void shouldReturnFechadurasAsResponseDtos() throws Exception
	{
		when(fechaduraService.findAll()).thenReturn(List.of(response()));

		mockMvc.perform(get("/fechaduras"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].idFechadura").value(1))
				.andExpect(jsonPath("$[0].provedor.idProvedor").value(4));
	}

	@Test
	@DisplayName("Deve retornar uma fechadura como DTO de resposta")
	void shouldReturnFechaduraById() throws Exception
	{
		when(fechaduraService.findById(1)).thenReturn(Optional.of(response()));

		mockMvc.perform(get("/fechaduras/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idFechadura").value(1))
				.andExpect(jsonPath("$.chaveDispositivo").value("device-key"));
	}

	@Test
	@DisplayName("Deve criar fechadura a partir de DTO validado")
	void shouldCreateFechaduraFromRequestDto() throws Exception
	{
		when(fechaduraService.save(any(FechaduraRequestDTO.class))).thenReturn(response());

		mockMvc.perform(post("/fechaduras")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.idFechadura").value(1))
				.andExpect(jsonPath("$.provedor.provedor").value("tuya"));

		verify(fechaduraService).save(request());
	}

	@Test
	@DisplayName("Deve atualizar fechadura a partir de DTO validado")
	void shouldUpdateFechaduraFromRequestDto() throws Exception
	{
		when(fechaduraService.update(any(FechaduraRequestDTO.class))).thenReturn(response());

		mockMvc.perform(put("/fechaduras")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idFechadura").value(1));

		verify(fechaduraService).update(request());
	}

	@Test
	@DisplayName("Deve rejeitar chave de dispositivo vazia")
	void shouldRejectBlankDeviceKey() throws Exception
	{
		FechaduraRequestDTO invalidRequest = new FechaduraRequestDTO(1, " ", 4, null);

		mockMvc.perform(post("/fechaduras")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("O campo 'chaveDispositivo' é obrigatório"));

		verify(fechaduraService, never()).save(any(FechaduraRequestDTO.class));
	}

	@Test
	@DisplayName("Deve excluir fechadura")
	void shouldDeleteFechadura() throws Exception
	{
		mockMvc.perform(delete("/fechaduras/1"))
				.andExpect(status().isOk());

		verify(fechaduraService).deleteById(1);
	}

	private FechaduraRequestDTO request()
	{
		return new FechaduraRequestDTO(1, "device-key", 4, null);
	}

	private FechaduraResponseDTO response()
	{
		ProvedorResponseDTO provedor = new ProvedorResponseDTO(4, "tuya", Status.S, "client", "secret");
		return new FechaduraResponseDTO(1, "device-key", provedor);
	}
}
