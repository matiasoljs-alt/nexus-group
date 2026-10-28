package com.gruponexus.gestao.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint unauthorizedHandler,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Rotas estáticas públicas da interface web
                .requestMatchers("/", "/index.html", "/favicon.ico", "/css/**", "/js/**", "/assets/**").permitAll()
                // Console H2 (se ativado)
                .requestMatchers("/h2-console/**").permitAll()
                // Autenticação e recuperação de senha
                .requestMatchers("/api/auth/**").permitAll()

                // Gestão de Usuários e Permissões (apenas Administrador)
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                // Modificações (POST, PUT, DELETE, PATCH) em Funcionários, Cargos, Setores, Escalas, Benefícios e Comunicados: RH e ADMIN
                .requestMatchers(HttpMethod.POST, "/api/funcionarios/**").hasAnyRole("RH", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/funcionarios/**").hasAnyRole("RH", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/funcionarios/**").hasAnyRole("RH", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/funcionarios/**").hasAnyRole("RH", "ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/cargos/**", "/api/setores/**", "/api/escalas/**", "/api/beneficios/**", "/api/comunicados/**").hasAnyRole("RH", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/cargos/**", "/api/setores/**", "/api/escalas/**", "/api/beneficios/**", "/api/comunicados/**").hasAnyRole("RH", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/cargos/**", "/api/setores/**", "/api/escalas/**", "/api/beneficios/**", "/api/comunicados/**").hasAnyRole("RH", "ADMIN")

                // Qualquer outra rota de API requer autenticação (Funcionário, RH ou Admin)
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            );

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())); // Para o H2 console
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
