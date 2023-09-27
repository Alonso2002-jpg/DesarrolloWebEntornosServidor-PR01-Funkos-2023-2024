package org.develop;

import org.develop.model.Funko;
import org.develop.model.Modelo;
import org.develop.repository.FunkoRepositoryImpl;
import org.develop.services.DatabaseManager;
import org.develop.services.ReadCSVFunkos;
import org.develop.services.WriteFunkosJSON;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        ReadCSVFunkos rs = new ReadCSVFunkos();
        DatabaseManager db = DatabaseManager.getInstance();
        FunkoRepositoryImpl fkImp = FunkoRepositoryImpl.getInstance(db);

        //Insercion en Base de Datos del fichero
        rs.readFileFunko().forEach(fkImp::save);

        //Obtener Funko por ID
        var funk = fkImp.findById(4).orElseGet(Funko::new);
        System.out.println(funk);

        //Update de Funko en Base de Datos
        funk.setName("Pam Besley The Office");
        funk.setModelo(Modelo.OTROS);
        funk.setPrecio(13.5);

        var funk1= fkImp.update(funk);
        System.out.println(funk1);

        //Obtener Funkos por Patron Nombre
        var funk2 = fkImp.findByName("Pa");
        funk2.forEach(System.out::println);

        //Eliminar Funko por ID
        var funkDel = fkImp.deleteById(4);

        //Obtener todos los Funkos de la base de datos
        fkImp.findAll().forEach(System.out::println);

        //Backup de los Funkos en JSON
        var bk = fkImp.backup("funkos");
        System.out.println(bk);

        //Eliminar todos los Funkos de la base de datos
        fkImp.deleteAll();

        //Comprobacion Delete
        fkImp.findAll().forEach(System.out::println);

    }
}