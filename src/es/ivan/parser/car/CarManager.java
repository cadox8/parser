package es.ivan.parser.car;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import es.ivan.parser.utils.MySQL;
import es.ivan.parser.utils.Oracle;
import lombok.Getter;

import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarManager {

    private final MySQL mysql;
    private final Oracle oracle;

    @Getter private final List<Coche> coches;

    public CarManager() {
        this.mysql = new MySQL("localhost", "3306", "coches", "root", "123456");
        this.oracle = new Oracle("oracle.iesjulianmarias.es", "1521", "coches", "damd108", "damd108");

        this.coches = new ArrayList<>();
    }

    public void load() {
        try {
            this.loadMySQL();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos de MySQL");
            e.printStackTrace();
        }

        this.coches.forEach(System.out::println);

        try {
            this.loadCSV();
        } catch (IOException | CsvException e) {
            System.err.println("Error al cargar los datos del CSV");
            e.printStackTrace();
        }

        System.out.println("\n\n\n");
        this.coches.forEach(System.out::println);

/*        try {
            this.testOracle();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    private void loadMySQL() throws SQLException, ClassNotFoundException {
        // Load from MySQL
        final PreparedStatement statement = this.mysql.openConnection().prepareStatement("SELECT * FROM `coches`");
        final ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            this.coches.add(new Coche(rs.getString("nombreCoche"), rs.getString("modelo"),
                    rs.getInt("year"), rs.getFloat("vMax"), rs.getInt("cilindrada")) );
        }
    }

    private void loadCSV() throws IOException, CsvException {
        // Load from CSV
        final CSVReader reader = new CSVReaderBuilder(new FileReader("./file1.csv")).withSkipLines(1).build();
        reader.readAll().forEach(l -> {
            this.coches.stream().filter(c -> c.getNombreCoche().equalsIgnoreCase(l[0]) && c.getYear() == Integer.parseInt(l[2])).findAny().ifPresent(coche -> coche.setColor(l[1]));
        });
    }

    private void testOracle() throws SQLException, ClassNotFoundException {
        final PreparedStatement statement = this.oracle.openConnection().prepareStatement("SELECT * FROM COCHES");
        final ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("NOMBRE_CTA"));
        }
    }
}
