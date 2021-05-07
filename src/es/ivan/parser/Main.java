package es.ivan.parser;

import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws CsvException {
        // Check files
        try {
            final File f = new File("./file1.csv");
            if (!f.exists()) throw new FileNotFoundException("Illo, que no existe el archivo!");

            final File f2 = new File("./file2.csv");
            if (!f2.exists()) f2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final CarManager carManager = new CarManager();
        carManager.load();

        System.out.println("Coches MySQL");
        carManager.getCocheMySQLS().forEach(System.out::println);

        System.out.println("Coches CSV");
        carManager.getCocheCSVS().forEach(System.out::println);
    }
}
