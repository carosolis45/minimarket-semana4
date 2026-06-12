package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;  // ← Usamos la implementación

    private Producto productoSinStock;
    private Producto productoConStock;
    private DetalleVenta detalleSinStock;
    private DetalleVenta detalleConStock;
    private Venta ventaValida;

    @BeforeEach
    void setUp() {
        productoSinStock = new Producto();
        productoSinStock.setId(1L);
        productoSinStock.setStock(0);

        productoConStock = new Producto();
        productoConStock.setId(2L);
        productoConStock.setStock(10);

        detalleConStock = new DetalleVenta();
        detalleConStock.setProducto(productoConStock);
        detalleConStock.setCantidad(1);
        detalleConStock.setPrecio(2500.0);

        detalleSinStock = new DetalleVenta();
        detalleSinStock.setProducto(productoSinStock);
        detalleSinStock.setCantidad(1);
        detalleSinStock.setPrecio(1000.0);

        ventaValida = new Venta();
        ventaValida.setDetalles(Arrays.asList(detalleConStock));
    }

    @Test
    void testValidarStock_ProductoConStock_RetornaTrue() {
        boolean stockDisponible = ventaService.validarStock(ventaValida);
        assertTrue(stockDisponible);
    }

    @Test
    void testValidarStock_ProductoSinStock_RetornaFalse() {
        Venta ventaSinStock = new Venta();
        ventaSinStock.setDetalles(Arrays.asList(detalleSinStock));
        
        boolean stockDisponible = ventaService.validarStock(ventaSinStock);
        assertFalse(stockDisponible);
    }

    @Test
    void testCalcularTotalVenta() {
        double total = ventaService.calcularTotal(ventaValida);
        assertEquals(2500.0, total);
    }
}