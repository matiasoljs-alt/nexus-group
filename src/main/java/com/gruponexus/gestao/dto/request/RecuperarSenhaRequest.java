package com.gruponexus.gestao.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RecuperarSenhaRequest {

    @NotBlank(message = "Informe o e-mail ou matrícula cadastrado")
    private String identificador;

    public RecuperarSenhaRequest() {}

    public RecuperarSenhaRequest(String identificador) {
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
