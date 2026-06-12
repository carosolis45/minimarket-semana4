package com.minimarket.controller;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.util.JwtUtil;
import com.minimarket.security.model.LoginRequest;
import com.minimarket.security.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Endpoint para LOGIN - genera un JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Guardar la autenticación en el contexto
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Obtener los detalles del usuario
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // Generar el token JWT
        String token = jwtUtil.generateToken(userDetails.getUsername());
        
        // Obtener el rol del usuario
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();

        // Devolver la respuesta con el token
        return ResponseEntity.ok(new LoginResponse(token, userDetails.getUsername(), rol));
    }

    // 2. Endpoint para REGISTRO de nuevos usuarios
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        // Verificar si el usuario ya existe
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El usuario ya existe");
        }
        
        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Asignar el rol CLIENTE por defecto
        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
            .orElseThrow(() -> new RuntimeException("Error: Rol CLIENTE no encontrado en la base de datos"));
        usuario.setRoles(Set.of(rolCliente));
        
        // Guardar el usuario
        usuarioRepository.save(usuario);
        
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}