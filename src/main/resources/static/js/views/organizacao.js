/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * View: Estrutura Organizacional (Cargos & Setores) - RH / Admin
 */

const OrganizacaoView = {
  abaAtiva: 'setores',
  listaSetores: [],
  listaCargos: [],

  async render() {
    const container = document.getElementById('view-container');
    container.innerHTML = `
      <div style="display:flex; justify-content:center; align-items:center; min-height:300px;">
        <span class="loading-spinner"></span>
      </div>
    `;

    try {
      const [setores, cargos] = await Promise.all([
        Api.get('/setores'),
        Api.get('/cargos')
      ]);

      this.listaSetores = setores;
      this.listaCargos = cargos;

      let html = `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;">
          <div>
            <h2 style="font-size:1.35rem; font-weight:700; color:var(--slate-900);">Estrutura Organizacional</h2>
            <p style="font-size:0.85rem; color:var(--slate-500);">Gerencie os setores corporativos e o plano de cargos e níveis do Grupo Nexus.</p>
          </div>
          <div>
            ${this.abaAtiva === 'setores' ? `
              <button class="btn btn-primary" onclick="OrganizacaoView.abrirModalSetor()">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
                Novo Setor
              </button>
            ` : `
              <button class="btn btn-primary" onclick="OrganizacaoView.abrirModalCargo()">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
                Novo Cargo
              </button>
            `}
          </div>
        </div>

        <!-- ABAS DE NAVEGAÇÃO -->
        <div style="display:flex; gap:0.5rem; margin-bottom:1.25rem; border-bottom:1px solid var(--slate-200); padding-bottom:0.5rem;">
          <button class="btn ${this.abaAtiva === 'setores' ? 'btn-primary' : 'btn-secondary'} btn-sm" onclick="OrganizacaoView.trocarAba('setores')">
            Departamentos / Setores (${this.listaSetores.length})
          </button>
          <button class="btn ${this.abaAtiva === 'cargos' ? 'btn-primary' : 'btn-secondary'} btn-sm" onclick="OrganizacaoView.trocarAba('cargos')">
            Cargos & Níveis (${this.listaCargos.length})
          </button>
        </div>

        <div class="content-card">
          <div class="table-responsive">
      `;

      if (this.abaAtiva === 'setores') {
        html += `
          <table class="custom-table">
            <thead>
              <tr>
                <th>Nome do Setor</th>
                <th>Sigla</th>
                <th>Descrição</th>
                <th>Status</th>
                <th style="text-align:right;">Ações</th>
              </tr>
            </thead>
            <tbody>
              ${this.listaSetores.map(s => `
                <tr>
                  <td><strong>${App.escapeHtml(s.nome)}</strong></td>
                  <td><span class="role-badge rh">${App.escapeHtml(s.sigla || '-')}</span></td>
                  <td style="color:var(--slate-600);">${App.escapeHtml(s.descricao || '-')}</td>
                  <td><span class="status-badge ${s.ativo ? 'ativo' : 'inativo'}">${s.ativo ? 'Ativo' : 'Inativo'}</span></td>
                  <td style="text-align:right;">
                    <div style="display:inline-flex; gap:0.35rem;">
                      <button class="btn btn-secondary btn-icon" title="Editar" onclick="OrganizacaoView.editarSetor(${s.id})">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                      </button>
                      <button class="btn btn-secondary btn-icon" title="Excluir" onclick="OrganizacaoView.excluirSetor(${s.id})" style="color:var(--rose-600);">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
                      </button>
                    </div>
                  </td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        `;
      } else {
        html += `
          <table class="custom-table">
            <thead>
              <tr>
                <th>Cargo</th>
                <th>Nível</th>
                <th>Setor Vinculado</th>
                <th>Salário Base</th>
                <th>Status</th>
                <th style="text-align:right;">Ações</th>
              </tr>
            </thead>
            <tbody>
              ${this.listaCargos.map(c => `
                <tr>
                  <td><strong>${App.escapeHtml(c.nome)}</strong></td>
                  <td><span class="role-badge rh">${App.escapeHtml(c.nivel || 'Geral')}</span></td>
                  <td>${App.escapeHtml(c.setor?.nome || '-')}</td>
                  <td style="font-weight:600; color:var(--nexus-blue-600);">${c.salarioBase ? App.formatCurrency(c.salarioBase) : '-'}</td>
                  <td><span class="status-badge ${c.ativo ? 'ativo' : 'inativo'}">${c.ativo ? 'Ativo' : 'Inativo'}</span></td>
                  <td style="text-align:right;">
                    <div style="display:inline-flex; gap:0.35rem;">
                      <button class="btn btn-secondary btn-icon" title="Editar" onclick="OrganizacaoView.editarCargo(${c.id})">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                      </button>
                      <button class="btn btn-secondary btn-icon" title="Excluir" onclick="OrganizacaoView.excluirCargo(${c.id})" style="color:var(--rose-600);">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
                      </button>
                    </div>
                  </td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        `;
      }

      html += `
          </div>
        </div>
      `;

      container.innerHTML = html;
    } catch (error) {
      container.innerHTML = `
        <div class="empty-state">
          <p style="color:var(--rose-600); font-weight:600;">Erro ao carregar estrutura:</p>
          <p style="font-size:0.85rem; color:var(--slate-500);">${App.escapeHtml(error.message)}</p>
        </div>
      `;
    }
  },

  trocarAba(aba) {
    this.abaAtiva = aba;
    this.render();
  },

  abrirModalSetor() {
    const form = document.getElementById('form-setor');
    if (form) form.reset();
    document.getElementById('setor-id').value = '';
    document.getElementById('modal-setor-title').innerText = 'Novo Setor Corporativo';
    App.openModal('modal-setor');
  },

  editarSetor(id) {
    const s = this.listaSetores.find(item => item.id === id);
    if (!s) return;

    document.getElementById('setor-id').value = s.id;
    document.getElementById('modal-setor-title').innerText = 'Editar Setor: ' + s.nome;
    document.getElementById('setor-nome').value = s.nome;
    document.getElementById('setor-sigla').value = s.sigla || '';
    document.getElementById('setor-descricao').value = s.descricao || '';
    App.openModal('modal-setor');
  },

  async salvarSetor(event) {
    event.preventDefault();
    const id = document.getElementById('setor-id').value;

    const payload = {
      nome: document.getElementById('setor-nome').value.trim(),
      sigla: document.getElementById('setor-sigla').value.trim(),
      descricao: document.getElementById('setor-descricao').value.trim(),
      ativo: true
    };

    try {
      if (id) {
        await Api.put(`/setores/${id}`, payload);
        App.showToast('Setor atualizado com sucesso.', 'success');
      } else {
        await Api.post('/setores', payload);
        App.showToast('Setor cadastrado com sucesso.', 'success');
      }

      App.closeModal('modal-setor');
      this.render();
    } catch (error) {
      App.showToast(error.message, 'error', 'Erro');
    }
  },

  async excluirSetor(id) {
    if (confirm('Deseja excluir este setor? Atenção: não deve conter funcionários vinculados.')) {
      try {
        await Api.delete(`/setores/${id}`);
        App.showToast('Setor removido com sucesso.', 'success');
        this.render();
      } catch (error) {
        App.showToast(error.message, 'error');
      }
    }
  },

  abrirModalCargo() {
    const form = document.getElementById('form-cargo');
    if (form) form.reset();
    document.getElementById('cargo-id').value = '';
    document.getElementById('modal-cargo-title').innerText = 'Novo Cargo Corporativo';

    const select = document.getElementById('cargo-setor-id');
    select.innerHTML = '<option value="">Selecione o setor...</option>' +
      this.listaSetores.map(s => `<option value="${s.id}">${App.escapeHtml(s.nome)}</option>`).join('');

    App.openModal('modal-cargo');
  },

  editarCargo(id) {
    const c = this.listaCargos.find(item => item.id === id);
    if (!c) return;

    document.getElementById('cargo-id').value = c.id;
    document.getElementById('modal-cargo-title').innerText = 'Editar Cargo: ' + c.nome;
    document.getElementById('cargo-nome').value = c.nome;
    document.getElementById('cargo-nivel').value = c.nivel || 'Pleno';
    document.getElementById('cargo-salario').value = c.salarioBase || '';
    document.getElementById('cargo-descricao').value = c.descricao || '';

    const select = document.getElementById('cargo-setor-id');
    select.innerHTML = '<option value="">Selecione o setor...</option>' +
      this.listaSetores.map(s => `<option value="${s.id}" ${s.id == c.setor?.id ? 'selected' : ''}>${App.escapeHtml(s.nome)}</option>`).join('');

    App.openModal('modal-cargo');
  },

  async salvarCargo(event) {
    event.preventDefault();
    const id = document.getElementById('cargo-id').value;

    const payload = {
      nome: document.getElementById('cargo-nome').value.trim(),
      nivel: document.getElementById('cargo-nivel').value.trim(),
      salarioBase: document.getElementById('cargo-salario').value ? parseFloat(document.getElementById('cargo-salario').value) : null,
      descricao: document.getElementById('cargo-descricao').value.trim(),
      setorId: parseInt(document.getElementById('cargo-setor-id').value),
      ativo: true
    };

    try {
      if (id) {
        await Api.put(`/cargos/${id}`, payload);
        App.showToast('Cargo atualizado com sucesso.', 'success');
      } else {
        await Api.post('/cargos', payload);
        App.showToast('Cargo cadastrado com sucesso.', 'success');
      }

      App.closeModal('modal-cargo');
      this.render();
    } catch (error) {
      App.showToast(error.message, 'error', 'Erro');
    }
  },

  async excluirCargo(id) {
    if (confirm('Deseja excluir este cargo?')) {
      try {
        await Api.delete(`/cargos/${id}`);
        App.showToast('Cargo removido com sucesso.', 'success');
        this.render();
      } catch (error) {
        App.showToast(error.message, 'error');
      }
    }
  }
};
