package com.radieske.reservasapi.integration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.radieske.reservasapi.enums.StatusIntegracao;
import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.Sala;
import com.radieske.reservasapi.model.SenhaTemporaria;
import com.radieske.reservasapi.repository.SenhaTemporariaRepository;

@ExtendWith(MockitoExtension.class)
class ProcessIntegrationTest
{
	@Mock
	private SenhaTemporariaRepository senhaRepository;

	@Mock
	private SenhaTemporaria senha;

	@Mock
	private Reserva reserva;

	@Mock
	private Sala sala;

	@Mock
	private Fechadura fechadura;

	@Mock
	private Provedor provedor;

	@InjectMocks
	private ProcessIntegration processIntegration;

	@Test
	void shouldPersistWarningWhenProviderCredentialsAreMissing()
	{
		givenIntegrationContext();
		when(provedor.getClientId()).thenReturn(null);
		when(provedor.getSecret()).thenReturn("secret");

		processIntegration.processAutomation(12);

		verify(senhaRepository).updateIntegrationResult(
				12,
				StatusIntegracao.AVISO,
				"Integração não executada: credenciais do provedor não configuradas.");
	}

	@Test
	void shouldPersistExceptionDetailsWhenProcessingFails()
	{
		when(senhaRepository.findForIntegrationById(12)).thenReturn(Optional.of(senha));
		when(senha.getReserva()).thenThrow(new IllegalStateException("falha inesperada"));

		processIntegration.processAutomation(12);

		verify(senhaRepository).updateIntegrationResult(
				12,
				StatusIntegracao.ERRO,
				"IllegalStateException: falha inesperada");
	}

	private void givenIntegrationContext()
	{
		when(senhaRepository.findForIntegrationById(12)).thenReturn(Optional.of(senha));
		when(senha.getReserva()).thenReturn(reserva);
		when(reserva.getSala()).thenReturn(sala);
		when(sala.getFechadura()).thenReturn(fechadura);
		when(fechadura.getProvedor()).thenReturn(provedor);
	}
}
