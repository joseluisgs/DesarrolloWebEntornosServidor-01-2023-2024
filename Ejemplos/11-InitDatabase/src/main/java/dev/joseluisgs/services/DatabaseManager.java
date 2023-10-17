package dev.joseluisgs.services;


import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase que gestiona la base de datos
 */
public class DatabaseManager implements AutoCloseable {
    private static DatabaseManager instance;
    private final boolean initTables = false; // Deberíamos inicializar las tablas? Fichero de configuración
    private final String url = "jdbc:sqlite:test.db"; // Fichero de configuración se lee en el constructor
    private Connection conn;

    // Constructor privado para que no se pueda instanciar Singleton
    private DatabaseManager() {
        // Aquñi leeriamos el fichero de configuración.properties
        // Y estableceriamos la url de la base de datos y si hay que inicializar las tablas
        // Usamos Properties
        /*
        Properties props = new Properties();
        try {
            props.load(DatabaseManager.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.url = props.getProperty("url");
        this.initTables = Boolean.parseBoolean(props.getProperty("initTables"));
        */

        try {
            openConnection();
            if (initTables) {
                initTables();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para obtener la instancia de la base de datos
     * Lo ideal e
     *
     * @return
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void openConnection() throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    private void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    /**
     * Método para inicializar la base de datos y las tablas
     * Esto puede ser muy complejo y mejor usar un script, ademas podemos usar datos de ejemplo en el script
     */
    private void initTables() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS alumno (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT)");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para ejecutar un script de SQL
     *
     * @param scriptSqlFile nombre del fichero de script SQL
     * @param logWriter     si queremos que nos muestre el log de la ejecución
     * @throws FileNotFoundException
     */
    public void executeScript(String scriptSqlFile, boolean logWriter) throws FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(conn);
        var file = ClassLoader.getSystemResource(scriptSqlFile).getFile();
        Reader reader = new BufferedReader(new FileReader(file));
        sr.setLogWriter(logWriter ? new PrintWriter(System.out) : null);
        sr.runScript(reader);
    }


    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                openConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    @Override
    public void close() throws SQLException {
        closeConnection();
    }
}
