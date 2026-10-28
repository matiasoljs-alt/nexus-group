/**
 * GRUPO NEXUS - GESTÃO INTERNA
 * View: Dashboard Corporativo
 */

const DashboardView = {
  async render() {
    const container = document.getElementById('view-container');
    container.innerHTML = `
      <div style="display:flex; justify-content:center; align-items:center; min-height:300px;">
        <span class="loading-spinner"></span>
      </div>
    `;

    try {
      const data = await Api.get('/dashboard');
      const user = Auth.getUser();

      const totalFunc = data.totalFuncionarios || 0;
      const ativos = data.funcionariosAtivos || 0;
      const setoresCount = data.totalSetores || 0;
      const beneficiosCount = data.totalBeneficios || 0;

      // Resumo de Saudação
      const hora = new Date().getHours();
      let saudacao = 'Bom dia';
      if (hora >= 12 && hora < 18) saudacao = 'Boa tarde';
      if (hora >= 18 || hora < 5) saudacao = 'Boa noite';

      let html = `
        <!-- BANNER DE BOAS-VINDAS -->
        <div class="profile-hero" style="margin-bottom: 1.5rem;">
          <div class="profile-avatar-block">
            <div class="profile-monogram">
              ${App.getInitials(user?.nome || user?.username || 'Nexus')}
            </div>
            <div>
              <h2>${saudacao}, ${App.escapeHtml(user?.nome || user?.username)}!</h2>
              <p>Bem-vindo(a) ao portal corporativo do <strong>Grupo Nexus</strong>. Acompanhe abaixo o resumo das atividades e indicadores internos.</p>
            </div>
          </div>
          <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
            <button class="btn btn-secondary btn-sm" onclick="App.navigate('perfil')" style="background: rgba(255,255,255,0.1); color:#fff; border-color: rgba(255,255,255,0.2);">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
              Meu Perfil
            </button>
            ${Auth.canManage() ? `
              <button class="btn btn-primary btn-sm" onclick="App.navigate('funcionarios')">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                Gerenciar Equipe
              </button>
            ` : ''}
          </div>
        </div>

        <!-- CARDS KPI DE ESTATÍSTICA -->
        <div class="kpi-grid">
          <div class="kpi-card blue">
            <div class="kpi-info">
              <h3>Total Colaboradores</h3>
              <div class="kpi-number">${totalFunc}</div>
              <div class="kpi-subtitle">Cadastrados no sistema</div>
            </div>
            <div class="kpi-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
            </div>
          </div>

          <div class="kpi-card emerald">
            <div class="kpi-info">
              <h3>Colaboradores Ativos</h3>
              <div class="kpi-number">${ativos}</div>
              <div class="kpi-subtitle">${totalFunc > 0 ? Math.round((ativos / totalFunc) * 100) : 100}% do quadro geral</div>
            </div>
            <div class="kpi-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
            </div>
          </div>

          <div class="kpi-card indigo">
            <div class="kpi-info">
              <h3>Setores Estratégicos</h3>
              <div class="kpi-number">${setoresCount}</div>
              <div class="kpi-subtitle">Departamentos ativos</div>
            </div>
            <div class="kpi-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path></svg>
            </div>
          </div>

          <div class="kpi-card amber">
            <div class="kpi-info">
              <h3>Benefícios Corporativos</h3>
              <div class="kpi-number">${beneficiosCount}</div>
              <div class="kpi-subtitle">Programas disponíveis</div>
            </div>
            <div class="kpi-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon></svg>
            </div>
          </div>
        </div>

        <!-- SEÇÃO EM DUAS COLUNAS: DISTRIBUIÇÃO E ESCALA -->
        <div class="two-col-grid">
          <!-- Coluna 1: Funcionários por Setor -->
          <div class="content-card">
            <div class="card-header">
              <div class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--nexus-blue-500)" stroke-width="2"><line x1="18" y1="20" x2="18" y2="10"></line><line x1="12" y1="20" x2="12" y2="4"></line><line x1="6" y1="20" x2="6" y2="14"></line></svg>
                <h2>Funcionários por Setor</h2>
              </div>
              <span style="font-size: 0.75rem; color: var(--slate-400);">Ativos</span>
            </div>
            <div class="card-body">
      `;

      if (data.funcionariosPorSetor && data.funcionariosPorSetor.length > 0) {
        data.funcionariosPorSetor.forEach(item => {
          const nomeSetor = item.setor || 'Não especificado';
          const qtd = item.quantidade || 0;
          const pct = ativos > 0 ? Math.round((qtd / ativos) * 100) : 0;

          html += `
            <div class="sector-progress-item">
              <div class="sector-meta">
                <span class="sector-name">${App.escapeHtml(nomeSetor)}</span>
                <span class="sector-count">${qtd} (${pct}%)</span>
              </div>
              <div class="progress-track">
                <div class="progress-fill" style="width: ${pct}%;"></div>
              </div>
            </div>
          `;
        });
      } else {
        html += `<p style="color:var(--slate-400); font-size:0.85rem;">Nenhum funcionário vinculado a setores no momento.</p>`;
      }

      html += `
            </div>
          </div>

          <!-- Coluna 2: Próxima Escala / Jornada de Trabalho -->
          <div class="content-card">
            <div class="card-header">
              <div class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--nexus-blue-500)" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
                <h2>Próxima Escala & Horário</h2>
              </div>
              <button class="btn btn-secondary btn-sm" onclick="App.navigate('escalas')">Ver todas</button>
            </div>
            <div class="card-body">
      `;

      if (data.escalaAtual) {
        const esc = data.escalaAtual;
        html += `
          <div style="background: var(--nexus-blue-50); border: 1px solid var(--nexus-blue-100); border-radius: var(--radius-md); padding: 1.25rem; margin-bottom: 1rem;">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:0.75rem;">
              <h3 style="font-size: 1.05rem; font-weight:700; color:var(--nexus-navy-900);">${App.escapeHtml(esc.nome)}</h3>
              <span class="role-badge rh">${App.escapeHtml(esc.tipoEscala || 'Padrão')}</span>
            </div>
            <div style="display:grid; grid-template-columns: 1fr 1fr; gap:0.75rem; font-size:0.85rem;">
              <div>
                <span style="color:var(--slate-500); display:block; font-size:0.75rem;">Horário de Entrada</span>
                <strong style="color:var(--slate-800); font-size:1rem;">${App.escapeHtml(esc.horarioEntrada || '--:--')}</strong>
              </div>
              <div>
                <span style="color:var(--slate-500); display:block; font-size:0.75rem;">Horário de Saída</span>
                <strong style="color:var(--slate-800); font-size:1rem;">${App.escapeHtml(esc.horarioSaida || '--:--')}</strong>
              </div>
              <div>
                <span style="color:var(--slate-500); display:block; font-size:0.75rem;">Intervalo</span>
                <span style="color:var(--slate-700);">${App.escapeHtml(esc.intervalo || '1 hora')}</span>
              </div>
              <div>
                <span style="color:var(--slate-500); display:block; font-size:0.75rem;">Dias de Trabalho</span>
                <span style="color:var(--slate-700);">${App.escapeHtml(esc.diasTrabalho || 'Segunda a Sexta')}</span>
              </div>
            </div>
          </div>
          <div style="display:flex; align-items:center; gap:0.5rem; font-size:0.8rem; color:var(--slate-500);">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg>
            <span>Folgas programadas: <strong>${App.escapeHtml(esc.folgas || 'Fins de semana')}</strong></span>
          </div>
        `;
      } else {
        html += `
          <div style="text-align:center; padding: 2rem 1rem; color:var(--slate-500);">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="var(--slate-300)" stroke-width="2" style="margin-bottom:0.5rem;"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
            <p>Jornada administrativa padrão <strong>5x2 (08h às 17h)</strong>.</p>
            <p style="font-size:0.8rem; color:var(--slate-400); margin-top:0.25rem;">Consulte o departamento de RH para detalhes de escalas específicas.</p>
          </div>
        `;
      }

      html += `
            </div>
          </div>
        </div>

        <!-- MURAL DE COMUNICADOS RECENTES -->
        <div class="content-card">
          <div class="card-header">
            <div class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--nexus-blue-500)" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path></svg>
              <h2>Comunicados Recentes</h2>
            </div>
            <button class="btn btn-secondary btn-sm" onclick="App.navigate('comunicados')">Ver todos</button>
          </div>
          <div class="card-body">
      `;

      if (data.comunicadosRecentes && data.comunicadosRecentes.length > 0) {
        data.comunicadosRecentes.forEach(c => {
          const dataFormatada = App.formatDateTime(c.dataPublicacao);
          html += `
            <div class="notice-card ${c.importante ? 'urgent' : ''}">
              <div class="notice-header">
                <div style="display:flex; align-items:center; gap:0.5rem;">
                  ${c.importante ? '<span class="status-badge inativo" style="font-size:0.7rem;">URGENTE / DESTAQUE</span>' : ''}
                  <span class="notice-title">${App.escapeHtml(c.titulo)}</span>
                </div>
                <span class="notice-date">${dataFormatada}</span>
              </div>
              <div class="notice-content">${App.escapeHtml(c.conteudo)}</div>
              <div class="notice-author">Publicado por: ${App.escapeHtml(c.autor || 'Recursos Humanos')}</div>
            </div>
          `;
        });
      } else {
        html += `<p style="color:var(--slate-400); font-size:0.85rem;">Nenhum comunicado publicado no momento.</p>`;
      }

      html += `
          </div>
        </div>
      `;

      container.innerHTML = html;
    } catch (error) {
      container.innerHTML = `
        <div class="empty-state">
          <p style="color:var(--rose-600); font-weight:600;">Falha ao carregar o painel:</p>
          <p style="font-size:0.85rem; color:var(--slate-500); margin-top:0.35rem;">${App.escapeHtml(error.message)}</p>
          <button class="btn btn-primary btn-sm" onclick="DashboardView.render()" style="margin-top:1rem;">Tentar novamente</button>
        </div>
      `;
    }
  }
};
