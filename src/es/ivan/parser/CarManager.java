package es.ivan.parser;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import es.ivan.parser.data.mysql.CocheCSV;
import es.ivan.parser.data.mysql.CocheMySQL;
import es.ivan.parser.utils.MySQL;
import es.ivan.parser.utils.Oracle;
import lombok.Getter;

import java.io.FileNotFoundException;
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

    @Getter private final List<CocheMySQL> cocheMySQLS;
    @Getter private final List<CocheCSV> cocheCSVS;

    public CarManager() {
        this.mysql = new MySQL("localhost", "3306", "coches", "root", "");
        this.oracle = new Oracle("oracle.iesjulianmarias.es", "1521", "coches", "damd108", "damd108");

        this.cocheMySQLS = new ArrayList<>();
        this.cocheCSVS = new ArrayList<>();
    }

    public void load() {
        try {
            this.loadMySQL();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos de MySQL");
            e.printStackTrace();
        }

        try {
            this.loadCSV();
        } catch (IOException | CsvException e) {
            System.err.println("Error al cargar los datos del CSV");
            e.printStackTrace();
        }

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
            this.cocheMySQLS.add(new CocheMySQL(rs.getString("nombreCoche"), rs.getString("modelo"),
                    rs.getInt("year"), rs.getFloat("vMax"), rs.getInt("cilindrada")) );
        }
    }

    private void loadCSV() throws IOException, CsvException {
        // Load from CSV
        final CSVReader reader = new CSVReaderBuilder(new FileReader("./file1.csv")).withSkipLines(1).build();
        reader.readAll().forEach(l -> this.cocheCSVS.add(new CocheCSV(l[0], l[1], Integer.parseInt(l[2]))));
    }

    private void testOracle() throws SQLException, ClassNotFoundException {
        final PreparedStatement statement = this.oracle.openConnection().prepareStatement("SELECT * FROM COCHES");
        final ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("NOMBRE_CTA"));
        }
    }
}
