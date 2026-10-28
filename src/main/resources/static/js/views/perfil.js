/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * View: Meu Perfil (Dossiê do Colaborador)
 */

const PerfilView = {
  async render() {
    const container = document.getElementById('view-container');
    container.innerHTML = `
      <div style="display:flex; justify-content:center; align-items:center; min-height:300px;">
        <span class="loading-spinner"></span>
      </div>
    `;

    try {
      const user = Auth.getUser();
      let func = null;

      try {
        func = await Api.get('/funcionarios/me');
      } catch (e) {
        console.warn('Usuário atual não possui ficha de funcionário vinculada (ex: Admin puro)');
      }

      const nome = func?.nome || user?.nome || user?.username;
      const matricula = func?.matricula || user?.matricula || user?.username;
      const cargo = func?.cargoNome || 'Não especificado';
      const nivel = func?.cargoNivel || 'Geral';
      const setor = func?.setorNome || 'Corporativo';
      const status = func?.status || 'ATIVO';

      let html = `
        <!-- HERO DO PERFIL -->
        <div class="profile-hero">
          <div class="profile-avatar-block">
            <div class="profile-monogram" style="width:72px; height:72px; font-size:1.75rem;">
              ${App.getInitials(nome)}
            </div>
            <div>
              <div style="display:flex; align-items:center; gap:0.6rem; margin-bottom:0.35rem; flex-wrap:wrap;">
                <h2>${App.escapeHtml(nome)}</h2>
                <span class="status-badge ativo">${status}</span>
                <span class="role-badge rh" style="font-size:0.75rem;">${App.escapeHtml(matricula)}</span>
              </div>
              <p>${App.escapeHtml(cargo)} ${nivel !== 'Geral' ? `• Nível ${App.escapeHtml(nivel)}` : ''} • Setor de ${App.escapeHtml(setor)}</p>
            </div>
          </div>

          <div style="display:flex; gap:0.5rem; flex-wrap:wrap;">
            <button class="btn btn-secondary btn-sm" onclick="App.openModal('modal-alterar-senha')" style="background: rgba(255,255,255,0.1); color:#fff; border-color: rgba(255,255,255,0.2);">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
              Alterar Senha
            </button>
          </div>
        </div>

        <!-- GRID DOSSIÊ -->
        <div class="two-col-grid">
          <!-- DADOS PROFISSIONAIS -->
          <div class="content-card">
            <div class="card-header">
              <div class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--nexus-blue-500)" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path></svg>
                <h2>Dados Profissionais</h2>
              </div>
            </div>
            <div class="card-body">
              <div style="display:grid; grid-template-columns: 1fr 1fr; gap:1.25rem;">
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Cargo Atual</span>
                  <div style="font-weight:600; color:var(--slate-800);">${App.escapeHtml(cargo)}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Nível Hierárquico</span>
                  <div style="font-weight:600; color:var(--slate-800);">${App.escapeHtml(nivel)}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Departamento / Setor</span>
                  <div style="font-weight:600; color:var(--slate-800);">${App.escapeHtml(setor)}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Data de Admissão</span>
                  <div style="font-weight:600; color:var(--slate-800);">${func?.dataAdmissao ? App.formatDate(func.dataAdmissao) : 'Não informada'}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Tipo de Vínculo</span>
                  <div style="font-weight:600; color:var(--slate-800);">${App.escapeHtml(func?.tipoContrato || 'CLT Efetivo')}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Matrícula Corporativa</span>
                  <div style="font-weight:600; color:var(--slate-800);">${App.escapeHtml(matricula)}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- DADOS DE CONTATO & PESSOAIS -->
          <div class="content-card">
            <div class="card-header">
              <div class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--nexus-blue-500)" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path></svg>
                <h2>Contato e Identificação</h2>
              </div>
            </div>
            <div class="card-body">
              <div style="display:grid; grid-template-columns: 1fr 1fr; gap:1.25rem;">
                <div style="grid-column: span 2;">
                  <span class="form-label" style="color:var(--slate-400);">E-mail Corporativo</span>
                  <div style="font-weight:600; color:var(--nexus-blue-600);">${App.escapeHtml(func?.email || user?.email)}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Telefone / Ramal</span>
                  <div style="font-weight:600; color:var(--slate-800);">${App.escapeHtml(func?.telefone || '(11) 3456-7890')}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">CPF Mascarado</span>
                  <div style="font-weight:600; color:var(--slate-800);">${App.escapeHtml(func?.cpf || '***.***.***-**')}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Data de Nascimento</span>
                  <div style="font-weight:600; color:var(--slate-800);">${func?.dataNascimento ? App.formatDate(func.dataNascimento) : 'Não informada'}</div>
                </div>
                <div>
                  <span class="form-label" style="color:var(--slate-400);">Nível de Acesso</span>
                  <div style="font-weight:600; color:var(--slate-800);">${App.escapeHtml(user?.role || 'FUNCIONARIO')}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- ESCALA DE TRABALHO DO COLABORADOR -->
        <div class="content-card" style="margin-bottom: 1.75rem;">
          <div class="card-header">
            <div class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--nexus-blue-500)" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
              <h2>Minha Escala & Horário de Trabalho</h2>
            </div>
          </div>
          <div class="card-body">
      `;

      if (func?.escalaNome) {
        html += `
          <div style="background:var(--slate-50); border:1px solid var(--slate-200); border-radius:var(--radius-md); padding:1.25rem;">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem; flex-wrap:wrap; gap:0.5rem;">
              <h3 style="font-size:1.1rem; color:var(--slate-900);">${App.escapeHtml(func.escalaNome)}</h3>
              <span class="role-badge rh">${App.escapeHtml(func.escalaTipo || 'Padrão')}</span>
            </div>
            <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:1rem; font-size:0.88rem;">
              <div>
                <span style="color:var(--slate-400); display:block; font-size:0.75rem;">Entrada</span>
                <strong style="color:var(--slate-800); font-size:1.1rem;">${App.escapeHtml(func.horarioEntrada || '--:--')}</strong>
              </div>
              <div>
                <span style="color:var(--slate-400); display:block; font-size:0.75rem;">Saída</span>
                <strong style="color:var(--slate-800); font-size:1.1rem;">${App.escapeHtml(func.horarioSaida || '--:--')}</strong>
              </div>
              <div>
                <span style="color:var(--slate-400); display:block; font-size:0.75rem;">Intervalo</span>
                <span>${App.escapeHtml(func.intervalo || '1h')}</span>
              </div>
              <div>
                <span style="color:var(--slate-400); display:block; font-size:0.75rem;">Dias de Trabalho</span>
                <span>${App.escapeHtml(func.diasTrabalho || 'Segunda a Sexta')}</span>
              </div>
              <div>
                <span style="color:var(--slate-400); display:block; font-size:0.75rem;">Folgas</span>
                <span>${App.escapeHtml(func.folgas || 'Fins de semana')}</span>
              </div>
            </div>
          </div>
        `;
      } else {
        html += `<p style="color:var(--slate-500); font-size:0.9rem;">Escala padrão corporativa 5x2 (08:00 às 17:00 com 1 hora de almoço).</p>`;
      }

      html += `
          </div>
        </div>

        <!-- MEUS BENEFÍCIOS -->
        <div class="content-card">
          <div class="card-header">
            <div class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--nexus-blue-500)" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon></svg>
              <h2>Meus Benefícios Corporativos Ativos</h2>
            </div>
            <span style="font-size:0.8rem; color:var(--slate-500);">${func?.beneficios?.length || 0} ativos</span>
          </div>
          <div class="card-body">
      `;

      if (func?.beneficios && func.beneficios.length > 0) {
        html += `<div class="benefit-grid">`;
        func.beneficios.forEach(b => {
          html += `
            <div class="benefit-card">
              <div class="benefit-card-header">
                <span class="role-badge rh">${App.escapeHtml(b.categoria || 'Geral')}</span>
                <span class="status-badge ativo">Ativo</span>
              </div>
              <div class="benefit-title">${App.escapeHtml(b.nome)}</div>
              <div class="benefit-desc">${App.escapeHtml(b.descricao || 'Benefício disponibilizado pela política de RH do Grupo Nexus.')}</div>
              <div class="benefit-value-box">
                <span style="color:var(--slate-500);">Subsídio Mensal:</span>
                <span class="benefit-value">${b.valor ? App.formatCurrency(b.valor) : 'Integral pela Empresa'}</span>
              </div>
            </div>
          `;
        });
        html += `</div>`;
      } else {
        html += `<p style="color:var(--slate-400); font-size:0.85rem;">Nenhum benefício específico vinculado a este perfil.</p>`;
      }

      html += `
          </div>
        </div>
      `;

      container.innerHTML = html;
    } catch (error) {
      container.innerHTML = `
        <div class="empty-state">
          <p style="color:var(--rose-600); font-weight:600;">Falha ao carregar o perfil:</p>
          <p style="font-size:0.85rem; color:var(--slate-500); margin-top:0.35rem;">${App.escapeHtml(error.message)}</p>
        </div>
      `;
    }
  }
};
