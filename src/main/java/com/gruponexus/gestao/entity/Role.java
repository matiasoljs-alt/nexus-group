package com.gruponexus.gestao.entity;

public enum Role {
    ROLE_ADMIN("Administrador"),
    ROLE_RH("Recursos Humanos"),
    ROLE_FUNCIONARIO("Funcionário");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
