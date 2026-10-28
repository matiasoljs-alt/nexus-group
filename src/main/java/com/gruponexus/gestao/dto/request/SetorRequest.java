package com.gruponexus.gestao.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SetorRequest {

    @NotBlank(message = "O nome do setor é obrigatório")
    private String nome;

    private String sigla;
    private String descricao;
    private Boolean ativo = true;

    public SetorRequest() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
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
