package com.gruponexus.gestao.controller;

import com.gruponexus.gestao.dto.request.EscalaRequest;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.entity.Escala;
import com.gruponexus.gestao.service.EscalaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escalas")
public class EscalaController {

    private final EscalaService escalaService;

    public EscalaController(EscalaService escalaService) {
        this.escalaService = escalaService;
    }

    @GetMapping
    public ResponseEntity<List<Escala>> listar(@RequestParam(required = false, defaultValue = "false") boolean apenasAtivas) {
        List<Escala> list = apenasAtivas ? escalaService.listarAtivas() : escalaService.listarTodas();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Escala> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(escalaService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Escala> criar(@Valid @RequestBody EscalaRequest request) {
        Escala escala = escalaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(escala);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Escala> atualizar(@PathVariable Long id, @Valid @RequestBody EscalaRequest request) {
        Escala escala = escalaService.atualizar(id, request);
        return ResponseEntity.ok(escala);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        escalaService.deletar(id);
        return ResponseEntity.ok(MessageResponse.ok("Escala excluída com sucesso"));
    }
}
