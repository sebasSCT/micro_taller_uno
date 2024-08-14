package co.edu.uniquindio.talleruno.dtos;

public record MensajeDTO<T>(
        boolean error,
        T respuesta
) {
}