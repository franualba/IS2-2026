package com.is2.tp1ej6ind.service;

import com.is2.tp1ej6ind.model.Profesor;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado del envío de mensajes por correo.
 *
 * Se usa para notificar al docente tras su registro, cumpliendo el requisito de
 * enviar un correo de bienvenida al personal email registrado.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(Profesor profesor) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(profesor.getEmail());
            helper.setSubject("Bienvenido al sistema escolar");
            helper.setText(buildWelcomeHtml(profesor), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("No se pudo enviar el correo de bienvenida", e);
        }
    }

    private String buildWelcomeHtml(Profesor profesor) {
        return "<html><body style='font-family:Arial,sans-serif; background:#f4f6fa; padding:24px;'>"
            + "<div style='max-width:600px; margin:auto; background:white; padding:28px; border-radius:12px; box-shadow:0 2px 12px rgba(0,0,0,0.08);'>"
            + "<h2 style='color:#0d6efd;'>Bienvenido/a, " + profesor.getNombre() + " " + profesor.getApellido() + "</h2>"
            + "<p>Tu registro como docente en el sistema escolar fue realizado con éxito.</p>"
            + "<p><strong>Usuario:</strong> " + profesor.getEmail() + "</p>"
            + "<p>Puedes iniciar sesión con tu correo personal y la contraseña que definiste durante el registro.</p>"
            + "<p style='margin-top:20px;'>Saludos cordiales,<br>Administración del Colegio</p>"
            + "</div></body></html>";
    }
}
