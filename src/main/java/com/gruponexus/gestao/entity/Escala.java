package com.gruponexus.gestao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "escalas")
public class Escala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da escala é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 50)
    private String tipoEscala; // 5x2, 6x1, 12x36, Flexível

    @Column(length = 20)
    private String horarioEntrada; // ex: 08:00

    @Column(length = 20)
    private String horarioSaida; // ex: 17:00

    @Column(length = 50)
    private String intervalo; // ex: 01:00 (12:00 às 13:00)

    @Column(length = 100)
    private String diasTrabalho; // ex: Segunda a Sexta-feira

    @Column(length = 100)
    private String folgas; // ex: Sábado e Domingo

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Escala() {}

    public Escala(String nome, String tipoEscala, String horarioEntrada, String horarioSaida, 
                  String intervalo, String diasTrabalho, String folgas, String descricao) {
        this.nome = nome;
        this.tipoEscala = tipoEscala;
        this.horarioEntrada = horarioEntrada;
        this.horarioSaida = horarioSaida;
        this.intervalo = intervalo;
        this.diasTrabalho = diasTrabalho;
        this.folgas = folgas;
        this.descricao = descricao;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoEscala() {
        return tipoEscala;
    }

    public void setTipoEscala(String tipoEscala) {
        this.tipoEscala = tipoEscala;
    }

    public String getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(String horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public String getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(String horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public String getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }

    public String getDiasTrabalho() {
        return diasTrabalho;
    }

    public void setDiasTrabalho(String diasTrabalho) {
        this.diasTrabalho = diasTrabalho;
    }

    public String getFolgas() {
        return folgas;
    }

    public void setFolgas(String folgas) {
        this.folgas = folgas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
