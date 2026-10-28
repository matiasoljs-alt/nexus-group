package com.gruponexus.gestao.controller;

import com.gruponexus.gestao.dto.request.CargoRequest;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.entity.Cargo;
import com.gruponexus.gestao.service.CargoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargos")
public class CargoController {

    private final CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping
    public ResponseEntity<List<Cargo>> listar(@RequestParam(required = false, defaultValue = "false") boolean apenasAtivos) {
        List<Cargo> list = apenasAtivos ? cargoService.listarAtivos() : cargoService.listarTodos();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/setor/{setorId}")
    public ResponseEntity<List<Cargo>> listarPorSetor(@PathVariable Long setorId) {
        return ResponseEntity.ok(cargoService.listarPorSetor(setorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cargoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Cargo> criar(@Valid @RequestBody CargoRequest request) {
        Cargo cargo = cargoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cargo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<Cargo> atualizar(@PathVariable Long id, @Valid @RequestBody CargoRequest request) {
        Cargo cargo = cargoService.atualizar(id, request);
        return ResponseEntity.ok(cargo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        cargoService.deletar(id);
        return ResponseEntity.ok(MessageResponse.ok("Cargo excluído com sucesso"));
    }
}
