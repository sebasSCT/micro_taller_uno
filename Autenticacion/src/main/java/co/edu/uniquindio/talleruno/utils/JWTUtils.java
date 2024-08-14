package co.edu.uniquindio.talleruno.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${jwt.secret}")
    private String claveSecreta;

    public String generarToken(String nombre) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(nombre)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(20L, ChronoUnit.DAYS)))
                .setIssuer("ingesis.uniquindio.edu.co") // Agrega el emisor al payload
                .signWith(getKey())
                .setHeaderParam("typ", "JWT") // Agrega el tipo de token al header
                .compact();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String parseJwt(String jwtString) throws ExpiredJwtException,
            UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, JsonProcessingException, IOException {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(jwtString);

        String subject = jws.getBody().getSubject();
        JsonNode jsonNode = objectMapper.readTree(subject);
        return jsonNode.get("nombre").asText();
    }

    private Key getKey(){
        return new SecretKeySpec(Base64.getDecoder().decode(claveSecreta),
                SignatureAlgorithm.HS256.getJcaName());
    }
}