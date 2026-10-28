/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * View: Mural de Comunicados Internos
 */

const ComunicadosView = {
  listaComunicados: [],

  async render() {
    const container = document.getElementById('view-container');
    container.innerHTML = `
      <div style="display:flex; justify-content:center; align-items:center; min-height:300px;">
        <span class="loading-spinner"></span>
      </div>
    `;

    try {
      this.listaComunicados = await Api.get('/comunicados?apenasAtivos=true');

      let html = `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;">
          <div>
            <h2 style="font-size:1.35rem; font-weight:700; color:var(--slate-900);">Mural Corporativo de Comunicados</h2>
            <p style="font-size:0.85rem; color:var(--slate-500);">Avisos institucionais, comunicados do RH, campanhas internas e diretrizes corporativas.</p>
          </div>
          ${Auth.canManage() ? `
            <button class="btn btn-primary" onclick="ComunicadosView.abrirModalCadastro()">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
              Novo Comunicado
            </button>
          ` : ''}
        </div>

        <div class="content-card">
          <div class="card-body">
      `;

      if (this.listaComunicados.length === 0) {
        html += `<p style="color:var(--slate-400); text-align:center; padding:2rem;">Nenhum comunicado disponível no momento.</p>`;
      } else {
        this.listaComunicados.forEach(c => {
          const dataFormatada = App.formatDateTime(c.dataPublicacao);
          html += `
            <div class="notice-card ${c.importante ? 'urgent' : ''}">
              <div class="notice-header">
                <div style="display:flex; align-items:center; gap:0.6rem; flex-wrap:wrap;">
                  ${c.importante ? '<span class="status-badge inativo" style="font-size:0.7rem; font-weight:700;">URGENTE / DESTAQUE</span>' : ''}
                  <span class="role-badge rh" style="font-size:0.72rem;">${App.escapeHtml(c.categoria || 'Geral')}</span>
                  <span class="notice-title">${App.escapeHtml(c.titulo)}</span>
                </div>
                <span class="notice-date">${dataFormatada}</span>
              </div>
              <div class="notice-content" style="white-space:pre-line; margin-top:0.4rem;">${App.escapeHtml(c.conteudo)}</div>
              <div style="display:flex; justify-content:space-between; align-items:center; margin-top:0.75rem; flex-wrap:wrap; gap:0.5rem;">
                <div class="notice-author">Publicado por: <strong>${App.escapeHtml(c.autor || 'Diretoria de RH')}</strong></div>
                ${Auth.canManage() ? `
                  <div style="display:flex; gap:0.4rem;">
                    <button class="btn btn-secondary btn-sm" onclick="ComunicadosView.abrirModalEdicao(${c.id})">Editar</button>
                    <button class="btn btn-secondary btn-sm" onclick="ComunicadosView.excluir(${c.id})" style="color:var(--rose-600);">Excluir</button>
                  </div>
                ` : ''}
              </div>
            </div>
          `;
        });
      }

      html += `
          </div>
        </div>
      `;

      container.innerHTML = html;
    } catch (error) {
      container.innerHTML = `
        <div class="empty-state">
          <p style="color:var(--rose-600); font-weight:600;">Erro ao carregar comunicados:</p>
          <p style="font-size:0.85rem; color:var(--slate-500);">${App.escapeHtml(error.message)}</p>
        </div>
      `;
    }
  },

  abrirModalCadastro() {
    const form = document.getElementById('form-comunicado');
    if (form) form.reset();
    document.getElementById('comunicado-id').value = '';
    document.getElementById('modal-comunicado-title').innerText = 'Publicar Novo Comunicado';
    document.getElementById('comunicado-importante').checked = false;
    App.openModal('modal-comunicado');
  },

  abrirModalEdicao(id) {
    const c = this.listaComunicados.find(item => item.id === id);
    if (!c) return;

    document.getElementById('comunicado-id').value = c.id;
    document.getElementById('modal-comunicado-title').innerText = 'Editar Comunicado: ' + c.titulo;
    document.getElementById('comunicado-titulo').value = c.titulo;
    document.getElementById('comunicado-categoria').value = c.categoria || 'Geral';
    document.getElementById('comunicado-autor').value = c.autor || '';
    document.getElementById('comunicado-importante').checked = !!c.importante;
    document.getElementById('comunicado-conteudo').value = c.conteudo;

    App.openModal('modal-comunicado');
  },

  async salvarComunicado(event) {
    event.preventDefault();
    const id = document.getElementById('comunicado-id').value;

    const payload = {
      titulo: document.getElementById('comunicado-titulo').value.trim(),
      categoria: document.getElementById('comunicado-categoria').value.trim(),
      autor: document.getElementById('comunicado-autor').value.trim() || 'Recursos Humanos',
      importante: document.getElementById('comunicado-importante').checked,
      conteudo: document.getElementById('comunicado-conteudo').value.trim(),
      ativo: true
    };

    try {
      if (id) {
        await Api.put(`/comunicados/${id}`, payload);
        App.showToast('Comunicado atualizado com sucesso.', 'success');
      } else {
        await Api.post('/comunicados', payload);
        App.showToast('Comunicado publicado no mural com sucesso.', 'success');
      }

      App.closeModal('modal-comunicado');
      this.render();
    } catch (error) {
      App.showToast(error.message, 'error', 'Erro ao Salvar');
    }
  },

  async excluir(id) {
    if (confirm('Deseja realmente remover este comunicado do mural?')) {
      try {
        await Api.delete(`/comunicados/${id}`);
        App.showToast('Comunicado removido com sucesso.', 'success');
        this.render();
      } catch (error) {
        App.showToast(error.message, 'error');
      }
    }
  }
};
