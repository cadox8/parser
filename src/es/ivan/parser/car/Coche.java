package es.ivan.parser.car;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Coche {

    @Getter private final String nombreCoche;
    @Getter private final String modelo;
    @Getter private final int year;
    @Getter private final float vMax;
    @Getter private final int cilindrada;

    @Getter @Setter private String color;

    public Coche(String nombreCoche, String modelo, int year, float vMax, int cilindrada) {
        this.nombreCoche = nombreCoche;
        this.modelo = modelo;
        this.year = year;
        this.vMax = vMax;
        this.cilindrada = cilindrada;
    }
}