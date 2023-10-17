package dev.joseluisgs.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

// Esto es porque GSON no se pensó para Java 11 hacia arriba, mejor usar Jackson o Moshi
// Tiene problemas con las fechas LocalDate y LocalDateTime
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString());
    }

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(localDateTime.toString());

    }
}