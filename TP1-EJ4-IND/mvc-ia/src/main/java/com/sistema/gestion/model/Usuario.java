package com.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * MODELO (CAPA M de MVC) - Usuario
 * ============================================================================
 * Extiende Persona (herencia "Extends" del UML). Representa a una persona
 * que puede iniciar sesion en el sistema como usuario "comun" (no administrador).
 *
 * CANTIDAD MAXIMA DE INTENTOS FALLIDOS PERMITIDOS: 3 (segun el enunciado).
 * Al llegar al 3er intento fallido, el usuario queda BLOQUEADO y no puede
 * volver a iniciar sesion hasta que un Administrador lo desbloquee
 * (ver Administrador.desbloquearUsuario()).
 *
 * -----------------------------------------------------------------------
 * DIFERENCIAS / MEJORAS RESPECTO DEL DIAGRAMA UML ORIGINAL (documentadas
 * segun lo solicitado, ya que se detecto una inconsistencia menor):
 * -----------------------------------------------------------------------
 * El UML declaraba simultaneamente:
 *      -bloqueado: Boolean                (atributo de Usuario)
 *      ENUM estadoUsuario { ACTIVO, BLOQUEADO }  1...* --> Usuario
 * Estos dos elementos representan el MISMO concepto (si el usuario esta
 * bloqueado o no) de dos formas redundantes/potencialmente inconsistentes
 * (por ejemplo bloqueado=false pero estado=BLOQUEADO). Para evitar esa
 * inconsistencia se UNIFICO el concepto usando unicamente el atributo
 * "estado: EstadoUsuario", que es el que efectivamente contiene el enum
 * mostrado en el diagrama. Los metodos bloquear() y resetearIntentos()
 * actuan ahora sobre este unico atributo "estado".
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Contrasena del usuario. Se almacena SIEMPRE encriptada (BCrypt),
     * nunca en texto plano. Ver UsuarioServiceImpl.registrar() /
     * PasswordEncoder.
     */
    @NotBlank
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Cantidad de intentos fallidos de inicio de sesion consecutivos.
     * Se reinicia a 0 cada vez que el usuario ingresa correctamente,
     * o cuando un administrador lo desbloquea.
     */
    @Column(nullable = false)
    private int intentos = 0;

    /**
     * Estado actual del usuario (ACTIVO / BLOQUEADO). Reemplaza al atributo
     * "bloqueado: Boolean" del UML original (ver nota de mejora arriba).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    public static final int MAX_INTENTOS = 3;

    /**
     * +iniciarSesion(password): boolean
     * Verifica la contrasena ingresada contra la almacenada (ya encriptada).
     * La comparacion real de hash se delega al PasswordEncoder desde el
     * Service (esta clase de Modelo no depende de Spring), por eso este
     * metodo recibe un booleano ya calculado ("passwordCorrecta") indicando
     * si la clave coincide, y se encarga de la LOGICA DE NEGOCIO de intentos
     * y bloqueo, que es responsabilidad genuina del Modelo.
     *
     * @param passwordCorrecta resultado de comparar la clave ingresada
     *                          contra el hash almacenado.
     * @return true si el login fue exitoso, false en caso contrario.
     */
    public boolean iniciarSesion(boolean passwordCorrecta) {
        if (this.estado == EstadoUsuario.BLOQUEADO) {
            return false;
        }
        if (passwordCorrecta) {
            resetearIntentos();
            return true;
        } else {
            sumarIntentoFallido();
            return false;
        }
    }

    /** +sumarIntentoFallido(): void */
    public void sumarIntentoFallido() {
        this.intentos++;
        if (this.intentos >= MAX_INTENTOS) {
            bloquear();
        }
    }

    /** +bloquear(): void */
    public void bloquear() {
        this.estado = EstadoUsuario.BLOQUEADO;
    }

    /** +resetearIntentos(): void */
    public void resetearIntentos() {
        this.intentos = 0;
        if (this.estado == EstadoUsuario.BLOQUEADO) {
            // Un reseteo explicito (realizado por un administrador) tambien
            // reactiva al usuario.
            this.estado = EstadoUsuario.ACTIVO;
        }
    }

    public boolean estaBloqueado() {
        return this.estado == EstadoUsuario.BLOQUEADO;
    }
}
