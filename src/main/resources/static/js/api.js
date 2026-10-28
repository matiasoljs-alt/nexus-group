/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * Módulo de Integração com API REST
 */

const API_BASE = '/api';

const Api = {
  getToken() {
    return localStorage.getItem('nexus_token');
  },

  async request(endpoint, options = {}) {
    const url = `${API_BASE}${endpoint}`;
    const token = this.getToken();

    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
      ...(options.headers || {})
    };

    const config = {
      ...options,
      headers
    };

    try {
      const response = await fetch(url, config);

      // Tratamento de 401 (Não autorizado / Sessão expirada)
      if (response.status === 401 && !endpoint.includes('/auth/login')) {
        Auth.logout();
        App.showToast('Sua sessão expirou. Por favor, realize o login novamente.', 'error', 'Sessão Expirada');
        App.navigate('login');
        throw new Error('Sessão expirada');
      }

      // Se for 204 No Content
      if (response.status === 204) {
        return null;
      }

      const data = await response.json().catch(() => null);

      if (!response.ok) {
        const errorMessage = data?.message || (data?.details ? Object.values(data.details).join(', ') : 'Erro ao processar solicitação');
        throw new Error(errorMessage);
      }

      return data;
    } catch (error) {
      console.error(`Erro na requisição ${endpoint}:`, error);
      throw error;
    }
  },

  get(endpoint) {
    return this.request(endpoint, { method: 'GET' });
  },

  post(endpoint, body) {
    return this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(body)
    });
  },

  put(endpoint, body) {
    return this.request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(body)
    });
  },

  patch(endpoint, body) {
    return this.request(endpoint, {
      method: 'PATCH',
      body: body ? JSON.stringify(body) : undefined
    });
  },

  delete(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  }
};
