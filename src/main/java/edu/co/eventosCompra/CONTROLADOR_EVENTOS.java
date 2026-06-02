package edu.co.eventosCompra;

import Dominio.Comprador;
import Dominio.Pelicula;
import Dominio.Linea;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import negocio.NegocioComprar;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class CONTROLADOR_EVENTOS implements Initializable {




    private Random random = new Random();


    private Comprador comprador;




    private NegocioComprar negocio;
    private static NegocioComprar sharedNegocio = new NegocioComprar();

    public CONTROLADOR_EVENTOS() {
        // la misma variable para que todas las pantallas las vean
        this.negocio = sharedNegocio;
    }








    //Pagos-----------------------------------------------------------------------------------------------

    @FXML
    private TextField Dolar1;
    @FXML
    private TextField Dolar2;
    @FXML
    private TextField Dolar5;
    @FXML
    private TextField Dolar10;
    @FXML
    private TextField Dolar50;
    @FXML
    private TextField Dolar100;
    @FXML
    private Label Sub_total;
    @FXML
    private Label vueltas;
    @FXML
    private Label nombrePAGO;
    @FXML
    private Label cedulaPAGO;
    @FXML
    private Label total_pago;




    //Pantalla Pagos----------------------


    @FXML Label NO_COMPRA;




    private void Actualizar_SubTotal() {
        try {
            // Iniciamos todas las variables en 0
            double dolar1 = 0;
            double dolar2 = 0;
            double dolar5 = 0;
            double dolar10 = 0;
            double dolar50 = 0;
            double dolar100 = 0;

            // Validamos uno por uno con if
            // Si no está vacío  entonces sí lo convertimos a número

            if (!Dolar1.getText().isEmpty()) {
                dolar1 = Double.parseDouble(Dolar1.getText());
            }

            if (!Dolar2.getText().isEmpty()) {
                dolar2 = Double.parseDouble(Dolar2.getText())*2;
            }

            if (!Dolar5.getText().isEmpty()) {
                dolar5 = Double.parseDouble(Dolar5.getText())*5;
            }

            if (!Dolar10.getText().isEmpty()) {
                dolar10 = Double.parseDouble(Dolar10.getText())*10;
            }

            if (!Dolar50.getText().isEmpty()) {
                dolar50 = Double.parseDouble(Dolar50.getText())*50;
            }

            if (!Dolar100.getText().isEmpty()) {
                dolar100 = Double.parseDouble(Dolar100.getText())*100;
            }

            // Sumamos y actualizamos
            Sub_total.setText(String.valueOf(dolar1 + dolar2 + dolar5 + dolar10 + dolar50 + dolar100));

            // mostrar número de compra de la compra actual (si existe)
            if (NO_COMPRA != null && negocio != null && negocio.getCompraactual() != null) {
                NO_COMPRA.setText(String.valueOf(negocio.getCompraactual().getNumeroCompra()));
            }

        } catch (NumberFormatException e) {
            Sub_total.setText("Ingrese un valor numérico válido");
        }
    }


    public void pagarTodo(ActionEvent actionEvent) {
        // Cuando se presiona pagar: calcular diferencia entre Sub_total y total_pago
        try {
            double subtotal = 0.0;
            if (Sub_total != null && !Sub_total.getText().isEmpty()) {
                subtotal = Double.parseDouble(Sub_total.getText());
            }
            double totalAPagar = 0.0;
            if (total_pago != null && !total_pago.getText().isEmpty()) {
                totalAPagar = Double.parseDouble(total_pago.getText());
            } else {
                // si total_pago no está en la vista, obtener del negocio
                totalAPagar = negocio.totalizarCompra();
            }

            if (subtotal < totalAPagar) {
                if (vueltas != null) vueltas.setText("Saldo insuficiente");
                System.err.println("pagarTodo: saldo insuficiente");
                return;
            }

            double cambio = subtotal - totalAPagar;
            if (vueltas != null) vueltas.setText(String.valueOf(cambio));

            // guardar la base de datoss
            negocio.guardarCompra();
        } catch (NumberFormatException e) {
            if (vueltas != null) vueltas.setText("Error en montos");
        }
    }



    // PANTALLA_Compra



    //convierte la lista de peliculas en observable (se inicializa en initialize)
    private ObservableList<Pelicula> peliculasObservable;

    @FXML
    private ComboBox<Pelicula> combo_peliculas;

    @FXML
    private ListView<String> lista_agregada;
    @FXML
    private Label TOTAL_PELICULAS_LISTA;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReiniciar;

    @FXML
    private Label precio_pelicula;

    @FXML
    private TextField cantidad;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializamos listeners de pagos si los campos están disponibles
        if (Dolar1 != null) Dolar1.textProperty().addListener((observable, oldValue, newValue) -> Actualizar_SubTotal());
        if (Dolar2 != null) Dolar2.textProperty().addListener((observable, oldValue, newValue) -> Actualizar_SubTotal());
        if (Dolar5 != null) Dolar5.textProperty().addListener((observable, oldValue, newValue) -> Actualizar_SubTotal());
        if (Dolar10 != null) Dolar10.textProperty().addListener((observable, oldValue, newValue) -> Actualizar_SubTotal());
        if (Dolar50 != null) Dolar50.textProperty().addListener((observable, oldValue, newValue) -> Actualizar_SubTotal());
        if (Dolar100 != null) Dolar100.textProperty().addListener((observable, oldValue, newValue) -> Actualizar_SubTotal());

        // Inicializamos la lista de películas desde negocio solo si negocio no es null
        if (negocio == null) {
            negocio = new NegocioComprar();
        }
        peliculasObservable = FXCollections.observableArrayList(negocio.getCatalogo());


        if (combo_peliculas != null) {
            combo_peliculas.setItems(peliculasObservable);
            combo_peliculas.setConverter(new StringConverter<Pelicula>() {
                @Override
                public String toString(Pelicula pelicula) {

                    if(pelicula==null){
                        return "";
                    }else
                        precio_pelicula.setText(String.valueOf(pelicula.getPrecio()));


                        return  pelicula.getNombre();

                }

                @Override
                public Pelicula fromString(String string) {
                    if (string == null) return null;
                    for (Pelicula p : combo_peliculas.getItems()) {
                        if (string.equals(p.getNombre()))

                            return p;
                    }
                    return null;
                }
            });

            System.out.println("CONTROLADOR_EVENTOS: Tamaño de peliculas combo aver si si se metio=" + combo_peliculas.getItems().size());
            //ver las peliculas del combo box
            combo_peliculas.getItems().forEach(p -> System.out.println("ITEM: " + p.getNombre()));
            // toma la primer pelicula
            if (!combo_peliculas.getItems().isEmpty()) {
                combo_peliculas.getSelectionModel().selectFirst();
            }
        } else {
            System.err.println("ERROR");
        }

        // Si esta instancia corresponde a la vista de carrito, mostramos las lineas actuales
        if (lista_agregada != null) {
            ObservableList<String> listaItems = FXCollections.observableArrayList();
            for (Linea l : negocio.listarLineas()) {
                Pelicula p = l.getTargeta();
                listaItems.add(p.getNombre() + " - " + l.getCantidad());
            }
            lista_agregada.setItems(listaItems);
            // actualizar total
            actualizarTotal();
        }

        // Si esta instancia corresponde a la pantalla de pago, rellenar datos
        if (nombrePAGO != null || cedulaPAGO != null || total_pago != null || NO_COMPRA != null) {
            if (negocio.getCompraactual() != null) {
                if (nombrePAGO != null && negocio.getCompraactual().getComprador() != null) {
                    nombrePAGO.setText(negocio.getCompraactual().getComprador().getNombre());
                }
                if (cedulaPAGO != null && negocio.getCompraactual().getComprador() != null) {
                    cedulaPAGO.setText(negocio.getCompraactual().getComprador().getCedula());
                }
                if (total_pago != null) {
                    total_pago.setText(String.valueOf(negocio.totalizarCompra()));
                }
                if (NO_COMPRA != null) {
                    NO_COMPRA.setText(String.valueOf(negocio.getCompraactual().getNumeroCompra()));
                }
            }
        }
    }



    //PANTALLAS CAMBIOS -------------------------

    @FXML
    protected void irACompraActual(ActionEvent event) throws IOException {
        Parent nuevaVista = FXMLLoader.load(getClass().getResource("/edu/co/PANTALLAS/CARRITO.fxml"));
        Scene escena = new Scene(nuevaVista);

        Stage ventana = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventana.setScene(escena);
        ventana.show();
    }

    @FXML
    protected void irACompra(ActionEvent event) throws IOException {
        Parent nuevaVista = FXMLLoader.load(getClass().getResource("/edu/co/PANTALLAS/PANTALLA_COMPRA.fxml"));
        Scene escena = new Scene(nuevaVista);

        Stage ventana = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventana.setScene(escena);
        ventana.show();
    }

    @FXML
    protected void irAPago(ActionEvent event) throws IOException {
        Parent nuevaVista = FXMLLoader.load(getClass().getResource("/edu/co/PANTALLAS/PANTALLA_PAGO.fxml"));
        Scene escena = new Scene(nuevaVista);

        Stage ventana = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ventana.setScene(escena);
        ventana.show();
    }
    @FXML
    private TextField cedula;
    @FXML
    private TextField nombre;

    @FXML
    private Label ERROR_M;


    @FXML
    protected void irACompraRegistro(ActionEvent event) throws IOException {


        if (nombre.getText().isEmpty() || cedula.getText().isEmpty()) {
            ERROR_M.setText("Por favor, complete ambos campos\n para iniciar la compra.");
            return;
        } else {
            ERROR_M.setText("");
            negocio.iniciarCompra(nombre.getText(), cedula.getText());

            Parent nuevaVista = FXMLLoader.load(getClass().getResource("/edu/co/PANTALLAS/PANTALLA_COMPRA.fxml"));
            Scene escena = new Scene(nuevaVista);

            Stage ventana = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventana.setScene(escena);
            ventana.show();
        }
    }


    public void Agregar_al_Carrito(ActionEvent actionEvent) {
        // Añade la película seleccionada en la pantalla de compra al carrito (compraactual)
        if (combo_peliculas == null) {
            System.err.println("Agregar_al_Carrito: combo_peliculas es null");
            return;
        }
        Pelicula seleccion = combo_peliculas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            System.err.println("Agregar_al_Carrito: no hay pelicula seleccionada");
            return;
        }

        // Capturar la cantidad del TextField
        int cantidadAgregar = 1;
        try {
            if (cantidad != null && !cantidad.getText().isEmpty()) {
                cantidadAgregar = Integer.parseInt(cantidad.getText());
                if (cantidadAgregar <= 0) {
                    System.err.println("Agregar_al_Carrito: cantidad debe ser mayor a 0");
                    cantidadAgregar = 1;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Agregar_al_Carrito: cantidad inválida, usando 1");
            cantidadAgregar = 1;
        }

        // Agregar una linea a la compra actual con la cantidad especificada
        negocio.agregarLinea(seleccion, cantidadAgregar);
        System.out.println("Agregar_al_Carrito: añadida -> " + seleccion.getNombre() + " cantidad: " + cantidadAgregar);

        // Si la vista actual tiene la lista de carrito (por si el usuario está en la misma pantalla), actualizarla
        if (lista_agregada != null) {
            ObservableList<String> listaItems = FXCollections.observableArrayList();
            for (Linea l : negocio.listarLineas()) {
                Pelicula p = l.getTargeta();
                listaItems.add(p.getNombre() + " - " + l.getCantidad());
            }
            lista_agregada.setItems(listaItems);
            actualizarTotal();
        }
    }

    @FXML
    protected void eliminarSeleccion(ActionEvent event) {
        if (lista_agregada == null) return;
        int pocision_Fila = lista_agregada.getSelectionModel().getSelectedIndex();
        if (pocision_Fila < 0) {
            System.err.println("eliminarSeleccion: no hay elemento seleccionado");
            return;
        }
        // Obtener la linea correspondiente y eliminarla
        if (negocio.listarLineas().size() > pocision_Fila) {
            Linea toRemove = negocio.listarLineas().get(pocision_Fila);
            negocio.eliminarLinea(toRemove);
            System.out.println("eliminarSeleccion: eliminado -> " + (toRemove.getTargeta() != null ? toRemove.getTargeta().getNombre() : "<sin nombre>"));
            // actualizar vista
            ObservableList<String> listaItems = FXCollections.observableArrayList();
            for (Linea l : negocio.listarLineas()) {
                Pelicula p = l.getTargeta();
                listaItems.add(p.getNombre() + " - " + l.getCantidad());
            }
            lista_agregada.setItems(listaItems);
            actualizarTotal();
        }
    }

    @FXML
    protected void reiniciarLista(ActionEvent event) {
        // Limpiar modelo
        negocio.reiniciarCompra();
        // Limpiar vista
        if (lista_agregada != null) {
            lista_agregada.getItems().clear();
        }
        if (TOTAL_PELICULAS_LISTA != null) {
            TOTAL_PELICULAS_LISTA.setText("0.0");
        }
        if (precio_pelicula != null) {
            precio_pelicula.setText("");
        }
        if (combo_peliculas != null) {
            combo_peliculas.getSelectionModel().clearSelection();
        }
        // Resetear campos de pago
        if (Dolar1 != null) Dolar1.clear();
        if (Dolar2 != null) Dolar2.clear();
        if (Dolar5 != null) Dolar5.clear();
        if (Dolar10 != null) Dolar10.clear();
        if (Dolar50 != null) Dolar50.clear();
        if (Dolar100 != null) Dolar100.clear();
        if (Sub_total != null) Sub_total.setText("");
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (Linea l : negocio.listarLineas()) {
            if (l.getTargeta() != null) {
                total += l.getTargeta().getPrecio() * l.getCantidad();
            }
        }
        if (TOTAL_PELICULAS_LISTA != null) {
            TOTAL_PELICULAS_LISTA.setText(String.valueOf(total));
        }
    }



    public void Modificar_cantidad(ActionEvent actionEvent) {
        if (lista_agregada == null) return;
        int idx = lista_agregada.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            System.err.println("Modificar_cantidad: no hay elemento seleccionado");
            return;
        }
        // obtener la linea correspondiente
        if (negocio.listarLineas().size() <= idx) {
            System.err.println("Modificar_cantidad: índice fuera de rango");
            return;
        }
        Linea linea = negocio.listarLineas().get(idx);

        int nuevaCantidad = 1;
        try {
            if (cantidad != null && !cantidad.getText().isEmpty()) {
                nuevaCantidad = Integer.parseInt(cantidad.getText());
                if (nuevaCantidad <= 0) {
                    System.err.println("Modificar_cantidad: la cantidad debe ser mayor que 0");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Modificar_cantidad: cantidad inválida");
            return;
        }

        // aplicar cambio en el modelo
        negocio.modificarCantidad(linea, nuevaCantidad);
        System.out.println("Modificar_cantidad: cambiado -> " + (linea.getTargeta() != null ? linea.getTargeta().getNombre() : "<sin nombre>") + " a " + nuevaCantidad);

        // refrescar vista
        ObservableList<String> listaItems = FXCollections.observableArrayList();
        for (Linea l : negocio.listarLineas()) {
            Pelicula p = l.getTargeta();
            listaItems.add(p.getNombre() + " - " + l.getCantidad());
        }
        lista_agregada.setItems(listaItems);
        actualizarTotal();
    }
}
