package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.request.EscalaRequest;
import com.gruponexus.gestao.entity.Escala;
import com.gruponexus.gestao.exception.ResourceNotFoundException;
import com.gruponexus.gestao.repository.EscalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EscalaService {

    private final EscalaRepository escalaRepository;

    public EscalaService(EscalaRepository escalaRepository) {
        this.escalaRepository = escalaRepository;
    }

    @Transactional(readOnly = true)
    public List<Escala> listarTodas() {
        return escalaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Escala> listarAtivas() {
        return escalaRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Escala buscarPorId(Long id) {
        return escalaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escala não encontrada com ID: " + id));
    }

    @Transactional
    public Escala criar(EscalaRequest request) {
        Escala escala = new Escala(
                request.getNome(),
                request.getTipoEscala(),
                request.getHorarioEntrada(),
                request.getHorarioSaida(),
                request.getIntervalo(),
                request.getDiasTrabalho(),
                request.getFolgas(),
                request.getDescricao()
        );
        if (request.getAtivo() != null) {
            escala.setAtivo(request.getAtivo());
        }
        return escalaRepository.save(escala);
    }

    @Transactional
    public Escala atualizar(Long id, EscalaRequest request) {
        Escala escala = buscarPorId(id);

        escala.setNome(request.getNome());
        escala.setTipoEscala(request.getTipoEscala());
        escala.setHorarioEntrada(request.getHorarioEntrada());
        escala.setHorarioSaida(request.getHorarioSaida());
        escala.setIntervalo(request.getIntervalo());
        escala.setDiasTrabalho(request.getDiasTrabalho());
        escala.setFolgas(request.getFolgas());
        escala.setDescricao(request.getDescricao());
        if (request.getAtivo() != null) {
            escala.setAtivo(request.getAtivo());
        }

        return escalaRepository.save(escala);
    }

    @Transactional
    public void deletar(Long id) {
        Escala escala = buscarPorId(id);
        escalaRepository.delete(escala);
    }
}
