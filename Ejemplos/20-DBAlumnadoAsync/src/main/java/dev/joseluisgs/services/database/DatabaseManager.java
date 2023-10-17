package dev.joseluisgs.services.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase que gestiona la base de datos
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private final HikariDataSource dataSource;
    private boolean databaseInitTables = false; // Deberíamos inicializar las tablas? Fichero de configuración
    private String databaseUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"; // Fichero de configuración se lee en el constructor
    private String databaseInitScript = "init.sql"; // Fichero de configuración se lee en el constructor
    private Connection conn;

    // Constructor privado para que no se pueda instanciar Singleton
    private DatabaseManager() {
        loadProperties();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        dataSource = new HikariDataSource(config);

        try (Connection conn = dataSource.getConnection()) {
            if (databaseInitTables) {
                initTables(conn);
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

    private synchronized void loadProperties() {
        logger.debug("Cargando fichero de configuración de la base de datos");
        try {
            var file = ClassLoader.getSystemResource("database.properties").getFile();
            var props = new Properties();
            props.load(new FileReader(file));
            // Establecemos la url de la base de datos
            databaseUrl = props.getProperty("database.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            databaseInitTables = Boolean.parseBoolean(props.getProperty("database.initTables", "false"));
            databaseInitScript = props.getProperty("database.initScript", "init.sql");
        } catch (IOException e) {
            logger.error("Error al leer el fichero de configuración de la base de datos " + e.getMessage());
        }
    }


    /**
     * Método para inicializar la base de datos y las tablas
     * Esto puede ser muy complejo y mejor usar un script, ademas podemos usar datos de ejemplo en el script
     */
    private synchronized void initTables(Connection conn) {
        try {
            executeScript(conn, databaseInitScript, true);
        } catch (FileNotFoundException e) {
            logger.error("Error al leer el fichero de inicialización de la base de datos " + e.getMessage());
        }
    }

    /**
     * Método para ejecutar un script de SQL
     *
     * @param conn
     * @param scriptSqlFile nombre del fichero de script SQL
     * @param logWriter     si queremos que nos muestre el log de la ejecución
     * @throws FileNotFoundException
     */
    public synchronized void executeScript(Connection conn, String scriptSqlFile, boolean logWriter) throws FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(conn);
        var file = ClassLoader.getSystemResource(scriptSqlFile).getFile();
        logger.debug("Ejecutando script de SQL " + file);
        Reader reader = new BufferedReader(new FileReader(file));
        sr.setLogWriter(logWriter ? new PrintWriter(System.out) : null);
        sr.runScript(reader);
    }


    public synchronized Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
