package com.minimarket.service;

import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    void testFindById_VentaExistente() {
        Venta venta = new Venta();
        venta.setId(1L);
        
        when(ventaRepository.findById(1L)).thenReturn(java.util.Optional.of(venta));
        
        Venta resultado = ventaService.findById(1L);
        
        assertNotNull(resultado);
        verify(ventaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_VentaNoExistente() {
        when(ventaRepository.findById(99L)).thenReturn(java.util.Optional.empty());
        
        Venta resultado = ventaService.findById(99L);
        
        assertNull(resultado);
        verify(ventaRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveVenta() {
        Venta venta = new Venta();
        venta.setId(1L);
        
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        
        Venta resultado = ventaService.save(venta);
        
        assertNotNull(resultado);
        verify(ventaRepository, times(1)).save(any(Venta.class));
    }
}