package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.request.CargoRequest;
import com.gruponexus.gestao.entity.Cargo;
import com.gruponexus.gestao.entity.Setor;
import com.gruponexus.gestao.exception.ResourceNotFoundException;
import com.gruponexus.gestao.repository.CargoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;
    private final SetorService setorService;

    public CargoService(CargoRepository cargoRepository, SetorService setorService) {
        this.cargoRepository = cargoRepository;
        this.setorService = setorService;
    }

    @Transactional(readOnly = true)
    public List<Cargo> listarTodos() {
        return cargoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cargo> listarAtivos() {
        return cargoRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Cargo> listarPorSetor(Long setorId) {
        return cargoRepository.findBySetorIdAndAtivoTrue(setorId);
    }

    @Transactional(readOnly = true)
    public Cargo buscarPorId(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + id));
    }

    @Transactional
    public Cargo criar(CargoRequest request) {
        Setor setor = setorService.buscarPorId(request.getSetorId());

        Cargo cargo = new Cargo(
                request.getNome(),
                request.getNivel(),
                request.getSalarioBase(),
                request.getDescricao(),
                setor
        );
        if (request.getAtivo() != null) {
            cargo.setAtivo(request.getAtivo());
        }

        return cargoRepository.save(cargo);
    }

    @Transactional
    public Cargo atualizar(Long id, CargoRequest request) {
        Cargo cargo = buscarPorId(id);
        Setor setor = setorService.buscarPorId(request.getSetorId());

        cargo.setNome(request.getNome());
        cargo.setNivel(request.getNivel());
        cargo.setSalarioBase(request.getSalarioBase());
        cargo.setDescricao(request.getDescricao());
        cargo.setSetor(setor);
        if (request.getAtivo() != null) {
            cargo.setAtivo(request.getAtivo());
        }

        return cargoRepository.save(cargo);
    }

    @Transactional
    public void deletar(Long id) {
        Cargo cargo = buscarPorId(id);
        cargoRepository.delete(cargo);
    }
}
