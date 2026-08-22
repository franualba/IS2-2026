package com.sistema.gestion.service.impl;

import com.sistema.gestion.exception.CorreoYaRegistradoException;
import com.sistema.gestion.exception.UsuarioNoEncontradoException;
import com.sistema.gestion.model.EstadoUsuario;
import com.sistema.gestion.model.Usuario;
import com.sistema.gestion.repository.UsuarioRepository;
import com.sistema.gestion.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * SERVICIO (IMPLEMENTACION) - UsuarioServiceImpl
 * ============================================================================
 * @Service marca esta clase como un "bean" de la capa de negocio, para que
 * Spring la administre y la pueda inyectar (via @Autowired / constructor)
 * en los Controllers.
 *
 * @Transactional asegura que las operaciones que modifican el estado del
 * Usuario (por ejemplo sumar un intento fallido y, si corresponde,
 * bloquearlo) se ejecuten dentro de una unica transaccion de base de datos:
 * o se aplican todos los cambios, o ninguno.
 * ============================================================================
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Inyeccion de dependencias por constructor (practica recomendada en
     * Spring, en lugar de @Autowired sobre atributos). Spring detecta que
     * existe UN SOLO constructor y lo utiliza automaticamente para inyectar
     * UsuarioRepository (capa ORM) y PasswordEncoder (bean definido en
     * SecurityConfig) sin necesidad de anotarlo explicitamente.
     */
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new CorreoYaRegistradoException(
                    "Ya existe un usuario registrado con el correo " + usuario.getCorreo());
        }
        // La clave NUNCA se guarda en texto plano: se encripta con BCrypt.
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setIntentos(0);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Optional<Usuario> iniciarSesion(String correo, String passwordPlano) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            // El correo no esta registrado: segun el enunciado, en este caso
            // se debe invitar a la persona a registrarse (logica manejada
            // en el AuthController, que interpreta un Optional.empty()).
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.estaBloqueado()) {
            // Usuario ya bloqueado: no se vuelve a verificar la clave.
            return Optional.empty();
        }

        boolean passwordCorrecta = passwordEncoder.matches(passwordPlano, usuario.getPassword());

        // Se delega la logica de negocio (contar intentos / bloquear) al
        // propio Modelo (Usuario.iniciarSesion), respetando el metodo
        // definido en el diagrama UML.
        boolean loginExitoso = usuario.iniciarSesion(passwordCorrecta);

        // Persistimos el nuevo estado (intentos/estado) del usuario, tanto
        // si el login fue exitoso (reseteo de intentos) como si fallo
        // (incremento de intentos / posible bloqueo).
        usuarioRepository.save(usuario);

        return loginExitoso ? Optional.of(usuario) : Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existeDocumento(String documento) {
        return usuarioRepository.existsByDocumento(documento);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No existe el usuario con id " + id));
    }

    @Override
    @Transactional
    public void desbloquear(Long usuarioId) {
        Usuario usuario = buscarPorId(usuarioId);
        // Delega en el metodo de dominio definido en el UML
        // (Usuario.resetearIntentos), que ademas reactiva el estado.
        usuario.resetearIntentos();
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
