package Integrador;

import Dominio.Pelicula;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class IntegradorFileSystem {

    private LlamadorCredenciales credenciales = new LlamadorCredenciales();
    Connection conex;

    public IntegradorFileSystem() {
        try {
            // Cargamos las credenciales desde el properties
            credenciales.cargarCredenciales();
            if (credenciales.getUrl() != null) {
                conex = DriverManager.getConnection(credenciales.getUrl(), credenciales.getUser(), credenciales.getPassword());
                System.out.println("IntegradorFileSystem: conexión establecida con " + credenciales.getUrl());
            } else {
                conex = null;
                System.err.println("Advertencia: credenciales de base de datos no configuradas.");
            }
        } catch (SQLException e) {
            conex = null;
            System.err.println("No se pudo establecer la conexión a la base de datos: " + e.getMessage());
        }
    }



    public ArrayList<Pelicula> catalogo(){
        String SQL ="select * from pelicula ";
        ArrayList<Pelicula> peliculas = new ArrayList<>();
        try {
            if (conex == null) {
                System.err.println("Conexión nula: devolviendo catálogo vacío");
                return peliculas;
            }
            java.sql.Statement statement = conex.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setNombre(resultSet.getString("nombre"));
                pelicula.setPrecio(resultSet.getDouble("precio"));
                peliculas.add(pelicula);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return peliculas;
    }

    public boolean guardarCompra(Dominio.Compra compra) {
        if (conex == null) {
            System.err.println("guardarCompra: conexión nula");
            return false;
        }
        try {
            // insertar comprador si no existe
            try (java.sql.PreparedStatement psComprador = conex.prepareStatement(
                    "INSERT INTO comprador (cedula, nombre) VALUES (?, ?)") ) {
                psComprador.setString(1, String.valueOf(compra.getComprador().getCedula()));
                psComprador.setString(2, compra.getComprador().getNombre());
                psComprador.executeUpdate();
            } catch (SQLException ex) {

                System.out.println("guardarCompra: comprador probablemente existente: " + ex.getMessage());
            }

            // insertar compra y obtener id generado
            long compraId = -1;
            try (java.sql.PreparedStatement psCompra = conex.prepareStatement(
                    "INSERT INTO compra (comprador_cedula) VALUES (?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS)) {
                String cedulaStr = String.valueOf(compra.getComprador().getCedula());
                System.out.println("guardarCompra: insertando compra para comprador_cedula='" + cedulaStr + "'");
                psCompra.setString(1, cedulaStr);
                psCompra.executeUpdate();
                try (java.sql.ResultSet keys = psCompra.getGeneratedKeys()) {
                    if (keys != null && keys.next()) {
                        try {
                            compraId = keys.getLong(1);
                        } catch (SQLException ex) {
                            // No podemos leer la clave generada (driver/trigger devolvió valor no numérico)
                            System.err.println("guardarCompra: no se pudo leer generated key (psCompra.getGeneratedKeys): " + ex.getMessage());
                            compraId = -1;
                        }
                    }
                }
            }

            // Si no obtuvimos id generado, intentamos obtener el id por la fila más reciente del comprador)
            if (compraId == -1) {
                try (java.sql.PreparedStatement psFindCompra = conex.prepareStatement(
                        "SELECT id FROM compra WHERE comprador_cedula = ? ORDER BY fecha DESC FETCH FIRST 1 ROWS ONLY")) {
                    String cedulaStr = String.valueOf(compra.getComprador().getCedula());
                    System.out.println("guardarCompra: fallback buscando compra reciente para comprador_cedula='" + cedulaStr + "'");
                    psFindCompra.setString(1, cedulaStr);
                    try (java.sql.ResultSet rs = psFindCompra.executeQuery()) {
                        if (rs.next()) compraId = rs.getLong("id");
                    }
                } catch (SQLException ex) {
                    // no podemos recuperar id, abortamos
                    System.err.println("guardarCompra: no fue posible recuperar id de compra: " + ex.getMessage());
                    return false;
                }
            }

            // insertar lineas
            for (Dominio.Linea l : compra.getLineas()) {
                long peliculaId = -1;
                // buscar pelicula por nombre
                try (java.sql.PreparedStatement psFind = conex.prepareStatement("SELECT id FROM pelicula WHERE nombre = ?")) {
                    System.out.println("guardarCompra: buscando pelicula por nombre='" + l.getTargeta().getNombre() + "'");
                    psFind.setString(1, l.getTargeta().getNombre());
                    try (java.sql.ResultSet rs = psFind.executeQuery()) {
                        if (rs.next()) {
                            peliculaId = rs.getLong("id");
                        }
                    }
                }

                if (peliculaId == -1) {
                    // insertar pelicula y recuperar id generado
                    try (java.sql.PreparedStatement psInsertP = conex.prepareStatement(
                            "INSERT INTO pelicula (nombre, precio) VALUES (?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        System.out.println("guardarCompra: insertando pelicula nombre='" + l.getTargeta().getNombre() + "' precio=" + l.getTargeta().getPrecio());
                        psInsertP.setString(1, l.getTargeta().getNombre());
                        psInsertP.setDouble(2, l.getTargeta().getPrecio());
                        psInsertP.executeUpdate();
                        try (java.sql.ResultSet k = psInsertP.getGeneratedKeys()) {
                            if (k != null && k.next()) {
                                try {
                                    peliculaId = k.getLong(1);
                                } catch (SQLException ex) {
                                    System.err.println("guardarCompra: no se pudo leer generated key para pelicula: " + ex.getMessage());
                                    peliculaId = -1;
                                }
                            }
                        }
                    }
                }

                if (peliculaId == -1) {
                    System.err.println("guardarCompra: no se pudo obtener id de película para: " + l.getTargeta().getNombre());
                    return false;
                }

                // insertar linea
                try (java.sql.PreparedStatement psLinea = conex.prepareStatement(
                        "INSERT INTO linea (compra_id, pelicula_id, cantidad) VALUES (?, ?, ?)") ) {
                    System.out.println("guardarCompra: insertando linea compra_id=" + compraId + " pelicula_id=" + peliculaId + " cantidad=" + l.getCantidad());
                    psLinea.setLong(1, compraId);
                    psLinea.setLong(2, peliculaId);
                    psLinea.setInt(3, l.getCantidad());
                    psLinea.executeUpdate();
                }
            }

            return true;
        } catch (SQLException e) {
            System.err.println("guardarCompra: Se ha producido SQLException: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }








}
