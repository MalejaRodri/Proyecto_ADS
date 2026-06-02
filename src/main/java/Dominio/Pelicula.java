package Dominio;

public class Pelicula {
    private  String nombre;
    private double precio;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return  nombre + "    "+precio ;
    }

    public double getPrecio() {
        return precio;
    }
}
