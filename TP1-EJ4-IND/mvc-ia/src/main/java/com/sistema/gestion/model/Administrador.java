package com.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * MODELO (CAPA M de MVC) - Administrador
 * ============================================================================
 * Extiende Persona (herencia "Extends" del UML). Representa a un
 * administrador del sistema, con permisos para gestionar usuarios
 * (desbloquear cuentas, dar de alta/baja usuarios, etc.).
 *
 * Atributos UML: id, password, nivelAcceso.
 * Metodos UML: Usuario() [se interpreta como el constructor propio de esta
 *              clase, Administrador()], iniciarSesion(password): boolean,
 *              desbloquearUsuario(): void, gestionarUsuario(): void.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "administradores")
public class Administrador extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Nivel de acceso del administrador (por ejemplo: "TOTAL", "SOPORTE",
     * "SOLO_LECTURA"). Se modela como String libre, tal cual el UML.
     */
    @Column(name = "nivel_acceso", length = 50)
    private String nivelAcceso;

    /**
     * +iniciarSesion(password): boolean
     * Al igual que en Usuario, la comparacion criptografica de la clave se
     * realiza en la capa de Service (que si tiene acceso al PasswordEncoder
     * de Spring); este metodo de Modelo solo expresa la regla de negocio:
     * un Administrador no tiene limite de intentos ni se bloquea (regla que
     * el enunciado solo define para "Usuario").
     */
    public boolean iniciarSesion(boolean passwordCorrecta) {
        return passwordCorrecta;
    }

    /**
     * +desbloquearUsuario(): void
     * La implementacion real (que requiere acceso al repositorio de
     * Usuario) se encuentra en AdministradorServiceImpl; este metodo
     * de dominio se deja documentado aqui para respetar el UML, y actua
     * directamente sobre la instancia de Usuario que se le pasa.
     */
    public void desbloquearUsuario(Usuario usuario) {
        usuario.resetearIntentos();
    }

    /**
     * +gestionarUsuario(): void
     * Operacion generica de administracion (alta/baja/modificacion),
     * implementada de forma concreta en AdministradorService /
     * UsuarioService segun la operacion puntual (crear, editar, eliminar).
     */
}
