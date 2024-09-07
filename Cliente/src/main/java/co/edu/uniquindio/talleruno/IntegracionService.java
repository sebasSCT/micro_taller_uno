package co.edu.uniquindio.talleruno;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class IntegracionService {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${saludo.service.url}")
    private String saludoServiceUrl;

    public IntegracionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String obtenerToken(String nombre) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        String nameBody = nombre;
        HttpEntity<String> request = new HttpEntity<>(nameBody, headers);

        ResponseEntity<String> respuesta = restTemplate.postForEntity(
                authServiceUrl,
                request,
                String.class
        );

        String respuestaBody = respuesta.getBody();
        if (respuestaBody != null) {
            return respuestaBody;
        } else {
            throw new RuntimeException("Error al obtener el token");
        }
    }

    public String obtenerSaludo(String nombre, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = saludoServiceUrl + "?nombre=" + URLEncoder.encode(nombre, StandardCharsets.UTF_8);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}