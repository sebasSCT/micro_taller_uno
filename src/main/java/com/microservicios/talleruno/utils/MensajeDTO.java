package com.microservicios.talleruno.utils;


public record MensajeDTO<T>(
        boolean error,
        T respuesta
) {
}