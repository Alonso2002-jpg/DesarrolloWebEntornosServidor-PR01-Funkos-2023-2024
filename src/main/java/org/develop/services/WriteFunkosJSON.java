package org.develop.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.develop.adapter.LocalDateAdapter;
import org.develop.model.Funko;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class WriteFunkosJSON {

  public boolean writeJSON(String routePath, ArrayList<Funko> funks) {
    String path = Paths.get("").toAbsolutePath().toString() + File.separator + "data" + File.separator + routePath;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();
    boolean success = false;
    try (FileWriter writer = new FileWriter(path)) {
        gson.toJson(funks, writer);
        success = true;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return success;
}
}
