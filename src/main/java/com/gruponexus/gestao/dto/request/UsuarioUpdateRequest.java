package com.gruponexus.gestao.dto.request;

import com.gruponexus.gestao.entity.Role;
import jakarta.validation.constraints.NotNull;

public class UsuarioUpdateRequest {

    @NotNull(message = "A permissão (role) é obrigatória")
    private Role role;

    private Boolean ativo;
    private String novaSenha;

    public UsuarioUpdateRequest() {}

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}
