package negocio;

import Dominio.Compra;
import Dominio.Comprador;
import Dominio.Linea;
import Dominio.Pelicula;
import Integrador.IntegradorFileSystem;

import java.util.ArrayList;
import java.util.List;

public class NegocioComprar  implements  INegocioComprar {
    private IntegradorFileSystem integradorFileSystem ;
    private ArrayList<Pelicula> catalogo;
    private Compra compraactual;





    public NegocioComprar() {
        try {
            this.integradorFileSystem = new IntegradorFileSystem();
            this.catalogo = integradorFileSystem.catalogo();
            System.out.println("NegocioComprar: catálogo cargado con " +  catalogo.size()  + " elementos.");
        } catch (Exception e) {
            // Si falla la conexión no pone na en el ctalogo
            this.catalogo = new ArrayList<>();
            System.err.println("Advertencia: no se pudo inicializar IntegradorFileSystem: " + e.getMessage());
        }
    }

    public ArrayList<Pelicula> getCatalogo() {
        return catalogo;
    }

    public Compra getCompraactual() {
        return compraactual;
    }





















    @Override
    public List<Pelicula> cargarPeliculas() { return null; }

    @Override
    public void iniciarCompra(String nombre, String cedula) {

        if (this.compraactual == null) {
            Comprador comprador = new Comprador(cedula, nombre);
            this.compraactual = new Compra(comprador);
            // generar numero de compra aleatorio para ell numero de compra
            int nro = new java.util.Random().nextInt(1_000_000);
            this.compraactual.setNumeroCompra(nro);
        } else {
            // Si ya hay una compra en curso, actualizamos los datos del comprador pero conservamos el numero
            if (this.compraactual.getComprador() == null) {
                this.compraactual.setComprador(new Comprador(cedula, nombre));
            } else {
                this.compraactual.getComprador().setCedula(cedula);
                this.compraactual.getComprador().setNombre(nombre);
            }
        }

    }

    @Override
    public void agregarLinea(Pelicula pelicula, int cantidad) {
        if (this.compraactual == null) {
            // Si no hay compra activa, creamos una con comprador por defecto
            this.compraactual = new Compra(new Comprador("0", "Anonimo"));
        }
        // Si ya existe una linea con la misma pelicula, sumamos la cantidad
        boolean encontrada = false;
        for (Linea l : this.compraactual.getLineas()) {
            if (l.getTargeta() != null && pelicula != null && pelicula.getNombre().equals(l.getTargeta().getNombre())) {
                l.setCantidad(l.getCantidad() + cantidad);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            Linea linea = new Linea(cantidad, pelicula);
            this.compraactual.getLineas().add(linea);
        }
    }



    @Override
    public List<Linea> listarLineas() {
        if (this.compraactual == null) return new ArrayList<>();
        return this.compraactual.getLineas();
    }

    @Override
    public void modificarCantidad(Linea linea, int nuevaCantidad) {
        if (linea != null) {
            linea.setCantidad(nuevaCantidad);
        }
    }

    @Override
    public void eliminarLinea(Linea linea) {
        if (this.compraactual != null && linea != null) {
            this.compraactual.getLineas().remove(linea);
        }
    }

    // Reinicia la compra actual (vacía las lineas)
    public void reiniciarCompra() {
        if (this.compraactual != null) {
            this.compraactual.getLineas().clear();
        }
    }


    @Override
    public double registrarPago(double denominacion, int cantidad) { return 0; }
    @Override
    public double totalizarCompra() {
        double total = 0.0;
        if (this.compraactual != null) {
            for (Linea l : this.compraactual.getLineas()) {
                if (l.getTargeta() != null) total += l.getTargeta().getPrecio() * l.getCantidad();
            }
        }
        return total;
    }

    @Override
    public void guardarCompra() {
        if (this.compraactual == null) {
            System.err.println("guardarCompra: no hay compra actual");
            return;
        }
        boolean ok = false;
        try {
            ok = this.integradorFileSystem.guardarCompra(this.compraactual);
        } catch (Exception e) {
            System.err.println("guardarCompra: " + e.getMessage());
        }
        if (ok) {
            System.out.println("guardarCompra: compra guardada correctamente");
        } else {
            System.err.println("guardarCompra: error al guardar compra");
        }
    }
}
