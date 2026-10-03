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
import com.radieske.reservasapi.dto.ProvedorRequestDTO;
import com.radieske.reservasapi.dto.ProvedorResponseDTO;
import com.radieske.reservasapi.enums.Status;
import com.radieske.reservasapi.service.ProvedorService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "admin")
class ProvedorControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ProvedorService provedorService;

	@Test
	@DisplayName("Deve listar provedores usando ProvedorResponseDTO")
	void shouldReturnProvedoresAsResponseDtos() throws Exception
	{
		when(provedorService.findAll()).thenReturn(List.of(response()));

		mockMvc.perform(get("/provedor"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].idProvedor").value(1))
				.andExpect(jsonPath("$[0].provedor").value("tuya"))
				.andExpect(jsonPath("$[0].clientId").value("client"));
	}

	@Test
	@DisplayName("Deve retornar um provedor como ProvedorResponseDTO")
	void shouldReturnProvedorById() throws Exception
	{
		when(provedorService.findById(1)).thenReturn(Optional.of(response()));

		mockMvc.perform(get("/provedor/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idProvedor").value(1))
				.andExpect(jsonPath("$.provedor").value("tuya"));
	}

	@Test
	@DisplayName("Deve retornar 404 quando o provedor não existir")
	void shouldReturn404WhenProvedorDoesNotExist() throws Exception
	{
		when(provedorService.findById(99)).thenReturn(Optional.empty());

		mockMvc.perform(get("/provedor/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve criar provedor com DTO validado e retornar 201")
	void shouldCreateProvedorFromRequestDto() throws Exception
	{
		when(provedorService.save(any(ProvedorRequestDTO.class))).thenReturn(response());

		mockMvc.perform(post("/provedor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.idProvedor").value(1))
				.andExpect(jsonPath("$.provedor").value("tuya"));

		verify(provedorService).save(request());
	}

	@Test
	@DisplayName("Deve atualizar provedor com DTO validado e retornar ProvedorResponseDTO")
	void shouldUpdateProvedorFromRequestDto() throws Exception
	{
		when(provedorService.update(any(ProvedorRequestDTO.class))).thenReturn(response());

		mockMvc.perform(put("/provedor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idProvedor").value(1))
				.andExpect(jsonPath("$.provedor").value("tuya"));

		verify(provedorService).update(request());
	}

	@Test
	@DisplayName("Deve rejeitar provedor em branco sem chamar o serviço")
	void shouldRejectBlankProvedor() throws Exception
	{
		ProvedorRequestDTO invalidRequest = new ProvedorRequestDTO(null, " ", Status.S, null, null);

		mockMvc.perform(post("/provedor")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("O campo 'provedor' é obrigatório"));

		verify(provedorService, never()).save(any(ProvedorRequestDTO.class));
	}

	@Test
	@DisplayName("Deve excluir provedor e retornar 200")
	void shouldDeleteProvedor() throws Exception
	{
		mockMvc.perform(delete("/provedor/1"))
				.andExpect(status().isOk());

		verify(provedorService).deleteById(1);
	}

	private ProvedorRequestDTO request()
	{
		return new ProvedorRequestDTO(1, "tuya", Status.S, "client", "secret");
	}

	private ProvedorResponseDTO response()
	{
		return new ProvedorResponseDTO(1, "tuya", Status.S, "client", "secret");
	}
}
