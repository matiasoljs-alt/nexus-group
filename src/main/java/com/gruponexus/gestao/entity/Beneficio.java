package com.gruponexus.gestao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "beneficios")
public class Beneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do benefício é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 50)
    private String categoria; // Saúde, Alimentação, Mobilidade, Bem-Estar, Família

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor; // Valor mensal ou subsídio

    @Column(length = 255)
    private String informacoesImportantes;

    @Column(nullable = false)
    private Boolean ativo = true;

    @JsonIgnore
    @ManyToMany(mappedBy = "beneficios")
    private Set<Funcionario> funcionarios = new HashSet<>();

    public Beneficio() {}

    public Beneficio(String nome, String categoria, String descricao, BigDecimal valor, String informacoesImportantes) {
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.informacoesImportantes = informacoesImportantes;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getInformacoesImportantes() {
        return informacoesImportantes;
    }

    public void setInformacoesImportantes(String informacoesImportantes) {
        this.informacoesImportantes = informacoesImportantes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Set<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Set<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }
}
