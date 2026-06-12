package com.minimarket.service.impl;

import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    // ========== NUEVO METODO IMPLEMENTADO ==========
    @Override
    public boolean validarDatosCompletos(Usuario usuario) {
        if (usuario == null) return false;
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) return false;
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) return false;
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) return false;
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) return false;
        if (usuario.getApellido() == null || usuario.getApellido().isEmpty()) return false;
        if (usuario.getDireccion() == null || usuario.getDireccion().isEmpty()) return false;
        return true;
    }
}
