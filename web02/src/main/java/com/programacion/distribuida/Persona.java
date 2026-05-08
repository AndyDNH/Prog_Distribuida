package com.programacion.distribuida;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Persona {
    private String name;
    private LocalDate hora;
}
