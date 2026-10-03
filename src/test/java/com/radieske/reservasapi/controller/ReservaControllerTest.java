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

import java.time.LocalDateTime;
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
import com.radieske.reservasapi.dto.ReservaDTO;
import com.radieske.reservasapi.dto.ReservaRequestDTO;
import com.radieske.reservasapi.enums.Status;
import com.radieske.reservasapi.model.Usuario;
import com.radieske.reservasapi.service.AuthenticatedUserService;
import com.radieske.reservasapi.service.ReservaService;
import com.radieske.reservasapi.service.SenhaTemporariaService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ReservaControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ReservaService reservaService;

	@MockitoBean
	private AuthenticatedUserService authUserService;

	@MockitoBean
	private SenhaTemporariaService senhaService;

	@Test
	@DisplayName("Deve listar reservas ativas como DTOs de resposta")
	void shouldReturnActiveReservasAsDtos() throws Exception
	{
		when(authUserService.getAuthenticatedUser()).thenReturn(new Usuario());
		when(reservaService.findActive(any(Usuario.class))).thenReturn(List.of(response()));

		mockMvc.perform(get("/reservas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].idReserva").value(10))
				.andExpect(jsonPath("$[0].status").value("S"));
	}

	@Test
	@DisplayName("Deve retornar uma reserva como DTO de resposta")
	void shouldReturnReservaById() throws Exception
	{
		when(reservaService.findById(10)).thenReturn(Optional.of(response()));

		mockMvc.perform(get("/reservas/10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idReserva").value(10))
				.andExpect(jsonPath("$.dataReservaInicial").value("2026-10-02T14:00"));
	}

	@Test
	@DisplayName("Deve criar reserva a partir de DTO validado")
	void shouldCreateReservaFromRequestDto() throws Exception
	{
		when(reservaService.save(any(ReservaRequestDTO.class))).thenReturn(response());

		mockMvc.perform(post("/reservas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.idReserva").value(10));

		verify(reservaService).save(request());
	}

	@Test
	@DisplayName("Deve atualizar reserva a partir de DTO validado")
	void shouldUpdateReservaFromRequestDto() throws Exception
	{
		when(reservaService.update(any(ReservaRequestDTO.class))).thenReturn(response());

		mockMvc.perform(put("/reservas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.idReserva").value(10));

		verify(reservaService).update(request());
	}

	@Test
	@DisplayName("Deve rejeitar reserva sem data inicial")
	void shouldRejectReservaWithoutStartDate() throws Exception
	{
		ReservaRequestDTO invalidRequest = new ReservaRequestDTO(10, 1, null, 2, null, null,
				LocalDateTime.of(2026, 10, 2, 15, 0), Status.S);

		mockMvc.perform(post("/reservas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("O campo 'dataReservaInicial' é obrigatório"));

		verify(reservaService, never()).save(any(ReservaRequestDTO.class));
	}

	@Test
	@DisplayName("Deve excluir reserva")
	void shouldDeleteReserva() throws Exception
	{
		mockMvc.perform(delete("/reservas/10"))
				.andExpect(status().isOk());

		verify(reservaService).deleteById(10);
	}

	private ReservaRequestDTO request()
	{
		return new ReservaRequestDTO(10, 1, null, 2, null,
				LocalDateTime.of(2026, 10, 2, 14, 0),
				LocalDateTime.of(2026, 10, 2, 15, 0), Status.S);
	}

	private ReservaDTO response()
	{
		return new ReservaDTO(10, null, null, "2026-10-02T14:00", "2026-10-02T15:00", Status.S);
	}
}
