package dev.joseluisgs.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

// Esto es porque GSON no se pensó para Java 11 hacia arriba, mejor usar Jackson o Moshi
// Tiene problemas con las fechas LocalDate y LocalDateTime
public class UuidAdapter extends TypeAdapter<UUID> {

    @Override
    public UUID read(final JsonReader jsonReader) throws IOException {
        return UUID.fromString(jsonReader.nextString());
    }

    @Override
    public void write(JsonWriter jsonWriter, UUID uuid) throws IOException {
        jsonWriter.value(uuid.toString());

    }
}