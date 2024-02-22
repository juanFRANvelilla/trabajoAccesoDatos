import entrada.Teclado;
import objectDB.AccesoAlquileres;
import objectDB.Alquiler;
import xml.AccesoXML;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

    public static void mainMenu(){
        System.out.print("\n----------------");
        System.out.print("  Menú Principal  ");
        System.out.println("----------------");
        System.out.println("1. Registro de Alquileres (con ObjectDB)");
        System.out.println("2. Registro de Comentarios (con Neodatis)");
        System.out.println("3. Registro de Servicios Asociados (con existDB)");
        System.out.println("4. Gestión de Información en XML (con Xtream)");
        System.out.println("5. Salir");
        System.out.println("");
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("src/data/alquileres.odb");

        int opcion = 0;

        do {
            mainMenu();
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
                    accionesXML(emf);
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

    private static void menuXML() {
        System.out.print("\n------");
        System.out.print("  Submenú Gestión de Información en XML  ");
        System.out.println("------");
        System.out.println("a. Exportar");
        System.out.println("b. Importar");
        System.out.println("x. Volver al menú principal");
    }


    public static void accionesXML(EntityManagerFactory emf){
        String opcion = "";

        do {
            menuXML();
            opcion = Teclado.leerCadena("Elija una opción: ");

            switch (opcion) {
                case "a":
                    break;
                case "b":
                    List<Alquiler> listaAlquileres = AccesoXML.importarAlquileres(emf);
                    System.out.println("Número de alquileres importados: " + listaAlquileres.size());
                    break;
                case "x":
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (!opcion.equals("x"));
    }
}