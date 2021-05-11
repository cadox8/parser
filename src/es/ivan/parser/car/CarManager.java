package es.ivan.parser.car;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import es.ivan.parser.utils.Database;
import lombok.Getter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarManager {

    private final Database mysql;
    private final Database oracle;

    @Getter private final List<Coche> coches;

    public CarManager(Properties prop) {
        this.mysql = new Database(prop.getProperty("mysql.host"), prop.getProperty("mysql.port"), prop.getProperty("mysql.database"), prop.getProperty("mysql.user"), prop.getProperty("mysql.password"));
        this.oracle = new Database(prop.getProperty("oracle.host"), prop.getProperty("oracle.port"), prop.getProperty("oracle.database"), prop.getProperty("oracle.user"), prop.getProperty("oracle.password"));

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

        try {
            this.createOracle();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            this.createOracleCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMySQL() throws SQLException, ClassNotFoundException {
        // Load from MySQL
        System.out.println("Cargando datos de MySQL");
        final PreparedStatement statement = this.mysql.openMySQLConnection().prepareStatement("SELECT * FROM `coches`");
        final ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            this.coches.add(new Coche(rs.getString("nombreCoche"), rs.getString("modelo"),
                    rs.getInt("year"), rs.getFloat("vMax"), rs.getInt("cilindrada")) );
        }
    }

    private void loadCSV() throws IOException, CsvException {
        // Load from CSV
        System.out.println("Cargando datos del CSV");
        final CSVReader reader = new CSVReaderBuilder(new FileReader("./file1.csv")).withSkipLines(1).build();
        reader.readAll().forEach(l -> {
            this.coches.stream().filter(c -> c.getNombreCoche().equalsIgnoreCase(l[0]) && c.getYear() == Integer.parseInt(l[2])).findAny().ifPresent(coche -> coche.setColor(l[1]));
        });
    }

    private void createOracle() throws SQLException, ClassNotFoundException {
        System.out.println("Metiendo datos a Oracle");
        final String dropTable = "drop table coches purge";
        final String createTable = "create table coches (" +
                "nombreCoche VARCHAR2(100), " +
                "modelo VARCHAR2(100), " +
                "year NUMBER(4), " +
                "color VARCHAR2(50), " +
                "vMax NUMBER(4), " +
                "cilindrada NUMBER(5) " +
                "CONSTRAINT coches_pk PRIMARY KEY(nombreCoche)"
                + ")";
        final String insertTable = "insert into coches (nombreCoche, modelo, year, color, vMax, cilindrada) values (?, ?, ?, ?, ?, ?)";

        final PreparedStatement statement = this.oracle.openOracleConnection().prepareStatement(dropTable);
        statement.executeQuery();
        statement.executeUpdate(createTable);

        // Insert values
        final PreparedStatement insertStatement = this.oracle.openOracleConnection().prepareStatement(insertTable);

        for (Coche c : this.coches) {
            insertStatement.setString(1, c.getNombreCoche());
            insertStatement.setString(2, c.getModelo());
            insertStatement.setInt(3, c.getYear());
            insertStatement.setString(4, c.getColor());
            insertStatement.setFloat(5, c.getVMax());
            insertStatement.setInt(6, c.getCilindrada());
            insertStatement.addBatch();
        }
    }

    private void createOracleCSV() throws IOException {
        System.out.println("Creando CSV con nuevos datos");
        final CSVWriter writer = new CSVWriter(new FileWriter("./file2.csv"));
        writer.writeNext(new String[]{ "nombreCoche", "year", "cantidad" });
        this.coches.forEach(c -> writer.writeNext(new String[]{ c.getNombreCoche(), String.valueOf(c.getYear()),
                String.valueOf(this.coches.stream().filter(c2 -> c2.getNombreCoche().equalsIgnoreCase(c.getNombreCoche())).count()) }));
        writer.flush();
        writer.close();
    }
}
