package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.request.BeneficioRequest;
import com.gruponexus.gestao.entity.Beneficio;
import com.gruponexus.gestao.exception.ResourceNotFoundException;
import com.gruponexus.gestao.repository.BeneficioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;

    public BeneficioService(BeneficioRepository beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
    }

    @Transactional(readOnly = true)
    public List<Beneficio> listarTodos() {
        return beneficioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Beneficio> listarAtivos() {
        return beneficioRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Beneficio buscarPorId(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benefício não encontrado com ID: " + id));
    }

    @Transactional
    public Beneficio criar(BeneficioRequest request) {
        Beneficio beneficio = new Beneficio(
                request.getNome(),
                request.getCategoria(),
                request.getDescricao(),
                request.getValor(),
                request.getInformacoesImportantes()
        );
        if (request.getAtivo() != null) {
            beneficio.setAtivo(request.getAtivo());
        }
        return beneficioRepository.save(beneficio);
    }

    @Transactional
    public Beneficio atualizar(Long id, BeneficioRequest request) {
        Beneficio beneficio = buscarPorId(id);

        beneficio.setNome(request.getNome());
        beneficio.setCategoria(request.getCategoria());
        beneficio.setDescricao(request.getDescricao());
        beneficio.setValor(request.getValor());
        beneficio.setInformacoesImportantes(request.getInformacoesImportantes());
        if (request.getAtivo() != null) {
            beneficio.setAtivo(request.getAtivo());
        }

        return beneficioRepository.save(beneficio);
    }

    @Transactional
    public void deletar(Long id) {
        Beneficio beneficio = buscarPorId(id);
        beneficioRepository.delete(beneficio);
    }
}
