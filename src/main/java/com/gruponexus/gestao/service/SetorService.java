package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.request.SetorRequest;
import com.gruponexus.gestao.entity.Setor;
import com.gruponexus.gestao.exception.BusinessException;
import com.gruponexus.gestao.exception.ResourceNotFoundException;
import com.gruponexus.gestao.repository.SetorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetorService {

    private final SetorRepository setorRepository;

    public SetorService(SetorRepository setorRepository) {
        this.setorRepository = setorRepository;
    }

    @Transactional(readOnly = true)
    public List<Setor> listarTodos() {
        return setorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Setor> listarAtivos() {
        return setorRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Setor buscarPorId(Long id) {
        return setorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado com ID: " + id));
    }

    @Transactional
    public Setor criar(SetorRequest request) {
        if (setorRepository.existsByNomeIgnoreCase(request.getNome())) {
            throw new BusinessException("Já existe um setor cadastrado com este nome: " + request.getNome());
        }

        Setor setor = new Setor(request.getNome(), request.getSigla(), request.getDescricao());
        if (request.getAtivo() != null) {
            setor.setAtivo(request.getAtivo());
        }
        return setorRepository.save(setor);
    }

    @Transactional
    public Setor atualizar(Long id, SetorRequest request) {
        Setor setor = buscarPorId(id);

        if (!setor.getNome().equalsIgnoreCase(request.getNome()) &&
                setorRepository.existsByNomeIgnoreCase(request.getNome())) {
            throw new BusinessException("Já existe outro setor cadastrado com o nome: " + request.getNome());
        }

        setor.setNome(request.getNome());
        setor.setSigla(request.getSigla());
        setor.setDescricao(request.getDescricao());
        if (request.getAtivo() != null) {
            setor.setAtivo(request.getAtivo());
        }

        return setorRepository.save(setor);
    }

    @Transactional
    public void deletar(Long id) {
        Setor setor = buscarPorId(id);
        if (!setor.getFuncionarios().isEmpty()) {
            throw new BusinessException("Não é possível excluir o setor pois existem funcionários vinculados a ele. Considere inativá-lo.");
        }
        setorRepository.delete(setor);
    }
}
