package com.gruponexus.gestao.controller;

import com.gruponexus.gestao.dto.request.SetorRequest;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.entity.Setor;
import com.gruponexus.gestao.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/setores")
public class SetorController {

    private final SetorService setorService;

    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    @GetMapping
    public ResponseEntity<List<Setor>> listar(@RequestParam(required = false, defaultValue = "false") boolean apenasAtivos) {
        List<Setor> list = apenasAtivos ? setorService.listarAtivos() : setorService.listarTodos();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Setor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(setorService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Setor> criar(@Valid @RequestBody SetorRequest request) {
        Setor setor = setorService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(setor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Setor> atualizar(@PathVariable Long id, @Valid @RequestBody SetorRequest request) {
        Setor setor = setorService.atualizar(id, request);
        return ResponseEntity.ok(setor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        setorService.deletar(id);
        return ResponseEntity.ok(MessageResponse.ok("Setor excluído com sucesso"));
    }
}
