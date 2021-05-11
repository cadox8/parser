package es.ivan.parser;

import es.ivan.parser.car.CarManager;

import java.io.*;
import java.util.Properties;

public class Launcher {

    public static void main(String[] args) {
        // Check files
        try {
            System.out.println("Comprobando archivos . . . ✓");
            final File f = new File("./file1.csv");
            if (!f.exists()) throw new FileNotFoundException("Illo, que no existe el archivo!");

            final File f3 = new File("./config.config");
            if (!f.exists()) throw new FileNotFoundException("Illo, que no existe el archivo!");

            final File f2 = new File("./file2.csv");
            if (!f2.exists()) f2.createNewFile();

            System.out.println("Cargando configuración . . . ✓");
            final Properties prop = new Properties();
            prop.load(new FileReader(f3));

            System.out.println("Inicio del programa . . . ✓");
            final CarManager carManager = new CarManager(prop);
            carManager.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
