/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * Módulo de Autenticação e Controle de Acesso (RBAC)
 */

const Auth = {
  getUser() {
    const raw = localStorage.getItem('nexus_user');
    return raw ? JSON.parse(raw) : null;
  },

  setUser(user) {
    localStorage.setItem('nexus_user', JSON.stringify(user));
  },

  setToken(token) {
    localStorage.setItem('nexus_token', token);
  },

  isAuthenticated() {
    return !!localStorage.getItem('nexus_token') && !!this.getUser();
  },

  getRole() {
    const user = this.getUser();
    return user ? user.role : null;
  },

  isAdmin() {
    return this.getRole() === 'ROLE_ADMIN';
  },

  isRh() {
    return this.getRole() === 'ROLE_RH';
  },

  isFuncionario() {
    return this.getRole() === 'ROLE_FUNCIONARIO';
  },

  canManage() {
    return this.isAdmin() || this.isRh();
  },

  async login(login, senha) {
    const response = await Api.post('/auth/login', { login, senha });
    this.setToken(response.token);
    this.setUser({
      id: response.id,
      username: response.username,
      email: response.email,
      role: response.role,
      matricula: response.matricula,
      nome: response.nome
    });
    return response;
  },

  logout() {
    localStorage.removeItem('nexus_token');
    localStorage.removeItem('nexus_user');
  }
};
