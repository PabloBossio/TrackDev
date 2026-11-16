package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.LoginRequestDTO;
import com.Trackdev.control.Dto.LoginResponseDTO;
import com.Trackdev.control.Entity.Usuario;
import com.Trackdev.control.Mapper.UsuarioMapper;
import com.Trackdev.control.Repository.UsuarioRepository;
import com.Trackdev.control.Security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.email());
        String token = jwtService.generarToken(userDetails);

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + dto.email()));

        return new LoginResponseDTO(token, UsuarioMapper.toResponseDTO(usuario));
    }
}