package com.radieske.reservasapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "fechaduras")
public class Fechadura
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fechadura")
    private Integer idFechadura;

    @ManyToOne
    @JoinColumn(name = "id_provedor", nullable = false)
    private Provedor provedor;

    @Column(name = "chave_dispositivo", length = 500, nullable = false)
    private String chaveDispositivo;
}
