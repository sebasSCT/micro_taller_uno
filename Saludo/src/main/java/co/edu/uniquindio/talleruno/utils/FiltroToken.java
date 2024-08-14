package co.edu.uniquindio.talleruno.utils;

import co.edu.uniquindio.talleruno.dtos.MensajeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FiltroToken extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;

    @Override
    public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        // Configuración de cabeceras para CORS
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.addHeader("Access-Control-Allow-Headers", "Origin, Accept, Content-Type,Authorization");
                res.addHeader("Access-Control-Allow-Credentials", "true");
        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
        }else {
            String requestURI = req.getRequestURI();
            String token = getToken(req);
            System.out.println(token);
            boolean error = true;

            try {
                if (requestURI.startsWith("/saludo")) {
                    if (token != null) {
                        String nombreEnToken = jwtUtils.parseJwt(token);
                        String nombreEnParametro = req.getParameter("nombre");

                        if (nombreEnParametro == null || !nombreEnParametro.equals(nombreEnToken)) {
                            crearRespuestaError("El nombre en el parámetro no coincide con el nombre en el token", HttpServletResponse.SC_FORBIDDEN, res);
                            return;

                        } else {
                            error = false;
                        }
                    } else {
                        crearRespuestaError("No hay un Token", HttpServletResponse.SC_FORBIDDEN,
                                res);
                    }
                } else {
                    error = false;
                }
            } catch (MalformedJwtException | SignatureException e) {
                crearRespuestaError("El token es incorrecto",
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res);
            } catch (ExpiredJwtException e) {
                crearRespuestaError("El token está vencido",
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res);
            } catch (Exception e) {
                crearRespuestaError(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        res);
            }
            if (!error) {
                filterChain.doFilter(req, res);
            }
        }
    }

    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer "))
            return header.replace("Bearer ", "");
        return null;
    }

    private void crearRespuestaError(String mensaje, int codigoError, HttpServletResponse
            response) throws IOException {
        MensajeDTO<String> dto = new MensajeDTO<>(true, mensaje);
        response.setContentType("application/json");
        response.setStatus(codigoError);
        response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
