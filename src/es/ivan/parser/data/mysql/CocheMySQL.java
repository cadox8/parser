package es.ivan.parser.data.mysql;

import lombok.Getter;
import lombok.ToString;

@ToString
public class CocheMySQL {

    @Getter private final String nombreCoche;
    @Getter private final String modelo;
    @Getter private final int year;
    @Getter private final float vMax;
    @Getter private final int cilindrada;

    public CocheMySQL(String nombreCoche, String modelo, int year, float vMax, int cilindrada) {
        this.nombreCoche = nombreCoche;
        this.modelo = modelo;
        this.year = year;
        this.vMax = vMax;
        this.cilindrada = cilindrada;
    }
}
