package Integrador;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LlamadorCredenciales {
    private String url;
    private String user;
    private String password;

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public LlamadorCredenciales() {
    }

    public void cargarCredenciales() {
        Properties propiedades = new Properties();


        try (InputStream archivo = getClass().getResourceAsStream("/config/database.properties")) {
            if (archivo == null) {
                System.out.println("No se encontró el archivo de configuración.");
                return;
            }
            propiedades.load(archivo);
            this.url = propiedades.getProperty("db.url");
            this.user = propiedades.getProperty("db.user");
            this.password = propiedades.getProperty("db.password");

        } catch (IOException e) {
            e.printStackTrace();
        }






    }
}
