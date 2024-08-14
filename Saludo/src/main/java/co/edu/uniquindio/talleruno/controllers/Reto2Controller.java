package co.edu.uniquindio.talleruno.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Reto2Controller {

    @GetMapping("/saludo")
    public ResponseEntity<String> saludo (@RequestParam(value="nombre", required = false) String nombre)
    {
        if (nombre != null && !nombre.isEmpty()) {
            return ResponseEntity.ok("Hola " + nombre);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El nombre es obligatorio");
        }
    }

    @RequestMapping("*")
    public ResponseEntity<String> manejarRutasNoEncontradas() {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Recurso no encontrado");

    }
}
