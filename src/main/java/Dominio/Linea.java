package Dominio;

public class Linea {
    private int cantidad;
    private Pelicula targeta;

    public Linea(int cantidad, Pelicula targeta) {
        this.cantidad = cantidad;
        this.targeta = targeta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Pelicula getTargeta() {
        return targeta;
    }

    public void setTargeta(Pelicula targeta) {
        this.targeta = targeta;
    }
}
