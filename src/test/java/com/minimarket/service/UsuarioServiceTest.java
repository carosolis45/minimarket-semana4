package com.minimarket.service;

import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioExistente;
    private Usuario usuarioNuevo;

    @BeforeEach
    void setUp() {
        usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setUsername("juanito");
        usuarioExistente.setPassword("password123");

        usuarioNuevo = new Usuario();
        usuarioNuevo.setUsername("pedrito");
        usuarioNuevo.setPassword("123456");
    }

    @Test
    void testFindByUsername_UsuarioExistente() {
        when(usuarioRepository.findByUsername("juanito")).thenReturn(Optional.of(usuarioExistente));

        Optional<Usuario> resultado = usuarioService.findByUsername("juanito");

        assertTrue(resultado.isPresent());
        assertEquals("juanito", resultado.get().getUsername());
        verify(usuarioRepository, times(1)).findByUsername("juanito");
    }

    @Test
    void testFindByUsername_UsuarioNoExistente() {
        when(usuarioRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findByUsername("inexistente");

        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findByUsername("inexistente");
    }

    @Test
    void testAddUsuario_CodificaPasswordYGuarda() {
        String passwordPlano = "123456";
        String passwordCodificada = "$2a$10$codificada123";
        
        when(passwordEncoder.encode(passwordPlano)).thenReturn(passwordCodificada);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });

        Usuario resultado = usuarioService.save(usuarioNuevo);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("pedrito", resultado.getUsername());
        assertEquals(passwordCodificada, resultado.getPassword());
        
        verify(passwordEncoder, times(1)).encode(passwordPlano);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_ConservaPasswordSiNoCambia() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setUsername("juanito_modificado");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.update(1L, usuarioActualizado);

        assertNotNull(resultado);
        assertEquals("juanito_modificado", resultado.getUsername());
        assertEquals("password123", resultado.getPassword());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}