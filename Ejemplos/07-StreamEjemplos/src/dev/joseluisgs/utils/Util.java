package dev.joseluisgs.utils;

import dev.joseluisgs.model.Product;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Util {

    public static List<Product> getProducts() throws IOException {
        // Cargamos el archivo ubicado en la carpeta data
        String DATA_FILE = "products.csv";
        String WORKING_DIRECTORY = System.getProperty("user.dir");
        Path path = Paths.get(WORKING_DIRECTORY + File.separator + "data" + File.separator + DATA_FILE);
        // Puedo leer todo el fichero del tiron. Cuidado si no es muy grande!!
        final List<String> lineas = Files.readAllLines(path, StandardCharsets.UTF_8);
        // lines.forEach(System.out::println);
        // List<Product> products = new ArrayList<>();
        // Me he saltado la primera línea del archivo, porque es la cabecera
        /*for (int i = 1; i < lineas.size(); i++) {
            Product product = parseProduct(lineas.get(i));
            products.add(product);
        }*/
        // Mapeando cada línea a un objeto Product
        // Usamos Skip para saltarnos la primera línea
        return lineas.stream().skip(1).map(Util::parseProduct).collect(Collectors.toList());

    }

    public static List<Product> getProducts2() throws IOException {
        // Cargamos el archivo ubicado en la carpeta data
        String DATA_FILE = "products.csv";
        String WORKING_DIRECTORY = System.getProperty("user.dir");
        Path path = Paths.get(WORKING_DIRECTORY + File.separator + "data" + File.separator + DATA_FILE);
        // Puedo leer todo el fichero liena a linea usando API Stream y Programación Funcional
        // Mapeando cada línea a un objeto Product
        // Usamos Skip para saltarnos la primera línea
        return Files.lines(path, StandardCharsets.UTF_8)
                .skip(1).map(Util::parseProduct).collect(Collectors.toList());

    }

    private static Product parseProduct(String linea) {
        String[] campos = linea.split(",");
        Product product = new Product();
        product.setId(Integer.parseInt(campos[0]));
        product.setName(campos[1]);
        product.setSupplier(Integer.parseInt(campos[2]));
        product.setCategory(Integer.parseInt(campos[3]));
        product.setUnitPrice(Double.parseDouble(campos[5]));
        product.setUnitsInStock(Integer.parseInt(campos[6]));
        return product;
    }
}
