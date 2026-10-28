package com.gruponexus.gestao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comunicados")
public class Comunicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título do comunicado é obrigatório")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "O conteúdo é obrigatório")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(length = 50)
    private String categoria; // Geral, RH, Diretoria, Segurança, TI

    @Column(nullable = false)
    private Boolean importante = false;

    @Column(nullable = false)
    private LocalDateTime dataPublicacao;

    @Column(length = 100)
    private String autor;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Comunicado() {
        this.dataPublicacao = LocalDateTime.now();
    }

    public Comunicado(String titulo, String conteudo, String categoria, Boolean importante, String autor) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.categoria = categoria;
        this.importante = importante != null ? importante : false;
        this.autor = autor;
        this.dataPublicacao = LocalDateTime.now();
        this.ativo = true;
    }

    @PrePersist
    public void prePersist() {
        if (this.dataPublicacao == null) {
            this.dataPublicacao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getImportante() {
        return importante;
    }

    public void setImportante(Boolean importante) {
        this.importante = importante;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
