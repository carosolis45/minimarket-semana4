package com.minimarket.service.impl;

import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta findById(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> findByUsuarioId(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

     // NUEVO MÉTODO: Validar stock
    public boolean validarStock(Venta venta) {
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) return false;
        
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            if (producto == null || producto.getStock() < detalle.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    // NUEVO MÉTODO: Calcular total
    public double calcularTotal(Venta venta) {
        double total = 0.0;
        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                total += detalle.getPrecio() * detalle.getCantidad();
            }
        }
        return total;
    }
}


