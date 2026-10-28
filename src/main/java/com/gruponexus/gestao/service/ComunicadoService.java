package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.request.ComunicadoRequest;
import com.gruponexus.gestao.entity.Comunicado;
import com.gruponexus.gestao.exception.ResourceNotFoundException;
import com.gruponexus.gestao.repository.ComunicadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComunicadoService {

    private final ComunicadoRepository comunicadoRepository;

    public ComunicadoService(ComunicadoRepository comunicadoRepository) {
        this.comunicadoRepository = comunicadoRepository;
    }

    @Transactional(readOnly = true)
    public List<Comunicado> listarTodos() {
        return comunicadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Comunicado> listarAtivos() {
        return comunicadoRepository.findByAtivoTrueOrderByDataPublicacaoDesc();
    }

    @Transactional(readOnly = true)
    public List<Comunicado> listarRecentes() {
        return comunicadoRepository.findTop5ByAtivoTrueOrderByDataPublicacaoDesc();
    }

    @Transactional(readOnly = true)
    public Comunicado buscarPorId(Long id) {
        return comunicadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comunicado não encontrado com ID: " + id));
    }

    @Transactional
    public Comunicado criar(ComunicadoRequest request) {
        Comunicado comunicado = new Comunicado(
                request.getTitulo(),
                request.getConteudo(),
                request.getCategoria(),
                request.getImportante(),
                request.getAutor()
        );
        if (request.getAtivo() != null) {
            comunicado.setAtivo(request.getAtivo());
        }
        return comunicadoRepository.save(comunicado);
    }

    @Transactional
    public Comunicado atualizar(Long id, ComunicadoRequest request) {
        Comunicado comunicado = buscarPorId(id);

        comunicado.setTitulo(request.getTitulo());
        comunicado.setConteudo(request.getConteudo());
        comunicado.setCategoria(request.getCategoria());
        if (request.getImportante() != null) {
            comunicado.setImportante(request.getImportante());
        }
        if (request.getAutor() != null) {
            comunicado.setAutor(request.getAutor());
        }
        if (request.getAtivo() != null) {
            comunicado.setAtivo(request.getAtivo());
        }

        return comunicadoRepository.save(comunicado);
    }

    @Transactional
    public void deletar(Long id) {
        Comunicado comunicado = buscarPorId(id);
        comunicadoRepository.delete(comunicado);
    }
}
