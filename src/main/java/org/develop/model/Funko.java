package org.develop.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Funko {
    private UUID uuid;
    private String name;
    private Modelo modelo;
    private double precio;
    private LocalDate fecha_lanzamiento;
}
