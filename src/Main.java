import entrada.Teclado;
import objectDB.AccesoAlquileres;
import xml.AccesoXML;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    public static void menu(){
        System.out.print("-----------------");
        System.out.print("Menú Principal");
        System.out.println("-----------------");
        System.out.println("1. Registro de Alquileres (con ObjectDB)");
        System.out.println("2. Registro de Comentarios (con Neodatis)");
        System.out.println("3. Registro de Servicios Asociados (con existDB)");
        System.out.println("4. Gestión de Información en XML (con Xtream)");
        System.out.println("5. Salir");
        System.out.println("");
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("src/data/alquileres.odb");
//        AccesoXML.importarAlquileres(emf);
        int opcion = 0;

        do {
            menu();
            opcion = Teclado.leerEntero("Elija una opción: ");
            switch (opcion) {
                case 1:
                    AccesoAlquileres.accionesAlquileres(emf);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opción no válida. Debe introducir un valor entre 1 y 5");
                    break;

            }
        }
        while (opcion != 5) ;
        emf.close();
    }
}