package com.gruponexus.gestao.controller;

import com.gruponexus.gestao.dto.request.ComunicadoRequest;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.entity.Comunicado;
import com.gruponexus.gestao.service.ComunicadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comunicados")
public class ComunicadoController {

    private final ComunicadoService comunicadoService;

    public ComunicadoController(ComunicadoService comunicadoService) {
        this.comunicadoService = comunicadoService;
    }

    @GetMapping
    public ResponseEntity<List<Comunicado>> listar(@RequestParam(required = false, defaultValue = "true") boolean apenasAtivos) {
        List<Comunicado> list = apenasAtivos ? comunicadoService.listarAtivos() : comunicadoService.listarTodos();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/recentes")
    public ResponseEntity<List<Comunicado>> listarRecentes() {
        return ResponseEntity.ok(comunicadoService.listarRecentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comunicado> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comunicadoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Comunicado> criar(@Valid @RequestBody ComunicadoRequest request) {
        Comunicado comunicado = comunicadoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comunicado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Comunicado> atualizar(@PathVariable Long id, @Valid @RequestBody ComunicadoRequest request) {
        Comunicado comunicado = comunicadoService.atualizar(id, request);
        return ResponseEntity.ok(comunicado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        comunicadoService.deletar(id);
        return ResponseEntity.ok(MessageResponse.ok("Comunicado excluído com sucesso"));
    }
}
