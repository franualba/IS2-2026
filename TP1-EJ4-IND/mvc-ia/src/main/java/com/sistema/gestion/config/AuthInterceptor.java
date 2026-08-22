package com.sistema.gestion.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ============================================================================
 * CONFIGURACION - AuthInterceptor
 * ============================================================================
 * Interceptor de Spring MVC que protege las rutas privadas del sistema
 * (todo lo que empieza con "/home", "/admin", "/productos", "/inventario",
 * "/compras"), verificando que exista una sesion HTTP activa
 * (HttpSession con atributo USUARIO_LOGUEADO o ADMIN_LOGUEADO).
 *
 * Si no hay sesion activa, redirige al login. Este componente reemplaza,
 * de forma liviana, a lo que en un proyecto con Spring Security completo
 * seria una cadena de filtros de seguridad.
 * ============================================================================
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);
        boolean logueado = session != null &&
                (session.getAttribute("USUARIO_LOGUEADO") != null
                        || session.getAttribute("ADMIN_LOGUEADO") != null);

        String path = request.getRequestURI();

        // Las rutas de administracion requieren ademas ser Administrador.
        boolean esRutaAdmin = path.startsWith("/admin");
        boolean esAdminLogueado = session != null && session.getAttribute("ADMIN_LOGUEADO") != null;

        if (!logueado) {
            response.sendRedirect("/login");
            return false;
        }
        if (esRutaAdmin && !esAdminLogueado) {
            response.sendRedirect("/home");
            return false;
        }
        return true;
    }
}
