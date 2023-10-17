package dev.joseluisgs.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

// Esto es porque GSON no se pensó para Java 11 hacia arriba, mejor usar Jackson o Moshi
// Tiene problemas con las fechas LocalDate y LocalDateTime
public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    @Override
    public LocalDate read(final JsonReader jsonReader) throws IOException {
        return LocalDate.parse(jsonReader.nextString());
    }

    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
        jsonWriter.value(localDate.toString());

    }
}