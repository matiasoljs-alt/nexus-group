/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * View: Gestão de Usuários e Permissões (Administrador)
 */

const UsuariosView = {
  listaUsuarios: [],

  async render() {
    const container = document.getElementById('view-container');
    container.innerHTML = `
      <div style="display:flex; justify-content:center; align-items:center; min-height:300px;">
        <span class="loading-spinner"></span>
      </div>
    `;

    try {
      this.listaUsuarios = await Api.get('/usuarios');

      let html = `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;">
          <div>
            <h2 style="font-size:1.35rem; font-weight:700; color:var(--slate-900);">Gestão de Usuários & Controle de Acesso</h2>
            <p style="font-size:0.85rem; color:var(--slate-500);">Gerencie permissões de perfil (Administrador, RH, Funcionário), bloqueios e credenciais do sistema.</p>
          </div>
        </div>

        <div class="content-card">
          <div class="table-responsive">
            <table class="custom-table">
              <thead>
                <tr>
                  <th>Usuário / Login</th>
                  <th>Colaborador Vinculado</th>
                  <th>E-mail</th>
                  <th>Nível de Acesso (Role)</th>
                  <th>Status</th>
                  <th>Último Acesso</th>
                  <th style="text-align:right;">Ações</th>
                </tr>
              </thead>
              <tbody>
                ${this.listaUsuarios.map(u => {
                  let roleClass = 'funcionario';
                  if (u.role === 'ROLE_ADMIN') roleClass = 'admin';
                  if (u.role === 'ROLE_RH') roleClass = 'rh';

                  return `
                    <tr>
                      <td>
                        <strong>${App.escapeHtml(u.username)}</strong>
                      </td>
                      <td>
                        <div style="font-weight:500; color:var(--slate-800);">${App.escapeHtml(u.funcionarioNome || '-')}</div>
                        ${u.matricula && u.matricula !== u.username ? `<span style="font-size:0.75rem; color:var(--slate-400);">Matrícula: ${App.escapeHtml(u.matricula)}</span>` : ''}
                      </td>
                      <td>${App.escapeHtml(u.email)}</td>
                      <td>
                        <span class="role-badge ${roleClass}">${App.escapeHtml(u.roleDescricao || u.role)}</span>
                      </td>
                      <td>
                        <span class="status-badge ${u.ativo ? 'ativo' : 'inativo'}">
                          ${u.ativo ? 'Ativo' : 'Inativo'}
                        </span>
                      </td>
                      <td style="font-size:0.8rem; color:var(--slate-500);">
                        ${u.ultimoAcesso ? App.formatDateTime(u.ultimoAcesso) : 'Nunca acessou'}
                      </td>
                      <td style="text-align:right;">
                        <button class="btn btn-secondary btn-sm" onclick="UsuariosView.abrirModalEdicao(${u.id})">
                          Gerenciar
                        </button>
                      </td>
                    </tr>
                  `;
                }).join('')}
              </tbody>
            </table>
          </div>
        </div>
      `;

      container.innerHTML = html;
    } catch (error) {
      container.innerHTML = `
        <div class="empty-state">
          <p style="color:var(--rose-600); font-weight:600;">Erro ao carregar usuários:</p>
          <p style="font-size:0.85rem; color:var(--slate-500);">${App.escapeHtml(error.message)}</p>
        </div>
      `;
    }
  },

  abrirModalEdicao(id) {
    const u = this.listaUsuarios.find(item => item.id === id);
    if (!u) return;

    document.getElementById('user-edit-id').value = u.id;
    document.getElementById('user-edit-username').innerText = u.username + ' (' + (u.funcionarioNome || 'Sistema') + ')';
    document.getElementById('user-edit-role').value = u.role;
    document.getElementById('user-edit-ativo').value = u.ativo ? 'true' : 'false';
    document.getElementById('user-edit-senha').value = '';

    App.openModal('modal-usuario');
  },

  async salvarUsuario(event) {
    event.preventDefault();
    const id = document.getElementById('user-edit-id').value;

    const payload = {
      role: document.getElementById('user-edit-role').value,
      ativo: document.getElementById('user-edit-ativo').value === 'true',
      novaSenha: document.getElementById('user-edit-senha').value.trim() || null
    };

    try {
      await Api.put(`/usuarios/${id}`, payload);
      App.showToast('Permissões e dados do usuário atualizados.', 'success');
      App.closeModal('modal-usuario');
      this.render();
    } catch (error) {
      App.showToast(error.message, 'error', 'Erro');
    }
  }
};
