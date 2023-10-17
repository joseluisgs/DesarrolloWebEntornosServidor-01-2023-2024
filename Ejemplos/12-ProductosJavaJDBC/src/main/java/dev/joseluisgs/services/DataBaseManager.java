package dev.joseluisgs.services;


import lombok.NonNull;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/**
 * Controlador de Bases de Datos Relacionales
 */
public class DataBaseManager implements Closeable {
    private static DataBaseManager controller;
    private final Logger logger = LoggerFactory.getLogger(DataBaseManager.class);
    String serverUrl;
    String serverPort;
    String dataBaseName;
    private String user;
    private String password;
    private String initScript;
    /*
    Tipos de Driver
    SQLite: "org.sqlite.JDBC";
    MySQL: "com.mysql.jdbc.Driver"
    MariaDB: com.mysql.cj.jdbc.Driver
     */
    private String jdbcDriver;
    private String connectionUrl;
    // Para manejar las conexiones y respuestas de las mismas
    private Connection connection;
    private PreparedStatement preparedStatement;

    /**
     * Constructor privado para Singleton
     */
    private DataBaseManager() {
        logger.debug("Inicializando el controlador de base de datos");
        initConfig();
    }

    /**
     * Devuelve una instancia del controlador
     *
     * @return instancia del controladorBD
     */
    public static DataBaseManager getInstance() {
        if (controller == null) {
            controller = new DataBaseManager();
        }
        return controller;
    }

    /**
     * Carga la configuración de acceso al servidor de Base de Datos
     * Puede ser directa "hardcodeada" o asignada dinámicamente a traves de ficheros .env o properties
     */
    private void initConfig() {
        // Leemos el fichero de configuración
        var propsFile = ClassLoader.getSystemResource("config.properties").getFile();
        var props = new Properties();
        try {
            props.load(new FileInputStream(propsFile));
            // Comentar o ajustar segun el tipo de base de datos y propiedades que se quieran usar
            serverUrl = props.getProperty("database.url", "localhost");
            serverPort = props.getProperty("database.port", "3306");
            dataBaseName = props.getProperty("database.name", "AppDatabase");
            jdbcDriver = props.getProperty("database.driver", "org.h2.Driver");
            user = props.getProperty("database.user", "sa");
            password = props.getProperty("database.password", "");

            connectionUrl =
                    props.getProperty("database.connectionUrl", "jdbc:h2:mem:" + dataBaseName + ";DB_CLOSE_DELAY=-1");
            initScript = props.getProperty("database.initScript", ClassLoader.getSystemResource("init.sql").getFile());

            logger.debug("Configuración de acceso a la Base de Datos cargada");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Abre la conexión con el servidor  de base de datos
     *
     * @throws SQLException Servidor no accesible por problemas de conexión o datos de acceso incorrectos
     */
    public void open() throws SQLException {
        // Obtenemos la conexión
        if (connection != null && !connection.isClosed()) {
            logger.debug("Conexión a la Base de Datos ya establecida");
            return;
        }
        connection = DriverManager.getConnection(connectionUrl, user, password);
        logger.debug("Conexión a la Base de Datos establecida: " + connectionUrl);
    }

    /**
     * Cierra la conexión con el servidor de base de datos
     *
     * @throws SQLException Servidor no responde o no puede realizar la operación de cierre
     */
    public void close() {
        try {
            if (preparedStatement != null)
                preparedStatement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Realiza una consulta a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     *
     * @param querySQL consulta SQL de tipo select
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe
     */
    private ResultSet executeQuery(@NonNull String querySQL, Object... params) throws SQLException {
        this.open();

        logger.debug("Ejecutando consulta: " + querySQL + " con parametros: " + Arrays.toString(params));
        preparedStatement = connection.prepareStatement(querySQL);
        // Vamos a pasarle los parametros usando preparedStatement
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }

    /**
     * Realiza una consulta select a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     * Quitar el Optional si no se quiere usar
     *
     * @param querySQL consulta SQL de tipo select
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe
     */
    public Optional<ResultSet> select(@NonNull String querySQL, Object... params) throws SQLException {
        return Optional.of(executeQuery(querySQL, params));
    }

    /**
     * Realiza una consulta select a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     *
     * @param querySQL consulta SQL de tipo select
     * @param limit    número de registros de la página
     * @param offset   desplazamiento de registros o número de registros ignorados para comenzar la devolución
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe o el desplazamiento es mayor que el número de registros
     */
    public Optional<ResultSet> select(@NonNull String querySQL, int limit, int offset, Object... params) throws SQLException {
        String query = querySQL + " LIMIT " + limit + " OFFSET " + offset;
        return Optional.of(executeQuery(query, params));
    }

    /**
     * Realiza una consulta de tipo insert de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param insertSQL consulta SQL de tipo insert
     * @param params    parámetros de la consulta parametrizada
     * @return Clave del registro insertado
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    public Optional<ResultSet> insertAndGetKey(@NonNull String insertSQL, Object... params) throws SQLException {
        this.open();

        logger.debug("Ejecutando consulta: " + insertSQL + " con parametros: " + Arrays.toString(params));
        // Con return generated keys obtenemos las claves generadas is las claves es autonumerica por ejemplo
        preparedStatement = connection.prepareStatement(insertSQL, preparedStatement.RETURN_GENERATED_KEYS);
        // Vamos a pasarle los parametros usando preparedStatement
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        preparedStatement.executeUpdate();
        return Optional.of(preparedStatement.getGeneratedKeys());
    }

    // USO esta función porque los UUID los genero desde la propia aplicación
    public int insert(@NonNull String insertSQL, Object... params) throws SQLException {
        return updateQuery(insertSQL, params);
    }

    /**
     * Realiza una consulta de tipo update de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param updateSQL consulta SQL de tipo update
     * @param params    parámetros de la consulta parametrizada
     * @return número de registros actualizados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    public int update(@NonNull String updateSQL, Object... params) throws SQLException {
        return updateQuery(updateSQL, params);
    }

    /**
     * Realiza una consulta de tipo delete de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param deleteSQL consulta SQL de tipo delete
     * @param params    parámetros de la consulta parametrizada
     * @return número de registros eliminados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    public int delete(@NonNull String deleteSQL, Object... params) throws SQLException {
        return updateQuery(deleteSQL, params);
    }

    /**
     * Realiza una consulta de tipo update, es decir que modifca los datos, de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param genericSQL consulta SQL de tipo update, delete, creted, etc.. que modifica los datos
     * @param params     parámetros de la consulta parametrizada
     * @return número de registros eliminados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    private int updateQuery(@NonNull String genericSQL, Object... params) throws SQLException {
        this.open();

        logger.debug("Ejecutando consulta: " + genericSQL + " con parametros: " + Arrays.toString(params));

        // Con return generated keys obtenemos las claves generadas
        preparedStatement = connection.prepareStatement(genericSQL);
        // Vamos a pasarle los parametros usando preparedStatement
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeUpdate();
    }

    public int createTables(String genericSQL) throws SQLException {
        logger.debug("Creando tablas: " + genericSQL);
        return updateQuery(genericSQL);
    }

    private void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    private void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    private void rollback() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }


    public void initData(@NonNull String sqlFile, boolean logWriter) throws FileNotFoundException, SQLException {
        logger.debug("Inicializando datos de fichero: " + sqlFile + " con logWriter: " + logWriter);
        this.open();
        var sr = new ScriptRunner(connection); // Si estas con H2, puedes usar RunScript
        var reader = new BufferedReader(new FileReader(sqlFile));
        if (logWriter) {
            sr.setLogWriter(new PrintWriter(System.out));
        } else {
            sr.setLogWriter(null);
        }
        sr.runScript(reader);
    }
}
