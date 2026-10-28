package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.request.UsuarioUpdateRequest;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.entity.Funcionario;
import com.gruponexus.gestao.entity.Usuario;
import com.gruponexus.gestao.exception.BusinessException;
import com.gruponexus.gestao.exception.ResourceNotFoundException;
import com.gruponexus.gestao.repository.FuncionarioRepository;
import com.gruponexus.gestao.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          FuncionarioRepository funcionarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Usuario u : usuarios) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("role", u.getRole().name());
            map.put("roleDescricao", u.getRole().getDescricao());
            map.put("ativo", u.getAtivo());
            map.put("criadoEm", u.getCriadoEm());
            map.put("ultimoAcesso", u.getUltimoAcesso());

            Funcionario f = funcionarioRepository.findByUsuarioId(u.getId()).orElse(null);
            if (f != null) {
                map.put("funcionarioId", f.getId());
                map.put("funcionarioNome", f.getNome());
                map.put("matricula", f.getMatricula());
                map.put("cargoNome", f.getCargo() != null ? f.getCargo().getNome() : "-");
                map.put("setorNome", f.getSetor() != null ? f.getSetor().getNome() : "-");
            } else {
                map.put("funcionarioNome", "Administrador do Sistema");
                map.put("matricula", u.getUsername());
            }

            result.add(map);
        }

        return result;
    }

    @Transactional
    public MessageResponse atualizarUsuario(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        if (request.getRole() != null) {
            usuario.setRole(request.getRole());
        }

        if (request.getAtivo() != null) {
            usuario.setAtivo(request.getAtivo());
        }

        if (request.getNovaSenha() != null && !request.getNovaSenha().trim().isEmpty()) {
            if (request.getNovaSenha().length() < 6) {
                throw new BusinessException("A nova senha deve possuir pelo menos 6 caracteres.");
            }
            usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        }

        usuarioRepository.save(usuario);
        return MessageResponse.ok("Dados do usuário atualizados com sucesso.");
    }
}
