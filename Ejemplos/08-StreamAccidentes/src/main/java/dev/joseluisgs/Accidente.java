package dev.joseluisgs;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Accidente {
    private final String numExpediente;
    private final LocalDate fecha;
    private final LocalTime hora;
    private final String localizacion;
    private final String distrito;
    private final String tipoAccidente;
    private final String meteorologia;
    private final String tipoVehiculo;
    private final String tipoPersona;
    private final String sexo;
    private final boolean positivoAlcohol;
    private final boolean positivoDrogas;
}
