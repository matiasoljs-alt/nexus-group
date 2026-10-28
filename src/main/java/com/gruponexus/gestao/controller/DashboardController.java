package com.gruponexus.gestao.controller;

import com.gruponexus.gestao.dto.response.DashboardResponse;
import com.gruponexus.gestao.entity.Usuario;
import com.gruponexus.gestao.service.AuthService;
import com.gruponexus.gestao.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthService authService;

    public DashboardController(DashboardService dashboardService, AuthService authService) {
        this.dashboardService = dashboardService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> obterDashboard(Authentication authentication) {
        Usuario usuario = null;
        if (authentication != null && authentication.isAuthenticated()) {
            usuario = authService.obterUsuarioAtual(authentication.getName());
        }
        DashboardResponse response = dashboardService.obterMetricas(usuario);
        return ResponseEntity.ok(response);
    }
}
