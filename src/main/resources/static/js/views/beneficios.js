/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * View: Benefícios Corporativos
 */

const BeneficiosView = {
  listaBeneficios: [],

  async render() {
    const container = document.getElementById('view-container');
    container.innerHTML = `
      <div style="display:flex; justify-content:center; align-items:center; min-height:300px;">
        <span class="loading-spinner"></span>
      </div>
    `;

    try {
      this.listaBeneficios = await Api.get('/beneficios');

      let html = `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;">
          <div>
            <h2 style="font-size:1.35rem; font-weight:700; color:var(--slate-900);">Programa de Benefícios Corporativos</h2>
            <p style="font-size:0.85rem; color:var(--slate-500);">Planos de saúde, alimentação, bem-estar, mobilidade e incentivos aos colaboradores do Grupo Nexus.</p>
          </div>
          ${Auth.canManage() ? `
            <button class="btn btn-primary" onclick="BeneficiosView.abrirModalCadastro()">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
              Novo Benefício
            </button>
          ` : ''}
        </div>

        <div class="benefit-grid">
      `;

      if (this.listaBeneficios.length === 0) {
        html += `
          <div class="empty-state" style="grid-column: 1 / -1;">
            <p>Nenhum benefício cadastrado no momento.</p>
          </div>
        `;
      } else {
        this.listaBeneficios.forEach(b => {
          html += `
            <div class="benefit-card">
              <div class="benefit-card-header">
                <span class="role-badge rh">${App.escapeHtml(b.categoria || 'Geral')}</span>
                <span class="status-badge ${b.ativo ? 'ativo' : 'inativo'}">${b.ativo ? 'Ativo' : 'Inativo'}</span>
              </div>
              <div class="benefit-title">${App.escapeHtml(b.nome)}</div>
              <div class="benefit-desc">${App.escapeHtml(b.descricao || 'Sem descrição informada.')}</div>
              
              <div class="benefit-value-box" style="margin-bottom:0.75rem;">
                <span style="color:var(--slate-500);">Subsídio Mensal:</span>
                <span class="benefit-value">${b.valor ? App.formatCurrency(b.valor) : 'Subsidiado 100%'}</span>
              </div>

              ${b.informacoesImportantes ? `
                <div style="font-size:0.78rem; color:var(--slate-500); background:var(--slate-50); padding:0.5rem; border-radius:var(--radius-sm); margin-bottom:0.75rem;">
                  <strong>Regras:</strong> ${App.escapeHtml(b.informacoesImportantes)}
                </div>
              ` : ''}

              ${Auth.canManage() ? `
                <div style="display:flex; justify-content:flex-end; gap:0.4rem; border-top:1px solid var(--slate-100); padding-top:0.6rem; margin-top:auto;">
                  <button class="btn btn-secondary btn-sm" onclick="BeneficiosView.abrirModalEdicao(${b.id})">Editar</button>
                  <button class="btn btn-secondary btn-sm" onclick="BeneficiosView.excluir(${b.id})" style="color:var(--rose-600);">Excluir</button>
                </div>
              ` : ''}
            </div>
          `;
        });
      }

      html += `</div>`;
      container.innerHTML = html;
    } catch (error) {
      container.innerHTML = `
        <div class="empty-state">
          <p style="color:var(--rose-600); font-weight:600;">Erro ao carregar benefícios:</p>
          <p style="font-size:0.85rem; color:var(--slate-500);">${App.escapeHtml(error.message)}</p>
        </div>
      `;
    }
  },

  abrirModalCadastro() {
    const form = document.getElementById('form-beneficio');
    if (form) form.reset();
    document.getElementById('beneficio-id').value = '';
    document.getElementById('modal-beneficio-title').innerText = 'Novo Benefício Corporativo';
    App.openModal('modal-beneficio');
  },

  abrirModalEdicao(id) {
    const b = this.listaBeneficios.find(item => item.id === id);
    if (!b) return;

    document.getElementById('beneficio-id').value = b.id;
    document.getElementById('modal-beneficio-title').innerText = 'Editar Benefício: ' + b.nome;
    document.getElementById('beneficio-nome').value = b.nome;
    document.getElementById('beneficio-categoria').value = b.categoria || 'Saúde';
    document.getElementById('beneficio-valor').value = b.valor || '';
    document.getElementById('beneficio-descricao').value = b.descricao || '';
    document.getElementById('beneficio-info').value = b.informacoesImportantes || '';

    App.openModal('modal-beneficio');
  },

  async salvarBeneficio(event) {
    event.preventDefault();
    const id = document.getElementById('beneficio-id').value;

    const payload = {
      nome: document.getElementById('beneficio-nome').value.trim(),
      categoria: document.getElementById('beneficio-categoria').value.trim(),
      valor: document.getElementById('beneficio-valor').value ? parseFloat(document.getElementById('beneficio-valor').value) : null,
      descricao: document.getElementById('beneficio-descricao').value.trim(),
      informacoesImportantes: document.getElementById('beneficio-info').value.trim(),
      ativo: true
    };

    try {
      if (id) {
        await Api.put(`/beneficios/${id}`, payload);
        App.showToast('Benefício atualizado com sucesso.', 'success');
      } else {
        await Api.post('/beneficios', payload);
        App.showToast('Benefício cadastrado com sucesso.', 'success');
      }

      App.closeModal('modal-beneficio');
      this.render();
    } catch (error) {
      App.showToast(error.message, 'error', 'Erro');
    }
  },

  async excluir(id) {
    if (confirm('Deseja realmente remover este benefício do catálogo?')) {
      try {
        await Api.delete(`/beneficios/${id}`);
        App.showToast('Benefício removido com sucesso.', 'success');
        this.render();
      } catch (error) {
        App.showToast(error.message, 'error');
      }
    }
  }
};
