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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;  // ← usamos la implementación

    private Usuario usuarioCompleto;
    private Usuario usuarioSinEmail;
    private Usuario usuarioSinDireccion;

    @BeforeEach
    void setUp() {
        // Usuario con datos COMPLETOS (válido)
        usuarioCompleto = new Usuario();
        usuarioCompleto.setId(1L);
        usuarioCompleto.setUsername("cliente1");
        usuarioCompleto.setPassword("123456");
        usuarioCompleto.setEmail("cliente@minimarket.cl");
        usuarioCompleto.setNombre("Juan");
        usuarioCompleto.setApellido("Perez");
        usuarioCompleto.setDireccion("Av. Siempre Viva 123");

        // Usuario sin email (inválido)
        usuarioSinEmail = new Usuario();
        usuarioSinEmail.setUsername("cliente2");
        usuarioSinEmail.setPassword("123456");
        usuarioSinEmail.setNombre("Pedro");
        usuarioSinEmail.setApellido("Gomez");
        usuarioSinEmail.setDireccion("Calle Falsa 123");
        // falta email

        // Usuario sin dirección (inválido)
        usuarioSinDireccion = new Usuario();
        usuarioSinDireccion.setUsername("cliente3");
        usuarioSinDireccion.setPassword("123456");
        usuarioSinDireccion.setEmail("cliente3@mail.com");
        usuarioSinDireccion.setNombre("Maria");
        usuarioSinDireccion.setApellido("Lopez");
        // falta direccion
    }

    // ========== PRUEBA DE DISPONIBILIDAD (DATOS COMPLETOS) ==========

    @Test
    void testValidarDatosCompletos_UsuarioValido_RetornaTrue() {
        boolean esValido = usuarioService.validarDatosCompletos(usuarioCompleto);
        assertTrue(esValido);
    }

    @Test
    void testValidarDatosCompletos_UsuarioSinEmail_RetornaFalse() {
        boolean esValido = usuarioService.validarDatosCompletos(usuarioSinEmail);
        assertFalse(esValido);
    }

    @Test
    void testValidarDatosCompletos_UsuarioSinDireccion_RetornaFalse() {
        boolean esValido = usuarioService.validarDatosCompletos(usuarioSinDireccion);
        assertFalse(esValido);
    }

    // ========== PRUEBAS EXISTENTES (findById) ==========

    @Test
    void testFindById_UsuarioExistente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioCompleto));
        
        Optional<Usuario> encontrado = usuarioService.findById(1L);
        
        assertTrue(encontrado.isPresent());
        assertEquals("cliente1", encontrado.get().getUsername());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_UsuarioNoExistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        
        Optional<Usuario> encontrado = usuarioService.findById(99L);
        
        assertFalse(encontrado.isPresent());
        verify(usuarioRepository, times(1)).findById(99L);
    }
}