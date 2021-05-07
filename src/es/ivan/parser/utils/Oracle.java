package es.ivan.parser.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Oracle {

    protected Connection connection;

    private final String user, database, password, port, hostname;

    public Oracle(String hostname, String port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
    }

    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public Connection getConnection() {
        return connection;
    }

    public Connection openConnection() throws SQLException, ClassNotFoundException {
        if (checkConnection()) return connection;
        Class.forName ("oracle.jdbc.driver.OracleDriver");
        connection = DriverManager.getConnection("jdbc:oracle:thin:@" + this.hostname + ":" + this.port + ":orcl", this.user, this.password);
        return connection;
    }

    public boolean closeConnection() throws SQLException {
        if (connection == null) return false;
        connection.close();
        return true;
    }
}
