package com.todocode.academy.service;

import com.todocode.academy.model.Usuario;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private Long nextId = 1L;

    public UsuarioService() {
        listaUsuarios.add(new Usuario(nextId++, "Juan Pérez", "juan@example.com", "ADMIN"));
        listaUsuarios.add(new Usuario(nextId++, "María García", "maria@example.com", "USER"));
    }

    public List<Usuario> obtenerTodos() {
        return listaUsuarios;
    }

    public void guardar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(nextId++);
            listaUsuarios.add(usuario);
        }
    }

    public void eliminar(Long id) {
        listaUsuarios.removeIf(u -> u.getId().equals(id));
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return listaUsuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
    }
}