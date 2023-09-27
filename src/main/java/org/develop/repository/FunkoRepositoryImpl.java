package org.develop.repository;

import org.develop.model.Funko;
import org.develop.model.Modelo;
import org.develop.services.DatabaseManager;
import org.develop.services.WriteFunkosJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FunkoRepositoryImpl implements FunkoRepository<Funko,Integer>{
    private static FunkoRepositoryImpl instance;
    private final Logger logger = LoggerFactory.getLogger(FunkoRepositoryImpl.class);

    private final DatabaseManager db;

    private FunkoRepositoryImpl(DatabaseManager db){
        this.db= db;
    }

    public static FunkoRepositoryImpl getInstance(DatabaseManager db){
        if (instance == null){
            instance= new FunkoRepositoryImpl(db);
        }

        return instance;
    }

    @Override
    public Funko save(Funko funko) {
        String sqlQuery = "INSERT INTO Funko (uuid, name, modelo, precio, fecha_lanzamiento) VALUES (?, ?, ?, ?, ?)";
    try (var conn = db.getConnection();
         var stmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);) {
        stmt.setObject(1, funko.getUuid());
        stmt.setString(2, funko.getName());
        stmt.setString(3, funko.getModelo().toString());
        stmt.setDouble(4, funko.getPrecio());
        stmt.setDate(5, Date.valueOf(funko.getFecha_lanzamiento()));
        int res = stmt.executeUpdate();
        conn.commit();

        if (res > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                funko.setId(rs.getInt(1)); // Obtiene el ID generado automáticamente
            }
            rs.close();
        }
    } catch (SQLException e) {
        logger.error("ERROR : "+e.getMessage(), e);
    }
    return funko;
    }

    @Override
    public Funko update(Funko funko){
        logger.info("Actualizando Objeto ..... ");
        String sqlQuery = "UPDATE Funko SET name = ? , modelo = ?, precio = ? , updated_at = ? WHERE id = ?";
        try (var conn = db.getConnection();
            var stmt = conn.prepareStatement(sqlQuery);){
            stmt.setString(1,funko.getName());
            stmt.setString(2,funko.getModelo().toString());
            stmt.setDouble(3,funko.getPrecio());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(5,funko.getId());
            var rs = stmt.executeUpdate();
            if (rs <= 0 ){
                return new Funko();
            }
            logger.debug("Objeto Actualizado Correctamente!");
        }catch (SQLException e){
            logger.error("ERROR : "+e.getMessage(), e);
        }

        return funko;
    }

    @Override
    public Optional<Funko> findById(Integer id) {
        logger.info("Buscando Objeto con ID "+ id + "......");
        String sqlQuery = "SELECT * FROM Funko WHERE id = ?";
        Optional<Funko> funk = Optional.empty();
        try (var conn = db.getConnection(); var stmt = conn.prepareStatement(sqlQuery)){
            stmt.setInt(1,id);
            var rs = stmt.executeQuery();
            while (rs.next()){
                Funko fk = new Funko();
                fk.setId(rs.getInt("id"));
                fk.setUuid((UUID) rs.getObject("uuid"));
                fk.setName(rs.getString("name"));
                fk.setModelo(Modelo.valueOf(rs.getString("modelo")));
                fk.setPrecio(rs.getDouble("precio"));
                fk.setFecha_lanzamiento(rs.getDate("fecha_lanzamiento").toLocalDate());
                funk = Optional.of(fk);
            }
            logger.debug("Objeto con ID: " + id + " encontrado!");
        }catch (SQLException e){
            logger.error("ERROR : "+e.getMessage(), e);
        }

        return funk;
    }


    @Override
    public List<Funko> findAll() {
        logger.info("Obteniendo todos los Objetos");
        String sqlQuery = "SELECT * FROM Funko";
        List<Funko> funks = new ArrayList<>();
        try(var conn = db.getConnection(); var stmt = conn.prepareStatement(sqlQuery)){
            var rs = stmt.executeQuery();
            while (rs.next()){
                Funko fk = new Funko();
                fk.setId(rs.getInt("id"));
                fk.setUuid((UUID) rs.getObject("uuid"));
                fk.setName(rs.getString("name"));
                fk.setModelo(Modelo.valueOf(rs.getString("modelo")));
                fk.setPrecio(rs.getDouble("precio"));
                fk.setFecha_lanzamiento(rs.getDate("fecha_lanzamiento").toLocalDate());
                funks.add(fk);
            }
        logger.debug("Objetos Obtenidos Correctamente");
        }catch (SQLException e){
            logger.error("ERROR : "+e.getMessage(), e);
        }
        return funks;
    }

    @Override
    public boolean deleteById(Integer id) {
        logger.info("Eliminando Objeto con ID " + id + "..........");
        String sqlQuery = "DELETE FROM Funko WHERE id = ? ";
        try (var conn = db.getConnection(); var stmt = conn.prepareStatement(sqlQuery)){
            stmt.setInt(1, id);
            var rs = stmt.executeUpdate();
            if (rs>0){
                logger.info("Eliminado correctamente");
                return true;
            }
        }catch (SQLException e){
            logger.error("ERROR : "+e.getMessage(), e);
        }
        return false;
    }


    @Override
    public void deleteAll() {
        logger.info("Eliminando Objetos de la BD......");
        String sqlQuery = "DELETE FROM Funko";
        try (var conn = db.getConnection(); var stmt = conn.prepareStatement(sqlQuery)){
            var rs = stmt.executeUpdate();
        }catch (SQLException e){
            logger.error("ERROR : "+e.getMessage(), e);
        }
        logger.info("Objetos eliminados Correctamente");
    }


    @Override
    public List<Funko> findByName(String name) {
        logger.info("Obtener Funko con Nombre "+name+".......");
        List<Funko> funks = new ArrayList<>();
        String sqlQuery = "SELECT * FROM Funko WHERE name LIKE ?";
        try (var conn = db.getConnection(); var stmt = conn.prepareStatement(sqlQuery)){
            stmt.setString(1,"%" + name + "%");
            var rs = stmt.executeQuery();
            while (rs.next()){
                Funko fk = new Funko();
                fk.setId(rs.getInt("id"));
                fk.setUuid((UUID) rs.getObject("uuid"));
                fk.setName(rs.getString("name"));
                fk.setModelo(Modelo.valueOf(rs.getString("modelo")));
                fk.setPrecio(rs.getDouble("precio"));
                fk.setFecha_lanzamiento(rs.getDate("fecha_lanzamiento").toLocalDate());
                funks.add(fk);
            }
            logger.debug("Objeto obtenido con nombre: " + name);
        }catch (SQLException e){
            logger.error("ERROR : "+e.getMessage(), e);
        }

        return funks;
    }

        @Override
    public boolean backup(String name){
        String file = name+ ".json";
        logger.info("Iniciando Backup de la Base de Datos......");
        WriteFunkosJSON wrJSON = new WriteFunkosJSON();
        List<Funko> funks = findAll();
        logger.debug("Backup Realizado Correctamente");
        return wrJSON.writeJSON(file,funks);
    }
}
