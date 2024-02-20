import xml.AccesoXML;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("src/data/alquileres.odb");

        AccesoXML.importarAlquileres(emf);
        emf.close();
    }
}