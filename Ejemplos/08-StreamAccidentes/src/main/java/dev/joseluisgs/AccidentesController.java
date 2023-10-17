package dev.joseluisgs;

import lombok.NonNull;
import lombok.val;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class AccidentesController {
    @val
    List<Accidente> accidentes;

    private void loadData() {
        @val String dataPath = "data" + File.separator + "accidentes.csv";
        @val String appPath = System.getProperty("user.dir");
        @val Path filePath = Paths.get(appPath + File.separator + dataPath);
        System.out.println("Loading data from: " + filePath);
        // Existe usando Paths
        if (Files.exists(filePath)) {
            System.out.println("File data exists");
        } else {
            System.out.println("File data does not exist");
        }

        // Leemos los datos...
        try {
            accidentes = Files.lines(filePath)
                    .skip(1)
                    .map(this::getAccidente)
                    .toList();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void run() {
        loadData();
        accidentesAlcoholDrogas();
        accidentesPorSexo();
        accidentesPorMeses();
        accidentesPorTipoVehiculo();
        accidentesDonde("Leganes");
        accidentesPorDistritos();
        estadisticasPorDistritos();
        accidentePorDias();
        accidentesFinesDeSemanaNoche();
        accidentesFinesDeSemanaNocheAlcohol();
    }

    private void accidentesFinesDeSemanaNocheAlcohol() {
        System.out.println();
        System.out.println("Accidentes fines de semana noche y con alcohol");
        // Usando predicados
        Predicate<Accidente> finesDeSemana = a -> a.getFecha().getDayOfWeek().getValue() > 5;
        Predicate<Accidente> noche = a -> a.getHora().getHour() > 20 || a.getHora().getHour() < 6;
        Predicate<Accidente> alcohol = Accidente::isPositivoAlcohol;
        var res = accidentes.stream()
                .filter(finesDeSemana.and(noche).and(alcohol))
                .toList();

        System.out.println("Numero de accidentes Fines de semana de noche con positivo en alcohol: " + res.size());
    }

    private void accidentesFinesDeSemanaNoche() {
        System.out.println();
        System.out.println("Accidentes fines de semana y noche");
        // Usando predicados
        Predicate<Accidente> finesDeSemana = a -> a.getFecha().getDayOfWeek().getValue() > 5;
        Predicate<Accidente> noche = a -> a.getHora().getHour() > 20 || a.getHora().getHour() < 8;
        var res = accidentes.stream()
                .filter(finesDeSemana.and(noche))
                .toList();

        System.out.println("Numero de accidentes Fines de semana de noche: " + res.size());
    }

    private void accidentePorDias() {
        System.out.println();
        System.out.println("Los 10 días con más accidentes");
        Map<String, Long> accidentesPorDias = accidentes.stream()
                .collect(
                        Collectors.groupingBy(
                                a -> a.getFecha().toString(),
                                Collectors.counting()
                        )
                );

        var porAccidentes = accidentesPorDias
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(10)
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        porAccidentes.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void estadisticasPorDistritos() {
        System.out.println();
        System.out.println("Estadisticas por Barrios");

        var resBarrios = accidentes.stream()
                .filter(a -> !a.getDistrito().equals("NULL") && !a.getDistrito().equals("9"))
                .collect(Collectors.groupingBy(Accidente::getDistrito));

        var estadisticasGlobales = resBarrios.entrySet()
                .stream()
                .collect(Collectors.summarizingDouble(e -> e.getValue().size()));


        System.out.println("Estadisticas: " + estadisticasGlobales);

        System.out.println("Numero de accidentes medio por barrios: " + estadisticasGlobales.getAverage());

        var maximo = resBarrios.entrySet().stream()
                .max(Comparator.comparingDouble(e -> e.getValue().size()));

        System.out.println("Barrio con Mayor número de accidentes: " + estadisticasGlobales.getMax());
        System.out.println(maximo.get().getKey() + ": " + maximo.get().getValue().size());

        var minimo = resBarrios.entrySet().stream()
                .min(Comparator.comparingDouble(e -> e.getValue().size()));

        System.out.println("Barrio con Menor número de accidentes: " + estadisticasGlobales.getMin());
        System.out.println(minimo.get().getKey() + ": " + minimo.get().getValue().size());

    }

    private void accidentesPorDistritos() {
        System.out.println();
        System.out.println("Accidentes por distritos");
        Map<String, List<Accidente>> res = accidentes.stream()
                .filter(a -> !a.getDistrito().equals("NULL") && !a.getDistrito().equals("9"))
                .collect(Collectors.groupingBy(Accidente::getDistrito));

        res.forEach((k, v) -> System.out.println(k + ": " + v.size()));

        // Vamos a ordenarlos por nombre de distrito
        System.out.println("Accidentes por distritos ordenados por nombre");
        var ordered = new TreeMap<>(res);

        ordered.forEach((k, v) -> System.out.println(k + ": " + v.size()));

        // Ordenamos por número de accidentes
        System.out.println("Accidentes por distritos ordenados por número de accidentes ascendente");

        // Una forma de ordenar, es decirle como queremos el mapa y el comparador
        var porAccidentes = res
                .entrySet()
                .stream()
                .sorted(comparingByValue(
                        Comparator.comparingInt(List::size)
                ))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        porAccidentes.forEach((k, v) -> System.out.println(k + ": " + v.size()));

        System.out.println("Accidentes por distritos ordenados por número de accidentes descendente");

        // Una forma de ordenar, es decirle como queremos el mapa y el comparador
        porAccidentes = res
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue(
                        Comparator.comparingInt(List::size)
                )))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        porAccidentes.forEach((k, v) -> System.out.println(k + ": " + v.size()));

        var usera = res.get("USERA");
        System.out.println("Accidentes en USERA: " + usera.size());
        // usera.forEach(System.out::println);
    }

    private void accidentesDonde(@NonNull String lugar) {
        System.out.println("Accidentes donde " + lugar);
        Map<String, List<Accidente>> res = accidentes.stream()
                .filter(a -> a.getLocalizacion().toUpperCase().contains(lugar.toUpperCase()))
                .collect(Collectors.groupingBy(Accidente::getLocalizacion));

        System.out.println("Accidentes donde Leganes: " + res.size());
        res.forEach((k, v) -> System.out.println(k + ": " + v.size()));

        System.out.println("Listado de accidentes donde Leganes");
        res.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void accidentesPorTipoVehiculo() {
        System.out.println();
        System.out.println("Accidentes por tipo de vehiculo");

        Map<String, List<Accidente>> res = accidentes.stream()
                .collect(Collectors.groupingBy(Accidente::getTipoVehiculo));
        res.forEach((k, v) -> System.out.println(k + ": " + v.size()));

        // Otra forma de hacerlo
        Map<String, Long> res2 = accidentes.stream()
                .collect(
                        Collectors.groupingBy(
                                Accidente::getTipoVehiculo,
                                Collectors.counting()
                        )
                );
        res2.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private Accidente getAccidente(String linea) {
        String[] campos = linea.split(";");
        String numExpediente = campos[0];
        LocalDate fecha = getFecha(campos[1]);
        LocalTime hora = getHora(campos[2]);
        String localizacion = campos[3];
        String distrito = campos[6];
        String tipoAccidente = campos[7];
        String meteorologia = campos[8];
        String tipoVehiculo = campos[9];
        String tipoPersona = campos[10];
        String sexo = campos[12];
        boolean positivoAlcohol = getAlcohol(campos[17]);
        boolean positivoDrogas = getDrogas(campos[18]);
        return new Accidente(numExpediente, fecha, hora, localizacion, distrito, tipoAccidente, meteorologia, tipoVehiculo, tipoPersona, sexo, positivoAlcohol, positivoDrogas);
    }

    private LocalDate getFecha(String fecha) {
        // Procesamos la fecha
        /*
        String[] fecha = campo.split("/");
        int dia = Integer.parseInt(fecha[0]);
        int mes = Integer.parseInt(fecha[1]);
        int anio = Integer.parseInt(fecha[2]);
        return LocalDate.of(anio, mes, dia);
        */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(fecha, formatter);
    }

    private LocalTime getHora(String hora) {
        // Procesamos la hora, ojo que falta numeros a las horas
        /*
        String[] hora = campo.split(":");
        int horas = Integer.parseInt(hora[0]);
        int minutos = Integer.parseInt(hora[1]);
        return LocalTime.of(horas, minutos);
        */
        hora = (hora.length() <= 7) ? "0" + hora : hora;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(hora, formatter);
    }

    private boolean getDrogas(String campo) {
        return campo.equals("1");
    }

    private boolean getAlcohol(String campo) {
        return campo.equals("S");
    }

    private void accidentesAlcoholDrogas() {
        System.out.println();
        System.out.println("Accidentes con positivo en alcohol y drogas");
        var res = accidentes.stream()
                .filter(a -> a.isPositivoAlcohol() && a.isPositivoDrogas())
                .toList();
        System.out.println("Numero de Expedientes con Alcohol y Drogas: " + res.size());
        // res.forEach(System.out::println);

        // Otra forma es con predicado
        Predicate<Accidente> alcohol = Accidente::isPositivoAlcohol;
        Predicate<Accidente> drogas = Accidente::isPositivoDrogas;
        var numero = accidentes.stream().filter(alcohol.and(drogas)).count();
        System.out.println("Numero de Expedientes con Alcohol y Drogas: " + numero);
    }

    public void accidentesPorSexo() {
        System.out.println();
        System.out.println("Accidentes por sexo");
        var res = accidentes.stream()
                .filter(a -> a.getSexo().equals("Hombre"))
                .count();
        System.out.println("Numero de Expedientes sexo Hombre: " + res);
        res = accidentes.stream()
                .filter(a -> a.getSexo().equals("Mujer"))
                .count();
        System.out.println("Numero de Expedientes sexo Mujer: " + res);

        Map<String, Long> resultado = accidentes.stream()
                .filter(a -> (a.getSexo().equals("Hombre") || a.getSexo().equals("Mujer")))
                .collect(
                        Collectors.groupingBy(
                                Accidente::getSexo,
                                Collectors.counting()
                        )
                );

        System.out.println("Numero de accidentes por sexo");
        resultado.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void accidentesPorMeses() {
        System.out.println();
        System.out.println("Accidentes por meses");
        var res = accidentes.stream()
                .filter(a -> a.getFecha().getMonth().equals(Month.APRIL))
                .collect(Collectors.toList());
        System.out.println("Numero de Expedientes en abril: " + res.size());
        res = accidentes.stream()
                .filter(a -> a.getFecha().getMonth().equals(Month.MAY))
                .collect(Collectors.toList());
        System.out.println("Numero de Expedientes en mayo: " + res.size());

        Map<String, Long> resultado = accidentes.stream()
                .collect(
                        Collectors.groupingBy(
                                a -> a.getFecha().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()),
                                Collectors.counting()
                        )
                );

        TreeMap<String, Long> resultadoOrdenado = new TreeMap<>(resultado);

        System.out.println("Numero de accidentes por mes");
        resultado.forEach((k, v) -> System.out.println(k + ": " + v));

        System.out.println("Numero de accidentes por mes ordenados");
        resultadoOrdenado.forEach((k, v) -> System.out.println(k + ": " + v));
    }
}
