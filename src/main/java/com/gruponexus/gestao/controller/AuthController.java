package com.gruponexus.gestao.controller;

import com.gruponexus.gestao.dto.request.AlterarSenhaRequest;
import com.gruponexus.gestao.dto.request.LoginRequest;
import com.gruponexus.gestao.dto.request.RecuperarSenhaRequest;
import com.gruponexus.gestao.dto.response.FuncionarioResponse;
import com.gruponexus.gestao.dto.response.JwtResponse;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.entity.Usuario;
import com.gruponexus.gestao.service.AuthService;
import com.gruponexus.gestao.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final FuncionarioService funcionarioService;

    public AuthController(AuthService authService, FuncionarioService funcionarioService) {
        this.authService = authService;
        this.funcionarioService = funcionarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.autenticar(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<MessageResponse> recuperarSenha(@Valid @RequestBody RecuperarSenhaRequest request) {
        MessageResponse response = authService.recuperarSenha(request.getIdentificador());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<MessageResponse> alterarSenha(Authentication authentication,
                                                        @Valid @RequestBody AlterarSenhaRequest request) {
        MessageResponse response = authService.alterarSenha(authentication.getName(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> obterUsuarioLogado(Authentication authentication) {
        Usuario usuario = authService.obterUsuarioAtual(authentication.getName());

        Map<String, Object> dados = new HashMap<>();
        dados.put("id", usuario.getId());
        dados.put("username", usuario.getUsername());
        dados.put("email", usuario.getEmail());
        dados.put("role", usuario.getRole().name());
        dados.put("roleDescricao", usuario.getRole().getDescricao());
        dados.put("ativo", usuario.getAtivo());
        dados.put("ultimoAcesso", usuario.getUltimoAcesso());

        try {
            FuncionarioResponse func = funcionarioService.buscarPorUsuarioId(usuario.getId());
            dados.put("funcionario", func);
        } catch (Exception e) {
            dados.put("funcionario", null);
        }

        return ResponseEntity.ok(dados);
    }
}
