package com.colegio.servicio;

import com.colegio.modelo.Profesor;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * SERVICIO DE CORREO: envía el correo de BIENVENIDA al registrar un docente.
 *
 * Usa JavaMailSender (spring-boot-starter-mail) con SMTP configurado en
 * application.properties (Gmail con "contraseña de aplicación", o cualquier SMTP).
 *
 * @Async: el envío se ejecuta en un hilo separado para no bloquear la respuesta
 * HTTP del registro. Si el SMTP falla, se registra en el log y el registro del
 * docente NO se pierde (tolerancia a fallos).
 */
@Service
@RequiredArgsConstructor
public class EmailServicio {

    private static final Logger log = LoggerFactory.getLogger(EmailServicio.class);
    private final JavaMailSender mailSender;

    @Value("${app.mail.remitente}")
    private String remitente;

    @Async
    public void enviarBienvenida(Profesor profesor) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(remitente);
            helper.setTo(profesor.getUsuario()); // correo personal del docente
            helper.setSubject("¡Bienvenido al Sistema de Gestión Escolar!");

            // Cuerpo HTML simple (plantilla de correo).
            String html = """
                    <html><body style="font-family: Arial; color:#333;">
                      <h2 style="color:#0d6efd;">¡Hola %s %s!</h2>
                      <p>Tu cuenta docente fue creada correctamente.</p>
                      <p><b>Usuario de acceso:</b> %s</p>
                      <p>Ya puedes iniciar sesión en el sistema.</p>
                      <hr><small>Sistema de Gestión Escolar</small>
                    </body></html>
                    """.formatted(profesor.getNombre(), profesor.getApellido(), profesor.getUsuario());

            helper.setText(html, true); // true = contenido HTML
            mailSender.send(mensaje);
            log.info("Correo de bienvenida enviado a {}", profesor.getUsuario());
        } catch (Exception e) {
            // No interrumpir el flujo de registro si el SMTP falla.
            log.error("No se pudo enviar el correo de bienvenida: {}", e.getMessage());
        }
    }
}