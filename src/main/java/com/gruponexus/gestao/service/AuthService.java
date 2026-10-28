package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.request.AlterarSenhaRequest;
import com.gruponexus.gestao.dto.request.LoginRequest;
import com.gruponexus.gestao.dto.response.JwtResponse;
import com.gruponexus.gestao.dto.response.MessageResponse;
import com.gruponexus.gestao.entity.Funcionario;
import com.gruponexus.gestao.entity.Usuario;
import com.gruponexus.gestao.exception.BusinessException;
import com.gruponexus.gestao.exception.ResourceNotFoundException;
import com.gruponexus.gestao.repository.FuncionarioRepository;
import com.gruponexus.gestao.repository.UsuarioRepository;
import com.gruponexus.gestao.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       FuncionarioRepository funcionarioRepository,
                       JwtTokenProvider tokenProvider,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public JwtResponse autenticar(LoginRequest loginRequest) {
        // Encontra o usuário pelo username (matrícula) ou pelo e-mail
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(loginRequest.getLogin(), loginRequest.getLogin())
                .orElseThrow(() -> new BadCredentialsException("Usuário ou e-mail não encontrado no sistema"));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new BusinessException("Esta conta de acesso está inativa. Entre em contato com o RH ou Administrador.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getUsername(), loginRequest.getSenha())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Senha incorreta. Verifique suas credenciais.");
        }

        // Atualiza último acesso
        usuario.setUltimoAcesso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // Busca dados do funcionário vinculado (se houver)
        Funcionario funcionario = funcionarioRepository.findByUsuarioId(usuario.getId()).orElse(null);
        String nome = funcionario != null ? funcionario.getNome() : usuario.getUsername();
        String matricula = funcionario != null ? funcionario.getMatricula() : usuario.getUsername();

        String token = tokenProvider.gerarToken(usuario.getUsername(), usuario.getRole().name(), nome, matricula);

        return new JwtResponse(
                token,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRole().name(),
                matricula,
                nome
        );
    }

    @Transactional
    public MessageResponse recuperarSenha(String identificador) {
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(identificador, identificador)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma conta encontrada com o e-mail ou matrícula: " + identificador));

        // Em ambiente empresarial/acadêmico simulamos o envio do e-mail com instruções e código de redefinição
        return MessageResponse.ok("Instruções para redefinição de acesso foram enviadas para o e-mail: " + usuario.getEmail());
    }

    @Transactional
    public MessageResponse alterarSenha(String username, AlterarSenhaRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + username));

        if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getSenha())) {
            throw new BusinessException("A senha atual informada está incorreta.");
        }

        usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        usuarioRepository.save(usuario);

        return MessageResponse.ok("Senha alterada com sucesso.");
    }

    @Transactional(readOnly = true)
    public Usuario obterUsuarioAtual(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário atual não encontrado: " + username));
    }
}
