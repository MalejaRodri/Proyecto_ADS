package Dominio;

import java.time.LocalDate;
import java.util.ArrayList;

public class Compra {
    private LocalDate fecha = LocalDate.now();
    private Comprador comprador;
    private ArrayList<Pago> pagos = new ArrayList<>();
    private ArrayList<Linea> Lineas = new ArrayList<>();
    private int numeroCompra;


    public LocalDate getFecha() {
        return fecha;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public ArrayList<Pago> getPagos() {
        return pagos;
    }

    public ArrayList<Linea> getLineas() {
        return Lineas;
    }




    public Compra(Comprador comprador) {
        this.comprador = comprador;
        // listas ya inicializadas en la declaración de campos
        this.numeroCompra = 0;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public int getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(int numeroCompra) {
        this.numeroCompra = numeroCompra;
    }
}
