package com.microservicios.talleruno.utils;

import jakarta.validation.constraints.NotBlank;

public record TokenDTO(
        @NotBlank
        String token) {
}
