package org.develop;

import org.develop.services.DatabaseManager;
import org.develop.services.ReadCSVFunkos;
import org.develop.services.WriteFunkosJSON;

public class Main {
    public static void main(String[] args) {
        ReadCSVFunkos rs = new ReadCSVFunkos();
        WriteFunkosJSON wr = new WriteFunkosJSON();
        var bien = wr.writeJSON("funkos.json",rs.readFileFunko());
        System.out.println(bien);

    }
}