package com.radieske.reservasapi.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.radieske.reservasapi.enums.StatusIntegracao;
import com.radieske.reservasapi.model.Fechadura;
import com.radieske.reservasapi.model.Provedor;
import com.radieske.reservasapi.model.Reserva;
import com.radieske.reservasapi.model.SenhaTemporaria;
import com.radieske.reservasapi.repository.SenhaTemporariaRepository;

@Component
public class ProcessIntegration
{
	private static final Logger log = LoggerFactory.getLogger(ProcessIntegration.class);
	private static final int MAX_MESSAGE_LENGTH = 1000;

	@Autowired
	private SenhaTemporariaRepository senhaRepository;

	@Async
	public void processAutomation(Integer idSenha)
	{
		try
		{
			SenhaTemporaria senha = senhaRepository.findForIntegrationById(idSenha)
					.orElse(null);
			if (senha == null)
			{
				log.error("Senha temporária {} não encontrada para provisionamento.", idSenha);
				return;
			}

			Reserva reserva = senha.getReserva();
			Fechadura fechadura = reserva.getSala().getFechadura();
			Provedor provedor = fechadura.getProvedor();

			if (provedor.getSecret() == null || provedor.getClientId() == null)
			{
				persistResult(idSenha, StatusIntegracao.AVISO,
						"Integração não executada: credenciais do provedor não configuradas.");
				log.warn("Integração da senha temporária {} não executada: credenciais ausentes.", idSenha);
				return;
			}

			IntegrationFactory.getIntegration(provedor).registerPassword(senha);
			persistResult(idSenha, StatusIntegracao.SUCESSO,
					"Senha provisionada na fechadura com sucesso.");
		}
		catch (Exception ex)
		{
			String detalhe = ex.getMessage();
			String mensagem = ex.getClass().getSimpleName()
					+ (detalhe == null || detalhe.isBlank() ? "" : ": " + detalhe);
			persistResult(idSenha, StatusIntegracao.ERRO, mensagem);
			log.error("Falha ao provisionar a senha temporária {}: {}", idSenha, mensagem, ex);
		}
	}

	private void persistResult(Integer idSenha, StatusIntegracao status, String mensagem)
	{
		String mensagemLimitada = mensagem.length() > MAX_MESSAGE_LENGTH
				? mensagem.substring(0, MAX_MESSAGE_LENGTH)
				: mensagem;
		int atualizados = senhaRepository.updateIntegrationResult(idSenha, status, mensagemLimitada);
		if (atualizados == 0)
		{
			log.error("Não foi possível persistir o resultado da integração da senha temporária {}.", idSenha);
		}
	}
}
