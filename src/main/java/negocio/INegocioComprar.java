package negocio;

import Dominio.Compra;
import Dominio.Linea;
import Dominio.Pelicula;

import java.util.List;

public interface INegocioComprar {
    // HU Cargar Pelicula
    List<Pelicula> cargarPeliculas();

    // HU Iniciar Compra
    void iniciarCompra(String nombre, String cedula);

    // HU Agregar Linea
    void agregarLinea(Pelicula pelicula, int cantidad);

    // HU Listar Lineas
    List<Linea> listarLineas();

    // HU Eliminar Linea
    void eliminarLinea(Linea linea);

    // HU Modificar Cantidad
    void modificarCantidad(Linea linea, int nuevaCantidad);

    // HU Totalizar Compra
    double totalizarCompra();

    // HU Terminar Compra
    double registrarPago(double denominacion, int cantidad);

    // HU Guardar Compra
    void guardarCompra();
}
