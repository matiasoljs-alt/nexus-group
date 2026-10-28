package com.gruponexus.gestao.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class BeneficioRequest {

    @NotBlank(message = "O nome do benefício é obrigatório")
    private String nome;

    private String categoria;
    private String descricao;
    private BigDecimal valor;
    private String informacoesImportantes;
    private Boolean ativo = true;

    public BeneficioRequest() {}

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
}
