package es.ivan.parser.data.mysql;

import lombok.Getter;
import lombok.ToString;

@ToString
public class CocheCSV {

    @Getter private final String nombreCoche;
    @Getter private final String color;
    @Getter private final int year;

    public CocheCSV(String nombreCoche, String color, int year) {
        this.nombreCoche = nombreCoche;
        this.color = color;
        this.year = year;
    }
}
