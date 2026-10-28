package com.gruponexus.gestao.controller;

import com.gruponexus.gestao.dto.request.BeneficioRequest;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.entity.Beneficio;
import com.gruponexus.gestao.service.BeneficioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficios")
public class BeneficioController {

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    public ResponseEntity<List<Beneficio>> listar(@RequestParam(required = false, defaultValue = "false") boolean apenasAtivos) {
        List<Beneficio> list = apenasAtivos ? beneficioService.listarAtivos() : beneficioService.listarTodos();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficio> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(beneficioService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Beneficio> criar(@Valid @RequestBody BeneficioRequest request) {
        Beneficio beneficio = beneficioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficio);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Beneficio> atualizar(@PathVariable Long id, @Valid @RequestBody BeneficioRequest request) {
        Beneficio beneficio = beneficioService.atualizar(id, request);
        return ResponseEntity.ok(beneficio);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        beneficioService.deletar(id);
        return ResponseEntity.ok(MessageResponse.ok("Benefício excluído com sucesso"));
    }
}
