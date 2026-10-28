package com.gruponexus.gestao.controller;

import com.gruponexus.gestao.dto.request.FuncionarioRequest;
import com.gruponexus.gestao.dto.response.FuncionarioResponse;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioResponse>> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) Long setorId,
            @RequestParam(required = false) Long cargoId,
            @RequestParam(required = false) String status) {
        List<FuncionarioResponse> list = funcionarioService.listar(busca, setorId, cargoId, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/me")
    public ResponseEntity<FuncionarioResponse> obterMeuPerfil(Authentication authentication) {
        FuncionarioResponse response = funcionarioService.buscarPorUsername(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarPorId(@PathVariable Long id) {
        FuncionarioResponse response = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<FuncionarioResponse> criar(@Valid @RequestBody FuncionarioRequest request) {
        FuncionarioResponse response = funcionarioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<FuncionarioResponse> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody FuncionarioRequest request) {
        FuncionarioResponse response = funcionarioService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<FuncionarioResponse> alternarStatus(@PathVariable Long id) {
        FuncionarioResponse response = funcionarioService.alternarStatus(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        funcionarioService.deletar(id);
        return ResponseEntity.ok(MessageResponse.ok("Funcionário excluído com sucesso"));
    }
}
