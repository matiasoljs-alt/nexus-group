/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * View: Escalas e Jornadas de Trabalho
 */

const EscalasView = {
  listaEscalas: [],

  async render() {
    const container = document.getElementById('view-container');
    container.innerHTML = `
      <div style="display:flex; justify-content:center; align-items:center; min-height:300px;">
        <span class="loading-spinner"></span>
      </div>
    `;

    try {
      this.listaEscalas = await Api.get('/escalas');

      let html = `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;">
          <div>
            <h2 style="font-size:1.35rem; font-weight:700; color:var(--slate-900);">Escalas e Jornadas de Trabalho</h2>
            <p style="font-size:0.85rem; color:var(--slate-500);">Grade de horários, turnos, descansos semanais e controle de jornada do Grupo Nexus.</p>
          </div>
          ${Auth.canManage() ? `
            <button class="btn btn-primary" onclick="EscalasView.abrirModalCadastro()">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
              Nova Escala
            </button>
          ` : ''}
        </div>

        <div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap:1.25rem;">
      `;

      if (this.listaEscalas.length === 0) {
        html += `
          <div class="empty-state" style="grid-column: 1 / -1;">
            <p>Nenhuma escala cadastrada no momento.</p>
          </div>
        `;
      } else {
        this.listaEscalas.forEach(e => {
          html += `
            <div class="content-card">
              <div class="card-header" style="background:var(--slate-50);">
                <div style="display:flex; align-items:center; justify-content:space-between; width:100%;">
                  <div>
                    <h3 style="font-size:1.05rem; font-weight:700; color:var(--slate-900);">${App.escapeHtml(e.nome)}</h3>
                    <span style="font-size:0.75rem; color:var(--slate-500);">Tipo: <strong>${App.escapeHtml(e.tipoEscala || '5x2')}</strong></span>
                  </div>
                  <span class="status-badge ${e.ativo ? 'ativo' : 'inativo'}">${e.ativo ? 'Ativa' : 'Inativa'}</span>
                </div>
              </div>
              <div class="card-body">
                <div style="display:grid; grid-template-columns: 1fr 1fr; gap:0.75rem; font-size:0.85rem; margin-bottom:1rem;">
                  <div style="background:var(--slate-50); padding:0.6rem; border-radius:var(--radius-sm);">
                    <span style="color:var(--slate-400); font-size:0.75rem; display:block;">Entrada</span>
                    <strong style="color:var(--slate-800); font-size:1.1rem;">${App.escapeHtml(e.horarioEntrada || '--:--')}</strong>
                  </div>
                  <div style="background:var(--slate-50); padding:0.6rem; border-radius:var(--radius-sm);">
                    <span style="color:var(--slate-400); font-size:0.75rem; display:block;">Saída</span>
                    <strong style="color:var(--slate-800); font-size:1.1rem;">${App.escapeHtml(e.horarioSaida || '--:--')}</strong>
                  </div>
                </div>

                <div style="font-size:0.85rem; display:flex; flex-direction:column; gap:0.4rem; color:var(--slate-600); margin-bottom:1rem;">
                  <div><strong>Intervalo:</strong> ${App.escapeHtml(e.intervalo || '1h')}</div>
                  <div><strong>Dias de Trabalho:</strong> ${App.escapeHtml(e.diasTrabalho || 'Segunda a Sexta')}</div>
                  <div><strong>Folgas:</strong> ${App.escapeHtml(e.folgas || 'Sábado e Domingo')}</div>
                  ${e.descricao ? `<div style="font-size:0.8rem; color:var(--slate-500); margin-top:0.25rem;">${App.escapeHtml(e.descricao)}</div>` : ''}
                </div>

                ${Auth.canManage() ? `
                  <div style="display:flex; justify-content:flex-end; gap:0.5rem; border-top:1px solid var(--slate-100); padding-top:0.75rem;">
                    <button class="btn btn-secondary btn-sm" onclick="EscalasView.abrirModalEdicao(${e.id})">Editar</button>
                    <button class="btn btn-secondary btn-sm" onclick="EscalasView.excluir(${e.id})" style="color:var(--rose-600);">Excluir</button>
                  </div>
                ` : ''}
              </div>
            </div>
          `;
        });
      }

      html += `</div>`;
      container.innerHTML = html;
    } catch (error) {
      container.innerHTML = `
        <div class="empty-state">
          <p style="color:var(--rose-600); font-weight:600;">Erro ao carregar escalas:</p>
          <p style="font-size:0.85rem; color:var(--slate-500);">${App.escapeHtml(error.message)}</p>
        </div>
      `;
    }
  },

  abrirModalCadastro() {
    const form = document.getElementById('form-escala');
    if (form) form.reset();
    document.getElementById('escala-id').value = '';
    document.getElementById('modal-escala-title').innerText = 'Nova Escala de Trabalho';
    App.openModal('modal-escala');
  },

  abrirModalEdicao(id) {
    const e = this.listaEscalas.find(item => item.id === id);
    if (!e) return;

    document.getElementById('escala-id').value = e.id;
    document.getElementById('modal-escala-title').innerText = 'Editar Escala: ' + e.nome;
    document.getElementById('escala-nome').value = e.nome;
    document.getElementById('escala-tipo').value = e.tipoEscala || '5x2';
    document.getElementById('escala-entrada').value = e.horarioEntrada || '';
    document.getElementById('escala-saida').value = e.horarioSaida || '';
    document.getElementById('escala-intervalo').value = e.intervalo || '';
    document.getElementById('escala-dias').value = e.diasTrabalho || '';
    document.getElementById('escala-folgas').value = e.folgas || '';
    document.getElementById('escala-descricao').value = e.descricao || '';

    App.openModal('modal-escala');
  },

  async salvarEscala(event) {
    event.preventDefault();
    const id = document.getElementById('escala-id').value;

    const payload = {
      nome: document.getElementById('escala-nome').value.trim(),
      tipoEscala: document.getElementById('escala-tipo').value.trim(),
      horarioEntrada: document.getElementById('escala-entrada').value.trim(),
      horarioSaida: document.getElementById('escala-saida').value.trim(),
      intervalo: document.getElementById('escala-intervalo').value.trim(),
      diasTrabalho: document.getElementById('escala-dias').value.trim(),
      folgas: document.getElementById('escala-folgas').value.trim(),
      descricao: document.getElementById('escala-descricao').value.trim(),
      ativo: true
    };

    try {
      if (id) {
        await Api.put(`/escalas/${id}`, payload);
        App.showToast('Escala atualizada com sucesso.', 'success');
      } else {
        await Api.post('/escalas', payload);
        App.showToast('Escala cadastrada com sucesso.', 'success');
      }

      App.closeModal('modal-escala');
      this.render();
    } catch (error) {
      App.showToast(error.message, 'error', 'Erro');
    }
  },

  async excluir(id) {
    if (confirm('Deseja realmente remover esta escala de trabalho?')) {
      try {
        await Api.delete(`/escalas/${id}`);
        App.showToast('Escala removida com sucesso.', 'success');
        this.render();
      } catch (error) {
        App.showToast(error.message, 'error');
      }
    }
  }
};
