package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.request.FuncionarioRequest;
import com.gruponexus.gestao.dto.response.FuncionarioResponse;
import com.gruponexus.gestao.entity.*;
import com.gruponexus.gestao.exception.BusinessException;
import com.gruponexus.gestao.exception.ResourceNotFoundException;
import com.gruponexus.gestao.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;
    private final CargoRepository cargoRepository;
    private final EscalaRepository escalaRepository;
    private final BeneficioRepository beneficioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository funcionarioRepository,
                              SetorRepository setorRepository,
                              CargoRepository cargoRepository,
                              EscalaRepository escalaRepository,
                              BeneficioRepository beneficioRepository,
                              UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.setorRepository = setorRepository;
        this.cargoRepository = cargoRepository;
        this.escalaRepository = escalaRepository;
        this.beneficioRepository = beneficioRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponse> listar(String busca, Long setorId, Long cargoId, String status) {
        if (busca != null && busca.trim().isEmpty()) {
            busca = null;
        }
        if (status != null && (status.trim().isEmpty() || "TODOS".equalsIgnoreCase(status))) {
            status = null;
        }

        List<Funcionario> funcionarios = funcionarioRepository.buscarComFiltros(busca, setorId, cargoId, status);
        return funcionarios.stream().map(FuncionarioResponse::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuncionarioResponse buscarPorId(Long id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));
        return FuncionarioResponse.fromEntity(f);
    }

    @Transactional(readOnly = true)
    public FuncionarioResponse buscarPorMatricula(String matricula) {
        Funcionario f = funcionarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com matrícula: " + matricula));
        return FuncionarioResponse.fromEntity(f);
    }

    @Transactional(readOnly = true)
    public FuncionarioResponse buscarPorUsuarioId(Long usuarioId) {
        Funcionario f = funcionarioRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de funcionário não encontrado para o usuário informado"));
        return FuncionarioResponse.fromEntity(f);
    }

    @Transactional(readOnly = true)
    public FuncionarioResponse buscarPorUsername(String username) {
        Funcionario f = funcionarioRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de funcionário não encontrado para: " + username));
        return FuncionarioResponse.fromEntity(f);
    }

    @Transactional
    public FuncionarioResponse criar(FuncionarioRequest request) {
        if (funcionarioRepository.existsByMatricula(request.getMatricula())) {
            throw new BusinessException("Já existe um colaborador com a matrícula: " + request.getMatricula());
        }

        if (funcionarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um colaborador com o e-mail: " + request.getEmail());
        }

        if (request.getCpf() != null && !request.getCpf().trim().isEmpty() && funcionarioRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Já existe um colaborador cadastrado com este CPF.");
        }

        Setor setor = setorRepository.findById(request.getSetorId())
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));
        Cargo cargo = cargoRepository.findById(request.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));

        Escala escala = null;
        if (request.getEscalaId() != null) {
            escala = escalaRepository.findById(request.getEscalaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Escala não encontrada"));
        }

        Funcionario f = new Funcionario();
        f.setMatricula(request.getMatricula());
        f.setNome(request.getNome());
        f.setEmail(request.getEmail());
        f.setTelefone(request.getTelefone());
        f.setCpf(request.getCpf());
        f.setDataNascimento(request.getDataNascimento());
        f.setDataAdmissao(request.getDataAdmissao());
        f.setStatus(request.getStatus() != null ? request.getStatus() : "ATIVO");
        f.setTipoContrato(request.getTipoContrato() != null ? request.getTipoContrato() : "CLT");
        f.setObservacoes(request.getObservacoes());
        f.setSetor(setor);
        f.setCargo(cargo);
        f.setEscala(escala);

        // Vínculo de Benefícios
        if (request.getBeneficioIds() != null && !request.getBeneficioIds().isEmpty()) {
            List<Beneficio> beneficios = beneficioRepository.findAllById(request.getBeneficioIds());
            f.setBeneficios(new HashSet<>(beneficios));
        }

        // Criação de usuário para o funcionário (login no sistema)
        if (Boolean.TRUE.equals(request.getCriarUsuarioAcesso())) {
            String senha = (request.getSenhaInicial() != null && !request.getSenhaInicial().trim().isEmpty())
                    ? request.getSenhaInicial()
                    : "nexus123";

            Role role = request.getRoleUsuario() != null ? request.getRoleUsuario() : Role.ROLE_FUNCIONARIO;

            // Se o username já existir, verifica
            if (usuarioRepository.existsByUsername(request.getMatricula())) {
                throw new BusinessException("O login de usuário '" + request.getMatricula() + "' já está em uso.");
            }

            Usuario usuario = new Usuario(
                    request.getMatricula(),
                    request.getEmail(),
                    passwordEncoder.encode(senha),
                    role
            );
            usuario.setAtivo(!"INATIVO".equalsIgnoreCase(f.getStatus()));
            usuario = usuarioRepository.save(usuario);
            f.setUsuario(usuario);
        }

        Funcionario salvo = funcionarioRepository.save(f);
        return FuncionarioResponse.fromEntity(salvo);
    }

    @Transactional
    public FuncionarioResponse atualizar(Long id, FuncionarioRequest request) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        // Valida duplicidade de matrícula caso alterada
        if (!f.getMatricula().equalsIgnoreCase(request.getMatricula()) &&
                funcionarioRepository.existsByMatricula(request.getMatricula())) {
            throw new BusinessException("A matrícula informada já está em uso por outro funcionário.");
        }

        // Valida duplicidade de e-mail caso alterado
        if (!f.getEmail().equalsIgnoreCase(request.getEmail()) &&
                funcionarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("O e-mail informado já está em uso por outro funcionário.");
        }

        Setor setor = setorRepository.findById(request.getSetorId())
                .orElseThrow(() -> new ResourceNotFoundException("Setor não encontrado"));
        Cargo cargo = cargoRepository.findById(request.getCargoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));

        Escala escala = null;
        if (request.getEscalaId() != null) {
            escala = escalaRepository.findById(request.getEscalaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Escala não encontrada"));
        }

        f.setMatricula(request.getMatricula());
        f.setNome(request.getNome());
        f.setEmail(request.getEmail());
        f.setTelefone(request.getTelefone());
        f.setCpf(request.getCpf());
        f.setDataNascimento(request.getDataNascimento());
        f.setDataAdmissao(request.getDataAdmissao());
        f.setStatus(request.getStatus() != null ? request.getStatus() : f.getStatus());
        f.setTipoContrato(request.getTipoContrato() != null ? request.getTipoContrato() : f.getTipoContrato());
        f.setObservacoes(request.getObservacoes());
        f.setSetor(setor);
        f.setCargo(cargo);
        f.setEscala(escala);

        // Atualizar benefícios
        if (request.getBeneficioIds() != null) {
            List<Beneficio> beneficios = beneficioRepository.findAllById(request.getBeneficioIds());
            f.setBeneficios(new HashSet<>(beneficios));
        }

        // Sincroniza usuário de acesso
        if (f.getUsuario() != null) {
            f.getUsuario().setEmail(f.getEmail());
            f.getUsuario().setAtivo(!"INATIVO".equalsIgnoreCase(f.getStatus()));
            if (request.getRoleUsuario() != null) {
                f.getUsuario().setRole(request.getRoleUsuario());
            }
            usuarioRepository.save(f.getUsuario());
        }

        Funcionario atualizado = funcionarioRepository.save(f);
        return FuncionarioResponse.fromEntity(atualizado);
    }

    @Transactional
    public FuncionarioResponse alternarStatus(Long id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        if ("ATIVO".equalsIgnoreCase(f.getStatus())) {
            f.setStatus("INATIVO");
            if (f.getUsuario() != null) {
                f.getUsuario().setAtivo(false);
            }
        } else {
            f.setStatus("ATIVO");
            if (f.getUsuario() != null) {
                f.getUsuario().setAtivo(true);
            }
        }

        Funcionario atualizado = funcionarioRepository.save(f);
        return FuncionarioResponse.fromEntity(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com ID: " + id));

        Usuario usuario = f.getUsuario();
        f.setUsuario(null);
        f.getBeneficios().clear();
        funcionarioRepository.delete(f);

        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }
    }
}
