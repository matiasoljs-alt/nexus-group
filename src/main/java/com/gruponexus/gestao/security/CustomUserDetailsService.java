package com.gruponexus.gestao.security;

import com.gruponexus.gestao.entity.Usuario;
import com.gruponexus.gestao.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOuEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(usernameOuEmail, usernameOuEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com login ou e-mail: " + usernameOuEmail));

        return new CustomUserDetails(usuario);
    }
}
