package com.gruponexus.gestao.config;

import com.gruponexus.gestao.entity.*;
import com.gruponexus.gestao.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final SetorRepository setorRepository;
    private final CargoRepository cargoRepository;
    private final EscalaRepository escalaRepository;
    private final BeneficioRepository beneficioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ComunicadoRepository comunicadoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           SetorRepository setorRepository,
                           CargoRepository cargoRepository,
                           EscalaRepository escalaRepository,
                           BeneficioRepository beneficioRepository,
                           FuncionarioRepository funcionarioRepository,
                           ComunicadoRepository comunicadoRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.setorRepository = setorRepository;
        this.cargoRepository = cargoRepository;
        this.escalaRepository = escalaRepository;
        this.beneficioRepository = beneficioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.comunicadoRepository = comunicadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Se já existem setores ou usuários, não reinsere para evitar duplicações
        if (setorRepository.count() > 0 && usuarioRepository.count() > 0) {
            return;
        }

        // ==========================================
        // 1. SETORES
        // ==========================================
        Setor setorTi = setorRepository.save(new Setor("Tecnologia da Informação", "TI", "Sistemas corporativos, infraestrutura de nuvem e segurança digital."));
        Setor setorEng = setorRepository.save(new Setor("Engenharia & Obras", "ENG", "Planejamento, execução de empreendimentos e inovação estrutural."));
        Setor setorRh = setorRepository.save(new Setor("Recursos Humanos", "RH", "Desenvolvimento humano, recrutamento, DP e cultura organizacional."));
        Setor setorFin = setorRepository.save(new Setor("Financeiro & Controladoria", "FIN", "Gestão financeira, contabilidade, auditoria e controladoria."));
        Setor setorOp = setorRepository.save(new Setor("Operações & Logística", "OP", "Coordenação operacional, cadeia de suprimentos e suporte de campo."));
        Setor setorCom = setorRepository.save(new Setor("Comercial & Novos Negócios", "COM", "Expansão de mercado, contratos estratégicos e parcerias."));

        // ==========================================
        // 2. CARGOS
        // ==========================================
        Cargo cargoDev = cargoRepository.save(new Cargo("Desenvolvedor Full Stack", "Pleno", new BigDecimal("7800.00"), "Desenvolvimento e manutenção dos sistemas da empresa.", setorTi));
        Cargo cargoArquiteto = cargoRepository.save(new Cargo("Arquiteto de Software", "Sênior", new BigDecimal("14500.00"), "Arquitetura e liderança técnica das plataformas digitais.", setorTi));
        Cargo cargoEngCivil = cargoRepository.save(new Cargo("Engenheira Civil", "Pleno", new BigDecimal("9200.00"), "Supervisão e execução técnica de projetos estruturais.", setorEng));
        Cargo cargoTecSeg = cargoRepository.save(new Cargo("Técnico de Segurança do Trabalho", "Pleno", new BigDecimal("4800.00"), "Fiscalização de normas de segurança e saúde ocupacional.", setorEng));
        Cargo cargoEspRh = cargoRepository.save(new Cargo("Especialista de Recursos Humanos", "Sênior", new BigDecimal("8600.00"), "Gestão de pessoas, clima corporativo e departamento pessoal.", setorRh));
        Cargo cargoAnalistaFin = cargoRepository.save(new Cargo("Analista Financeiro", "Sênior", new BigDecimal("7400.00"), "Controladoria, fluxo de caixa e relatórios contábeis.", setorFin));
        Cargo cargoCoordOp = cargoRepository.save(new Cargo("Coordenador de Operações", "Coordenação", new BigDecimal("10500.00"), "Liderança das equipes de logística e suprimentos.", setorOp));
        Cargo cargoExecCom = cargoRepository.save(new Cargo("Executiva de Novos Negócios", "Especialista", new BigDecimal("11000.00"), "Gestão de grandes contas comerciais do Grupo Nexus.", setorCom));

        // ==========================================
        // 3. ESCALAS DE TRABALHO
        // ==========================================
        Escala escalaComercial = escalaRepository.save(new Escala(
                "Comercial Padrão 5x2",
                "5x2",
                "08:00",
                "17:00",
                "01:00 (12:00 às 13:00)",
                "Segunda a Sexta-feira",
                "Sábado e Domingo",
                "Horário administrativo padrão de 40 horas semanais."
        ));

        Escala escalaHibrida = escalaRepository.save(new Escala(
                "Jornada Flexível Híbrida 5x2",
                "5x2",
                "09:00",
                "18:00",
                "01:00 (12:30 às 13:30)",
                "Segunda a Sexta-feira (3 dias remoto, 2 presenciais)",
                "Sábado e Domingo",
                "Escala híbrida para squads de tecnologia e inovação."
        ));

        Escala escalaOperacional = escalaRepository.save(new Escala(
                "Operacional Diurno 6x1",
                "6x1",
                "06:00",
                "14:20",
                "01:00 (10:00 às 11:00)",
                "Segunda a Sábado",
                "Domingo",
                "Escala de campo e suporte a canteiros de obras."
        ));

        Escala escalaPlantao = escalaRepository.save(new Escala(
                "Plantão Técnico 12x36",
                "12x36",
                "07:00",
                "19:00",
                "01:00 rotativo",
                "Escala 12x36 (Dia sim, dia não)",
                "36 horas consecutivas de folga pós-plantão",
                "Escala para monitoramento crítico de infraestrutura."
        ));

        // ==========================================
        // 4. BENEFÍCIOS CORPORATIVOS
        // ==========================================
        Beneficio bSaude = beneficioRepository.save(new Beneficio(
                "Plano de Saúde Bradesco Top Nacional",
                "Saúde",
                "Cobertura médica ambulatorial, hospitalar e obstetrícia em quarto privativo sem coparticipação para titular.",
                new BigDecimal("850.00"),
                "Cartão de acesso digital pelo aplicativo Bradesco Saúde."
        ));

        Beneficio bOdonto = beneficioRepository.save(new Beneficio(
                "Plano Odontológico MetLife Premium",
                "Saúde",
                "Cobertura completa para consultas, limpezas, ortodontia e procedimentos odontológicos.",
                new BigDecimal("120.00"),
                "Rede credenciada em todo o território nacional."
        ));

        Beneficio bRefeicao = beneficioRepository.save(new Beneficio(
                "Vale Refeição e Alimentação Flash",
                "Alimentação",
                "Cartão de benefício multibenefícios com saldo mensal de R$ 924,00 para refeição e alimentação.",
                new BigDecimal("924.00"),
                "Crédito disponível todo dia 1º de cada mês."
        ));

        Beneficio bMobilidade = beneficioRepository.save(new Beneficio(
                "Auxílio Mobilidade e Combustível",
                "Mobilidade",
                "Auxílio flexível para transporte coletivo, aplicativos de mobilidade ou combustível.",
                new BigDecimal("380.00"),
                "Sem desconto de 6% em folha para colaboradores Grupo Nexus."
        ));

        Beneficio bGym = beneficioRepository.save(new Beneficio(
                "Wellhub / Gympass Silver",
                "Bem-Estar",
                "Acesso a mais de 9.000 academias parceiras, estúdios de pilates, yoga e apps de meditação.",
                new BigDecimal("180.00"),
                "Permite inclusão de até 3 dependentes."
        ));

        Beneficio bSeguro = beneficioRepository.save(new Beneficio(
                "Seguro de Vida Zurich Coletivo",
                "Segurança",
                "Apólice com cobertura para acidentes pessoais, invalidez e auxílio funeral estendido à família.",
                new BigDecimal("75.00"),
                "Apólice número 2026-NEXUS-8841."
        ));

        Beneficio bEducacao = beneficioRepository.save(new Beneficio(
                "Programa Nexus Academy - Bolsa Estudos",
                "Educação",
                "Reembolso de 50% em cursos de pós-graduação, certificações técnicas e idiomas.",
                new BigDecimal("600.00"),
                "Mediante aprovação do plano de desenvolvimento individual (PDI)."
        ));

        // ==========================================
        // 5. USUÁRIO ADMINISTRADOR GERAL
        // ==========================================
        Usuario adminUser = new Usuario(
                "admin",
                "admin@gruponexus.com.br",
                passwordEncoder.encode("admin123"),
                Role.ROLE_ADMIN
        );
        adminUser = usuarioRepository.save(adminUser);

        // ==========================================
        // 6. USUÁRIO E FUNCIONÁRIO: RH
        // ==========================================
        Usuario rhUser = new Usuario(
                "rh",
                "rh@gruponexus.com.br",
                passwordEncoder.encode("rh123"),
                Role.ROLE_RH
        );
        rhUser = usuarioRepository.save(rhUser);

        Funcionario fRh = new Funcionario(
                "NEX-1001",
                "Beatriz Silveira Santos",
                "beatriz.silveira@gruponexus.com.br",
                "(11) 98765-4321",
                "123.456.789-01",
                LocalDate.of(2021, 3, 15),
                cargoEspRh,
                setorRh,
                escalaComercial
        );
        fRh.setDataNascimento(LocalDate.of(1990, 8, 22));
        fRh.setUsuario(rhUser);
        fRh.getBeneficios().addAll(List.of(bSaude, bOdonto, bRefeicao, bGym, bSeguro));
        fRh.setObservacoes("Responsável pela coordenação de recrutamento e gestão interna.");
        funcionarioRepository.save(fRh);

        // ==========================================
        // 7. FUNCIONÁRIOS COM PERFIL DE COLABORADOR
        // ==========================================

        // Funcionário 1: Lucas Ferreira (TI - Desenvolvedor)
        Usuario userLucas = new Usuario(
                "NEX-1002",
                "lucas.ferreira@gruponexus.com.br",
                passwordEncoder.encode("user123"),
                Role.ROLE_FUNCIONARIO
        );
        userLucas = usuarioRepository.save(userLucas);

        Funcionario fLucas = new Funcionario(
                "NEX-1002",
                "Lucas Ferreira de Almeida",
                "lucas.ferreira@gruponexus.com.br",
                "(11) 97123-4567",
                "234.567.890-12",
                LocalDate.of(2022, 6, 1),
                cargoDev,
                setorTi,
                escalaHibrida
        );
        fLucas.setDataNascimento(LocalDate.of(1995, 4, 12));
        fLucas.setUsuario(userLucas);
        fLucas.getBeneficios().addAll(List.of(bSaude, bOdonto, bRefeicao, bMobilidade, bGym, bEducacao));
        fLucas.setObservacoes("Atua no time de desenvolvimento dos sistemas web e portais corporativos.");
        funcionarioRepository.save(fLucas);

        // Funcionário 2: Mariana Albuquerque (Engenharia)
        Usuario userMariana = new Usuario(
                "NEX-1003",
                "mariana.albuquerque@gruponexus.com.br",
                passwordEncoder.encode("user123"),
                Role.ROLE_FUNCIONARIO
        );
        userMariana = usuarioRepository.save(userMariana);

        Funcionario fMariana = new Funcionario(
                "NEX-1003",
                "Mariana Albuquerque Lima",
                "mariana.albuquerque@gruponexus.com.br",
                "(11) 98234-5678",
                "345.678.901-23",
                LocalDate.of(2020, 9, 10),
                cargoEngCivil,
                setorEng,
                escalaComercial
        );
        fMariana.setDataNascimento(LocalDate.of(1992, 11, 5));
        fMariana.setUsuario(userMariana);
        fMariana.getBeneficios().addAll(List.of(bSaude, bOdonto, bRefeicao, bSeguro, bMobilidade));
        funcionarioRepository.save(fMariana);

        // Funcionário 3: Rafael Vasconcelos (Operações)
        Usuario userRafael = new Usuario(
                "NEX-1004",
                "rafael.vasconcelos@gruponexus.com.br",
                passwordEncoder.encode("user123"),
                Role.ROLE_FUNCIONARIO
        );
        userRafael = usuarioRepository.save(userRafael);

        Funcionario fRafael = new Funcionario(
                "NEX-1004",
                "Rafael Vasconcelos Prado",
                "rafael.vasconcelos@gruponexus.com.br",
                "(11) 99345-6789",
                "456.789.012-34",
                LocalDate.of(2023, 1, 16),
                cargoCoordOp,
                setorOp,
                escalaComercial
        );
        fRafael.setDataNascimento(LocalDate.of(1988, 2, 28));
        fRafael.setUsuario(userRafael);
        fRafael.getBeneficios().addAll(List.of(bSaude, bOdonto, bRefeicao, bMobilidade, bSeguro));
        funcionarioRepository.save(fRafael);

        // Funcionário 4: Camila Ramos (Financeiro)
        Usuario userCamila = new Usuario(
                "NEX-1005",
                "camila.ramos@gruponexus.com.br",
                passwordEncoder.encode("user123"),
                Role.ROLE_FUNCIONARIO
        );
        userCamila = usuarioRepository.save(userCamila);

        Funcionario fCamila = new Funcionario(
                "NEX-1005",
                "Camila Ramos Fontes",
                "camila.ramos@gruponexus.com.br",
                "(11) 97456-7890",
                "567.890.123-45",
                LocalDate.of(2021, 11, 22),
                cargoAnalistaFin,
                setorFin,
                escalaComercial
        );
        fCamila.setDataNascimento(LocalDate.of(1994, 7, 19));
        fCamila.setUsuario(userCamila);
        fCamila.getBeneficios().addAll(List.of(bSaude, bRefeicao, bGym, bEducacao));
        funcionarioRepository.save(fCamila);

        // Funcionário 5: Gabriel Martins (Engenharia - Técnico de Campo)
        Usuario userGabriel = new Usuario(
                "NEX-1006",
                "gabriel.martins@gruponexus.com.br",
                passwordEncoder.encode("user123"),
                Role.ROLE_FUNCIONARIO
        );
        userGabriel = usuarioRepository.save(userGabriel);

        Funcionario fGabriel = new Funcionario(
                "NEX-1006",
                "Gabriel Martins Souza",
                "gabriel.martins@gruponexus.com.br",
                "(11) 98567-8901",
                "678.901.234-56",
                LocalDate.of(2024, 2, 5),
                cargoTecSeg,
                setorEng,
                escalaOperacional
        );
        fGabriel.setDataNascimento(LocalDate.of(1998, 9, 30));
        fGabriel.setUsuario(userGabriel);
        fGabriel.getBeneficios().addAll(List.of(bSaude, bRefeicao, bMobilidade, bSeguro));
        funcionarioRepository.save(fGabriel);

        // Funcionário 6: Sofia Mendes (Comercial)
        Usuario userSofia = new Usuario(
                "NEX-1007",
                "sofia.mendes@gruponexus.com.br",
                passwordEncoder.encode("user123"),
                Role.ROLE_FUNCIONARIO
        );
        userSofia = usuarioRepository.save(userSofia);

        Funcionario fSofia = new Funcionario(
                "NEX-1007",
                "Sofia Mendes Carvalho",
                "sofia.mendes@gruponexus.com.br",
                "(11) 97678-9012",
                "789.012.345-67",
                LocalDate.of(2023, 8, 14),
                cargoExecCom,
                setorCom,
                escalaComercial
        );
        fSofia.setDataNascimento(LocalDate.of(1991, 12, 14));
        fSofia.setUsuario(userSofia);
        fSofia.getBeneficios().addAll(List.of(bSaude, bOdonto, bRefeicao, bMobilidade, bGym));
        funcionarioRepository.save(fSofia);

        // ==========================================
        // 8. COMUNICADOS CORPORATIVOS
        // ==========================================
        Comunicado c1 = new Comunicado(
                "Planejamento Estratégico & Novos Empreendimentos 2026/2027",
                "Informamos a todos os colaboradores que o Grupo Nexus concluiu com êxito o plano de expansão para o próximo biênio. Estão previstos 4 novos grandes projetos de infraestrutura sustentável e ampliação dos investimentos em tecnologia corporativa.",
                "Institucional",
                true,
                "Diretoria Executiva"
        );
        c1.setDataPublicacao(LocalDateTime.now().minusDays(1).withHour(9).withMinute(30));
        comunicadoRepository.save(c1);

        Comunicado c2 = new Comunicado(
                "Campanha de Vacinação Corporativa - Nexus Saúde",
                "A campanha anual de imunização contra a gripe acontecerá na próxima quinta e sexta-feira no ambulatório do prédio sede. Para os colaboradores em escala remota, os vouchers para clínicas credenciadas já estão liberados no portal.",
                "Recursos Humanos",
                true,
                "Departamento Médico e RH"
        );
        c2.setDataPublicacao(LocalDateTime.now().minusDays(3).withHour(14).withMinute(15));
        comunicadoRepository.save(c2);

        Comunicado c3 = new Comunicado(
                "Atualização da Política de Trabalho Híbrido e Horários Flexíveis",
                "Reiteramos as diretrizes atualizadas para a escala híbrida: os alinhamentos presenciais devem respeitar a escala definida junto à liderança imediata. Solicitamos que todos mantenham seus horários de entrada e saída atualizados no sistema.",
                "Recursos Humanos",
                false,
                "Coordenação de Pessoas"
        );
        c3.setDataPublicacao(LocalDateTime.now().minusDays(5).withHour(10).withMinute(0));
        comunicadoRepository.save(c3);

        Comunicado c4 = new Comunicado(
                "Novo Programa de Incentivo à Certificação Técnica - Nexus Academy",
                "Estão abertas as inscrições para o segundo ciclo de bolsas de estudo e certificações técnicas internacionais. Colaboradores com mais de 6 meses de empresa podem submeter propostas de cursos alinhados às suas metas de desenvolvimento.",
                "Treinamento & Inovação",
                false,
                "Comitê Nexus Academy"
        );
        c4.setDataPublicacao(LocalDateTime.now().minusDays(8).withHour(16).withMinute(45));
        comunicadoRepository.save(c4);
    }
}
