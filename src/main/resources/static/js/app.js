/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * Controlador Geral da Aplicação (App Shell & Roteamento)
 */

const App = {
  currentPage: 'dashboard',

  init() {
    this.bindEvents();

    if (Auth.isAuthenticated()) {
      this.showApp();
      this.navigate('dashboard');
    } else {
      this.showLogin();
    }
  },

  bindEvents() {
    // Fechar modais ao clicar no backdrop
    document.querySelectorAll('.modal-overlay').forEach(modal => {
      modal.addEventListener('click', (e) => {
        if (e.target === modal) {
          this.closeModal(modal.id);
        }
      });
    });

    // Fechar sidebar mobile ao redimensionar tela
    window.addEventListener('resize', () => {
      if (window.innerWidth > 1024) {
        this.closeSidebar();
      }
    });
  },

  showLogin() {
    document.getElementById('login-view').style.display = 'flex';
    document.getElementById('app-view').style.display = 'none';
  },

  showApp() {
    document.getElementById('login-view').style.display = 'none';
    document.getElementById('app-view').style.display = 'flex';

    this.updateUserInterface();
  },

  updateUserInterface() {
    const user = Auth.getUser();
    if (!user) return;

    // Atualiza nomes no topo e na sidebar
    const nome = user.nome || user.username;
    const matricula = user.matricula || user.username;
    const role = user.role;

    const elUserName = document.getElementById('sidebar-user-name');
    if (elUserName) elUserName.innerText = nome;

    const elUserRole = document.getElementById('sidebar-user-role');
    if (elUserRole) {
      if (role === 'ROLE_ADMIN') elUserRole.innerText = 'Administrador Geral';
      else if (role === 'ROLE_RH') elUserRole.innerText = 'Recursos Humanos';
      else elUserRole.innerText = `Matrícula: ${matricula}`;
    }

    const elUserMono = document.getElementById('sidebar-user-monogram');
    if (elUserMono) elUserMono.innerText = this.getInitials(nome);

    const elTopRole = document.getElementById('topbar-role-badge');
    if (elTopRole) {
      let roleLabel = 'Funcionário';
      let roleClass = 'funcionario';

      if (role === 'ROLE_ADMIN') {
        roleLabel = 'Administrador';
        roleClass = 'admin';
      } else if (role === 'ROLE_RH') {
        roleLabel = 'Recursos Humanos';
        roleClass = 'rh';
      }

      elTopRole.className = `role-badge ${roleClass}`;
      elTopRole.innerText = roleLabel;
    }

    // Controle de visibilidade de itens de menu baseado em permissão (RBAC)
    const itensRhAdmin = document.querySelectorAll('.menu-rh-admin');
    itensRhAdmin.forEach(el => {
      el.style.display = Auth.canManage() ? 'flex' : 'none';
    });

    const itensAdmin = document.querySelectorAll('.menu-admin-only');
    itensAdmin.forEach(el => {
      el.style.display = Auth.isAdmin() ? 'flex' : 'none';
    });
  },

  navigate(page) {
    this.currentPage = page;
    this.closeSidebar();

    // Atualiza classes ativas na navegação
    document.querySelectorAll('.nav-item').forEach(item => {
      item.classList.remove('active');
      if (item.getAttribute('data-page') === page) {
        item.classList.add('active');
      }
    });

    // Atualiza título da Topbar
    const titleMap = {
      dashboard: { title: 'Dashboard', desc: 'Visão geral corporativa e indicadores de gestão' },
      perfil: { title: 'Meu Perfil', desc: 'Dados cadastrais, jornada de trabalho e benefícios' },
      funcionarios: { title: 'Gestão de Funcionários', desc: 'Cadastro, edição, filtros e histórico de colaboradores' },
      escalas: { title: 'Escalas de Trabalho', desc: 'Jornadas, horários semanais e escalas de revezamento' },
      beneficios: { title: 'Benefícios Corporativos', desc: 'Catálogo de planos de saúde, alimentação e incentivos' },
      comunicados: { title: 'Mural de Comunicados', desc: 'Avisos e comunicados institucionais aos colaboradores' },
      organizacao: { title: 'Estrutura Organizacional', desc: 'Gerenciamento de departamentos, cargos e níveis' },
      usuarios: { title: 'Gestão de Usuários', desc: 'Controle de acessos, permissões e redefinição de senhas' }
    };

    const info = titleMap[page] || { title: 'Nexus Gestão', desc: '' };
    document.getElementById('current-page-title').innerText = info.title;
    document.getElementById('current-page-desc').innerText = info.desc;

    // Roteia para a view correspondente
    switch (page) {
      case 'dashboard':
        DashboardView.render();
        break;
      case 'perfil':
        PerfilView.render();
        break;
      case 'funcionarios':
        FuncionariosView.render();
        break;
      case 'escalas':
        EscalasView.render();
        break;
      case 'beneficios':
        BeneficiosView.render();
        break;
      case 'comunicados':
        ComunicadosView.render();
        break;
      case 'organizacao':
        OrganizacaoView.render();
        break;
      case 'usuarios':
        UsuariosView.render();
        break;
      default:
        DashboardView.render();
    }
  },

  async submitLogin(event) {
    event.preventDefault();
    const loginInput = document.getElementById('login-usuario').value.trim();
    const senhaInput = document.getElementById('login-senha').value;
    const btnSubmit = document.getElementById('btn-login-submit');

    if (!loginInput || !senhaInput) {
      this.showToast('Preencha seu login/matrícula e senha.', 'warning');
      return;
    }

    btnSubmit.disabled = true;
    btnSubmit.innerHTML = `<span class="loading-spinner" style="width:18px; height:18px; border-width:2px;"></span> Acessando...`;

    try {
      await Auth.login(loginInput, senhaInput);
      this.showToast('Login realizado com sucesso! Bem-vindo(a).', 'success', 'Acesso Autorizado');
      this.showApp();
      this.navigate('dashboard');
    } catch (error) {
      this.showToast(error.message, 'error', 'Falha no Acesso');
    } finally {
      btnSubmit.disabled = false;
      btnSubmit.innerHTML = `Entrar no Portal`;
    }
  },

  preencherLogin(tipo) {
    const elLogin = document.getElementById('login-usuario');
    const elSenha = document.getElementById('login-senha');

    if (tipo === 'admin') {
      elLogin.value = 'admin';
      elSenha.value = 'admin123';
      this.showToast('Credenciais de Administrador preenchidas.', 'info');
    } else if (tipo === 'rh') {
      elLogin.value = 'rh';
      elSenha.value = 'rh123';
      this.showToast('Credenciais de Recursos Humanos preenchidas.', 'info');
    } else if (tipo === 'funcionario') {
      elLogin.value = 'NEX-1002';
      elSenha.value = 'user123';
      this.showToast('Credenciais de Funcionário preenchidas.', 'info');
    }
  },

  logout() {
    if (confirm('Deseja realmente sair da sessão corporativa?')) {
      Auth.logout();
      this.showToast('Você saiu do sistema com segurança.', 'info');
      this.showLogin();
    }
  },

  toggleSidebar() {
    const sidebar = document.getElementById('app-sidebar');
    const backdrop = document.getElementById('sidebar-backdrop');
    const isOpen = sidebar.classList.toggle('open');
    if (isOpen) {
      backdrop.classList.add('active');
    } else {
      backdrop.classList.remove('active');
    }
  },

  closeSidebar() {
    const sidebar = document.getElementById('app-sidebar');
    const backdrop = document.getElementById('sidebar-backdrop');
    if (sidebar) sidebar.classList.remove('open');
    if (backdrop) backdrop.classList.remove('active');
  },

  openModal(id) {
    const modal = document.getElementById(id);
    if (modal) {
      modal.classList.add('active');
    }
  },

  closeModal(id) {
    const modal = document.getElementById(id);
    if (modal) {
      modal.classList.remove('active');
    }
  },

  async submitRecuperarSenha(event) {
    event.preventDefault();
    const idf = document.getElementById('recuperar-identificador').value.trim();
    if (!idf) return;

    try {
      const res = await Api.post('/auth/recuperar-senha', { identificador: idf });
      this.showToast(res.message, 'success', 'Recuperação de Acesso');
      this.closeModal('modal-recuperar-senha');
    } catch (error) {
      this.showToast(error.message, 'error', 'Erro');
    }
  },

  async submitAlterarSenha(event) {
    event.preventDefault();
    const senhaAtual = document.getElementById('senha-atual').value;
    const novaSenha = document.getElementById('nova-senha').value;
    const confirmacao = document.getElementById('nova-senha-conf').value;

    if (novaSenha !== confirmacao) {
      this.showToast('A nova senha e a confirmação não coincidem.', 'warning');
      return;
    }

    try {
      const res = await Api.post('/auth/alterar-senha', { senhaAtual, novaSenha });
      this.showToast(res.message, 'success', 'Senha Alterada');
      this.closeModal('modal-alterar-senha');
      document.getElementById('form-alterar-senha').reset();
    } catch (error) {
      this.showToast(error.message, 'error', 'Erro ao Alterar');
    }
  },

  showToast(message, type = 'info', title = '') {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;

    let defaultTitle = 'Notificação';
    if (type === 'success') defaultTitle = 'Sucesso';
    if (type === 'error') defaultTitle = 'Atenção';
    if (type === 'warning') defaultTitle = 'Alerta';

    toast.innerHTML = `
      <div style="flex:1;">
        <div class="toast-title">${App.escapeHtml(title || defaultTitle)}</div>
        <div class="toast-message">${App.escapeHtml(message)}</div>
      </div>
      <button onclick="this.parentElement.remove()" style="background:transparent; border:none; color:var(--slate-400); cursor:pointer; padding:0 0.2rem;">&times;</button>
    `;

    container.appendChild(toast);

    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transform = 'translateX(50px)';
      setTimeout(() => toast.remove(), 300);
    }, 4500);
  },

  // Helpers de formatação
  getInitials(nome) {
    if (!nome) return 'NX';
    const partes = nome.trim().split(/\s+/);
    if (partes.length === 1) return partes[0].substring(0, 2).toUpperCase();
    return (partes[0][0] + partes[partes.length - 1][0]).toUpperCase();
  },

  formatDate(dateStr) {
    if (!dateStr) return '-';
    const [ano, mes, dia] = dateStr.split('-');
    return `${dia}/${mes}/${ano}`;
  },

  formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '-';
    const date = new Date(dateTimeStr);
    return date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
  },

  formatCurrency(val) {
    if (val === null || val === undefined) return 'R$ 0,00';
    return Number(val).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  },

  escapeHtml(str) {
    if (!str && str !== 0) return '';
    return String(str)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  }
};

// Inicialização automática ao carregar o DOM
document.addEventListener('DOMContentLoaded', () => {
  App.init();
});
