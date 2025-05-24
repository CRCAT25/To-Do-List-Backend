package net.thanhdevjava.to_do_list.security.custom;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String message = "Unauthorized";

        if (authException instanceof DisabledException) {
            message = "Your account has not been activated yet.";
        }
        else if (authException instanceof BadCredentialsException) {
            message = "Invalid username or password.";
        }
        else if (authException instanceof CredentialsExpiredException) {
            message = "Your credentials have expired.";
        }
        else {
            message = authException.getMessage(); // fallback: show actual exception message
        }

        response.getWriter().write("{\"success\": false, \"message\": \"" + message + "\"}");
    }
}
