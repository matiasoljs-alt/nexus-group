/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * View: Gestão de Funcionários (RH / Admin)
 */

const FuncionariosView = {
  listaFuncionarios: [],
  listaSetores: [],
  listaCargos: [],
  listaEscalas: [],
  listaBeneficios: [],

  async render() {
    const container = document.getElementById('view-container');
    container.innerHTML = `
      <div style="display:flex; justify-content:center; align-items:center; min-height:300px;">
        <span class="loading-spinner"></span>
      </div>
    `;

    try {
      // Carrega dados necessários em paralelo
      const [funcionarios, setores, cargos, escalas, beneficios] = await Promise.all([
        Api.get('/funcionarios'),
        Api.get('/setores?apenasAtivos=true'),
        Api.get('/cargos?apenasAtivos=true'),
        Api.get('/escalas?apenasAtivas=true'),
        Api.get('/beneficios?apenasAtivos=true')
      ]);

      this.listaFuncionarios = funcionarios;
      this.listaSetores = setores;
      this.listaCargos = cargos;
      this.listaEscalas = escalas;
      this.listaBeneficios = beneficios;

      let html = `
        <!-- CABEÇALHO DA VIEW -->
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;">
          <div>
            <h2 style="font-size:1.35rem; font-weight:700; color:var(--slate-900);">Quadro de Colaboradores</h2>
            <p style="font-size:0.85rem; color:var(--slate-500);">Gerencie os colaboradores, contratos, alocação setorial e benefícios do Grupo Nexus.</p>
          </div>
          <button class="btn btn-primary" onclick="FuncionariosView.abrirModalCadastro()">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
            Novo Funcionário
          </button>
        </div>

        <!-- BARRA DE FILTROS E PESQUISA -->
        <div class="content-card" style="margin-bottom: 1.5rem; padding: 1rem 1.25rem;">
          <div class="filter-bar" style="margin-bottom:0;">
            <div class="search-input-box">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
              <input type="text" id="filtro-busca" class="form-control" placeholder="Buscar por nome, matrícula ou e-mail..." onkeyup="FuncionariosView.aplicarFiltros()">
            </div>
            <select id="filtro-setor" class="form-control" style="max-width:200px;" onchange="FuncionariosView.aplicarFiltros()">
              <option value="">Todos os Setores</option>
              ${this.listaSetores.map(s => `<option value="${s.id}">${App.escapeHtml(s.nome)}</option>`).join('')}
            </select>
            <select id="filtro-status" class="form-control" style="max-width:160px;" onchange="FuncionariosView.aplicarFiltros()">
              <option value="">Todos os Status</option>
              <option value="ATIVO">Ativos</option>
              <option value="INATIVO">Inativos</option>
            </select>
            <button class="btn btn-secondary btn-sm" onclick="FuncionariosView.limparFiltros()">Limpar</button>
          </div>
        </div>

        <!-- TABELA DE FUNCIONÁRIOS -->
        <div class="content-card">
          <div class="table-responsive">
            <table class="custom-table" id="tabela-funcionarios">
              <thead>
                <tr>
                  <th>Colaborador</th>
                  <th>Matrícula</th>
                  <th>Cargo & Nível</th>
                  <th>Setor</th>
                  <th>Contrato</th>
                  <th>Status</th>
                  <th style="text-align:right;">Ações</th>
                </tr>
              </thead>
              <tbody id="lista-funcionarios-tbody">
                ${this.renderLinhasTabela(this.listaFuncionarios)}
              </tbody>
            </table>
          </div>
        </div>
      `;

      container.innerHTML = html;
    } catch (error) {
      container.innerHTML = `
        <div class="empty-state">
          <p style="color:var(--rose-600); font-weight:600;">Erro ao carregar colaboradores:</p>
          <p style="font-size:0.85rem; color:var(--slate-500); margin-top:0.35rem;">${App.escapeHtml(error.message)}</p>
        </div>
      `;
    }
  },

  renderLinhasTabela(lista) {
    if (!lista || lista.length === 0) {
      return `
        <tr>
          <td colspan="7" style="text-align:center; padding:2.5rem; color:var(--slate-400);">
            Nenhum colaborador encontrado com os filtros aplicados.
          </td>
        </tr>
      `;
    }

    return lista.map(f => {
      const isAtivo = f.status === 'ATIVO';
      return `
        <tr>
          <td>
            <div style="display:flex; align-items:center; gap:0.75rem;">
              <div class="user-monogram" style="width:34px; height:34px; font-size:0.75rem; background:linear-gradient(135deg, var(--nexus-navy-800), var(--nexus-blue-600));">
                ${App.getInitials(f.nome)}
              </div>
              <div>
                <div style="font-weight:600; color:var(--slate-900);">${App.escapeHtml(f.nome)}</div>
                <div style="font-size:0.75rem; color:var(--slate-400);">${App.escapeHtml(f.email)}</div>
              </div>
            </div>
          </td>
          <td>
            <span class="role-badge rh" style="font-size:0.72rem;">${App.escapeHtml(f.matricula)}</span>
          </td>
          <td>
            <div style="font-weight:500; color:var(--slate-800);">${App.escapeHtml(f.cargoNome || '-')}</div>
            <div style="font-size:0.75rem; color:var(--slate-400);">${App.escapeHtml(f.cargoNivel || '')}</div>
          </td>
          <td>
            <span style="font-weight:500;">${App.escapeHtml(f.setorNome || '-')}</span>
          </td>
          <td>
            <span style="font-size:0.8rem; color:var(--slate-600);">${App.escapeHtml(f.tipoContrato || 'CLT')}</span>
          </td>
          <td>
            <span class="status-badge ${isAtivo ? 'ativo' : 'inativo'}">
              ${isAtivo ? 'Ativo' : 'Inativo'}
            </span>
          </td>
          <td style="text-align:right;">
            <div style="display:inline-flex; gap:0.35rem;">
              <button class="btn btn-secondary btn-icon" title="Ver Dossiê Completo" onclick="FuncionariosView.verDossie(${f.id})">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
              </button>
              <button class="btn btn-secondary btn-icon" title="Editar Colaborador" onclick="FuncionariosView.abrirModalEdicao(${f.id})">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
              </button>
              <button class="btn btn-secondary btn-icon" title="${isAtivo ? 'Inativar' : 'Ativar'}" onclick="FuncionariosView.alternarStatus(${f.id})">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="${isAtivo ? 'var(--rose-600)' : 'var(--emerald-600)'}" stroke-width="2"><path d="M18.36 6.64a9 9 0 1 1-12.73 0"></path><line x1="12" y1="2" x2="12" y2="12"></line></svg>
              </button>
              <button class="btn btn-secondary btn-icon" title="Excluir Colaborador" onclick="FuncionariosView.confirmarExclusao(${f.id}, '${App.escapeHtml(f.nome)}')">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="var(--rose-600)" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
              </button>
            </div>
          </td>
        </tr>
      `;
    }).join('');
  },

  aplicarFiltros() {
    const termo = (document.getElementById('filtro-busca')?.value || '').toLowerCase().trim();
    const setorId = document.getElementById('filtro-setor')?.value;
    const status = document.getElementById('filtro-status')?.value;

    const filtrados = this.listaFuncionarios.filter(f => {
      const matchBusca = !termo ||
        f.nome.toLowerCase().includes(termo) ||
        f.matricula.toLowerCase().includes(termo) ||
        f.email.toLowerCase().includes(termo);

      const matchSetor = !setorId || String(f.setorId) === String(setorId);
      const matchStatus = !status || f.status === status;

      return matchBusca && matchSetor && matchStatus;
    });

    const tbody = document.getElementById('lista-funcionarios-tbody');
    if (tbody) {
      tbody.innerHTML = this.renderLinhasTabela(filtrados);
    }
  },

  limparFiltros() {
    if (document.getElementById('filtro-busca')) document.getElementById('filtro-busca').value = '';
    if (document.getElementById('filtro-setor')) document.getElementById('filtro-setor').value = '';
    if (document.getElementById('filtro-status')) document.getElementById('filtro-status').value = '';
    this.aplicarFiltros();
  },

  abrirModalCadastro() {
    const form = document.getElementById('form-funcionario');
    if (form) form.reset();

    document.getElementById('func-id').value = '';
    document.getElementById('modal-func-title').innerText = 'Cadastrar Novo Colaborador';
    document.getElementById('box-usuario-acesso').style.display = 'block';

    this.popularSelectsFormulario();
    App.openModal('modal-funcionario');
  },

  popularSelectsFormulario(setorIdSelecionado, cargoIdSelecionado, escalaIdSelecionado, beneficiosIds = []) {
    // Setores
    const selectSetor = document.getElementById('func-setor');
    selectSetor.innerHTML = '<option value="">Selecione o setor...</option>' +
      this.listaSetores.map(s => `<option value="${s.id}" ${s.id == setorIdSelecionado ? 'selected' : ''}>${App.escapeHtml(s.nome)}</option>`).join('');

    // Cargos (atualiza baseado no setor ou lista todos)
    this.atualizarCargosPorSetor(setorIdSelecionado, cargoIdSelecionado);

    // Escalas
    const selectEscala = document.getElementById('func-escala');
    selectEscala.innerHTML = '<option value="">Selecione a escala...</option>' +
      this.listaEscalas.map(e => `<option value="${e.id}" ${e.id == escalaIdSelecionado ? 'selected' : ''}>${App.escapeHtml(e.nome)} (${e.horarioEntrada} - ${e.horarioSaida})</option>`).join('');

    // Benefícios Checkboxes
    const boxBeneficios = document.getElementById('func-beneficios-checkboxes');
    if (boxBeneficios) {
      boxBeneficios.innerHTML = this.listaBeneficios.map(b => {
        const checked = beneficiosIds.includes(b.id) ? 'checked' : '';
        return `
          <label style="display:flex; align-items:center; gap:0.5rem; font-size:0.85rem; padding:0.4rem 0.6rem; border:1px solid var(--slate-200); border-radius:var(--radius-sm); cursor:pointer;">
            <input type="checkbox" name="beneficioIds" value="${b.id}" ${checked}>
            <span>${App.escapeHtml(b.nome)} (${App.escapeHtml(b.categoria)})</span>
          </label>
        `;
      }).join('');
    }
  },

  atualizarCargosPorSetor(setorId, cargoIdSelecionado) {
    const selectCargo = document.getElementById('func-cargo');
    const filtrados = setorId ? this.listaCargos.filter(c => c.setor && c.setor.id == setorId) : this.listaCargos;

    selectCargo.innerHTML = '<option value="">Selecione o cargo...</option>' +
      filtrados.map(c => `<option value="${c.id}" ${c.id == cargoIdSelecionado ? 'selected' : ''}>${App.escapeHtml(c.nome)} - ${App.escapeHtml(c.nivel || '')}</option>`).join('');
  },

  abrirModalEdicao(id) {
    const f = this.listaFuncionarios.find(item => item.id === id);
    if (!f) return;

    document.getElementById('func-id').value = f.id;
    document.getElementById('modal-func-title').innerText = 'Editar Colaborador: ' + f.nome;
    document.getElementById('func-matricula').value = f.matricula;
    document.getElementById('func-nome').value = f.nome;
    document.getElementById('func-email').value = f.email;
    document.getElementById('func-telefone').value = f.telefone || '';
    document.getElementById('func-cpf').value = f.cpf || '';
    document.getElementById('func-dataNascimento').value = f.dataNascimento || '';
    document.getElementById('func-dataAdmissao').value = f.dataAdmissao || '';
    document.getElementById('func-status').value = f.status || 'ATIVO';
    document.getElementById('func-tipoContrato').value = f.tipoContrato || 'CLT';
    document.getElementById('func-observacoes').value = f.observacoes || '';

    // Esconde opção de recriar usuário no modo de edição (já criado)
    document.getElementById('box-usuario-acesso').style.display = 'none';

    const bIds = f.beneficios ? f.beneficios.map(b => b.id) : [];
    this.popularSelectsFormulario(f.setorId, f.cargoId, f.escalaId, bIds);

    App.openModal('modal-funcionario');
  },

  async salvarFuncionario(event) {
    event.preventDefault();
    const id = document.getElementById('func-id').value;

    const beneficioInputs = document.querySelectorAll('input[name="beneficioIds"]:checked');
    const beneficioIds = Array.from(beneficioInputs).map(cb => parseInt(cb.value));

    const payload = {
      matricula: document.getElementById('func-matricula').value.trim(),
      nome: document.getElementById('func-nome').value.trim(),
      email: document.getElementById('func-email').value.trim(),
      telefone: document.getElementById('func-telefone').value.trim(),
      cpf: document.getElementById('func-cpf').value.trim(),
      dataNascimento: document.getElementById('func-dataNascimento').value || null,
      dataAdmissao: document.getElementById('func-dataAdmissao').value,
      status: document.getElementById('func-status').value,
      tipoContrato: document.getElementById('func-tipoContrato').value,
      setorId: parseInt(document.getElementById('func-setor').value),
      cargoId: parseInt(document.getElementById('func-cargo').value),
      escalaId: document.getElementById('func-escala').value ? parseInt(document.getElementById('func-escala').value) : null,
      beneficioIds: beneficioIds,
      observacoes: document.getElementById('func-observacoes').value.trim()
    };

    if (!id) {
      // Novo funcionário
      payload.criarUsuarioAcesso = document.getElementById('func-criarUsuario')?.checked ?? true;
      payload.roleUsuario = document.getElementById('func-roleUsuario')?.value || 'ROLE_FUNCIONARIO';
      payload.senhaInicial = document.getElementById('func-senhaInicial')?.value.trim() || 'nexus123';
    }

    try {
      if (id) {
        await Api.put(`/funcionarios/${id}`, payload);
        App.showToast('Dados do colaborador atualizados com sucesso.', 'success');
      } else {
        await Api.post('/funcionarios', payload);
        App.showToast('Colaborador cadastrado com sucesso no Grupo Nexus.', 'success');
      }

      App.closeModal('modal-funcionario');
      this.render();
    } catch (error) {
      App.showToast(error.message, 'error', 'Erro ao Salvar');
    }
  },

  async alternarStatus(id) {
    try {
      await Api.patch(`/funcionarios/${id}/status`);
      App.showToast('Status do colaborador alterado com sucesso.', 'success');
      this.render();
    } catch (error) {
      App.showToast(error.message, 'error');
    }
  },

  async confirmarExclusao(id, nome) {
    if (confirm(`Deseja realmente excluir permanentemente o cadastro de ${nome}? Esta ação removerá também os acessos vinculados.`)) {
      try {
        await Api.delete(`/funcionarios/${id}`);
        App.showToast('Colaborador excluído com sucesso.', 'success');
        this.render();
      } catch (error) {
        App.showToast(error.message, 'error', 'Erro ao Excluir');
      }
    }
  },

  verDossie(id) {
    const f = this.listaFuncionarios.find(item => item.id === id);
    if (!f) return;

    const modalBody = document.getElementById('dossie-modal-body');
    modalBody.innerHTML = `
      <div class="profile-hero" style="margin-bottom: 1.25rem; padding: 1.25rem;">
        <div class="profile-avatar-block">
          <div class="profile-monogram" style="width:54px; height:54px; font-size:1.3rem;">
            ${App.getInitials(f.nome)}
          </div>
          <div>
            <h3 style="font-size:1.2rem; font-weight:700; color:#fff;">${App.escapeHtml(f.nome)}</h3>
            <p style="font-size:0.85rem; color:var(--slate-300);">Matrícula: <strong>${App.escapeHtml(f.matricula)}</strong> • ${App.escapeHtml(f.cargoNome || '')}</p>
          </div>
        </div>
        <span class="status-badge ${f.status === 'ATIVO' ? 'ativo' : 'inativo'}">${f.status}</span>
      </div>

      <div style="display:grid; grid-template-columns: 1fr 1fr; gap:1rem; font-size:0.88rem;">
        <div><strong>Setor:</strong> ${App.escapeHtml(f.setorNome || '-')}</div>
        <div><strong>Cargo / Nível:</strong> ${App.escapeHtml(f.cargoNome || '-')} (${App.escapeHtml(f.cargoNivel || '')})</div>
        <div><strong>E-mail:</strong> ${App.escapeHtml(f.email)}</div>
        <div><strong>Telefone:</strong> ${App.escapeHtml(f.telefone || '-')}</div>
        <div><strong>Data Admissão:</strong> ${f.dataAdmissao ? App.formatDate(f.dataAdmissao) : '-'}</div>
        <div><strong>CPF:</strong> ${App.escapeHtml(f.cpf || '-')}</div>
        <div><strong>Tipo de Contrato:</strong> ${App.escapeHtml(f.tipoContrato || 'CLT')}</div>
        <div><strong>Escala:</strong> ${App.escapeHtml(f.escalaNome || 'Padrão')}</div>
      </div>

      <div style="margin-top:1.25rem;">
        <strong>Benefícios Atribuídos:</strong>
        <div style="display:flex; flex-wrap:wrap; gap:0.4rem; margin-top:0.4rem;">
          ${f.beneficios && f.beneficios.length > 0 
            ? f.beneficios.map(b => `<span class="role-badge rh" style="font-size:0.75rem;">${App.escapeHtml(b.nome)}</span>`).join('')
            : '<span style="color:var(--slate-400); font-size:0.85rem;">Nenhum benefício vinculado.</span>'}
        </div>
      </div>

      ${f.observacoes ? `
        <div style="margin-top:1.25rem; background:var(--slate-50); padding:0.75rem; border-radius:var(--radius-sm); border:1px solid var(--slate-200); font-size:0.85rem;">
          <strong>Observações Internas:</strong>
          <p style="color:var(--slate-600); margin-top:0.25rem;">${App.escapeHtml(f.observacoes)}</p>
        </div>
      ` : ''}
    `;

    App.openModal('modal-dossie');
  }
};
