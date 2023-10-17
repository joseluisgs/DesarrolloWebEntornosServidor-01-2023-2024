package dev.joseluisgs.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.joseluisgs.models.Persona;
import dev.joseluisgs.utils.LocalDateAdapter;
import dev.joseluisgs.utils.LocalDateTimeAdapter;
import dev.joseluisgs.utils.UuidAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BackupJsonImpl implements BackupJson {
    private static BackupJsonImpl instance;
    private final String APP_PATH = System.getProperty("user.dir");
    private final String DATA_DIR = APP_PATH + File.separator + "data";
    private final String BACKUP_FILE = DATA_DIR + File.separator + "personas.json";

    private BackupJsonImpl() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }


    public static BackupJsonImpl getInstance() {
        if (instance == null) {
            instance = new BackupJsonImpl();
        }
        return instance;
    }


    @Override
    public void backup(List<Persona> personas) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(personas);
        Files.writeString(new File(BACKUP_FILE).toPath(), json);
    }


    @Override
    public List<Persona> restore() throws IOException {
        Gson gson = new GsonBuilder()
                // cargar solos los daptadores que necesites
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(UUID.class, new UuidAdapter())
                .create();
        String json = "";
        json = Files.readString(new File(BACKUP_FILE).toPath());
        return gson.fromJson(json, new TypeToken<List<Persona>>() {
        }.getType());
    }
}
