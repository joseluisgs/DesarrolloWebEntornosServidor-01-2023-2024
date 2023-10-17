package dev.joseluisgs.common.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private final String fileName;
    private final Properties properties;

    public PropertiesReader(String fileName) throws IOException {
        this.fileName = fileName;
        properties = new Properties();

        InputStream file = getClass().getClassLoader().getResourceAsStream(fileName);
        if (file != null) {
            properties.load(file);
        } else {
            throw new FileNotFoundException("No se encuentra el fichero " + fileName);
        }
    }

    public String getProperty(String key) throws FileNotFoundException {
        String value = properties.getProperty(key);
        if (value != null) {
            return value;
        } else {
            throw new FileNotFoundException("No se encuentra la propiedad " + key + " en el fichero " + fileName);
        }
    }
}