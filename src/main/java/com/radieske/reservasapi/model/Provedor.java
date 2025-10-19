package com.radieske.reservasapi.model;

import com.radieske.reservasapi.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "provedor")
public class Provedor
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_provedor")
	private Integer idProvedor;

	@Column(name = "provedor", length = 10)
	private String provedor;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status = Status.S;

	@Column(name = "client_id", length = 500)
	private String clientId;

	@Column(name = "secret", length = 500)
	private String secret;
}
