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
                    accionesAlquileres(emf);
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

    private static void menuObjectDB(){
        System.out.print("\n---------");
        System.out.print("  Submenú de Alquileres  ");
        System.out.println("---------");
        System.out.println("a. Insertar alquiler");
        System.out.println("b. Actualizar alquiler");
        System.out.println("c. Eliminar alquiler");
        System.out.println("d. Listar alquileres");
        System.out.println("e. Buscar alquiler por nombre de inquilino");
        System.out.println("x. Volver al Menú Principal");
        System.out.println("");
    }

    public static void accionesAlquileres(EntityManagerFactory emf) {
        String opcion = "";
        int id;
        String nombrePropietario;
        Alquiler alquiler;
        List<Alquiler> alquileres;
        do{
            menuObjectDB();
            opcion = Teclado.leerCadena("Elija una opción: ");
            switch (opcion) {
                case "a":
                    if(AccesoAlquileres.insertarAlquiler(emf)){
                        System.out.println("Alquiler agregado con éxito.");
                    }
                    else{
                        System.out.println("Se ha producido un error al insertar el nuevo alquiler.");
                    }
                    break;
                case "b":
                    alquileres = AccesoAlquileres.listarAlquileres(emf);
                    printearAlquileres(alquileres);
                    id = Teclado.leerEntero("ID del alquiler a actualizar: ");
                    alquiler = AccesoAlquileres.actualizarAlquilerId(emf, id);
                    if(alquiler != null){
                        System.out.println("Se ha actualizado el alquiler: \n" + alquiler.toString());
                    }
                    else{
                        System.out.println("No se ha encontrado ningun alquiler con ID: " + id);
                    }
                    break;
                case "c":
                    alquileres = AccesoAlquileres.listarAlquileres(emf);
                    printearAlquileres(alquileres);
                    id = Teclado.leerEntero("ID del alquiler a eliminar: ");
                    alquiler = AccesoAlquileres.eliminarAlquiler(emf, id);
                    if(alquiler != null){
                        System.out.println("Se ha eliminado el alquiler: \n" + alquiler.toString());
                    }
                    else{
                        System.out.println("No se ha encontrado ningun alquiler con ID: " + id);
                    }
                    break;
                case "d":
                    alquileres = AccesoAlquileres.listarAlquileres(emf);
                    printearAlquileres(alquileres);
                    break;
                case "e":
                    nombrePropietario = Teclado.leerCadena("Nombre del propietario del que desees consultar alquileres: ");
                    alquileres = AccesoAlquileres.listarAlquilerByNombre(emf,nombrePropietario);
                    printearAlquileres(alquileres);
                    break;
                case "x":
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }

        } while (!opcion.equals("x"));

    }

    private static void printearAlquileres(List<Alquiler> alquileres) {
        if (alquileres.size() == 0) {
            System.out.println("No se ha encontrado ningun alquiler en la base de datos.");
        }
        else {
            for (Alquiler alquiler : alquileres) {
                System.out.println(alquiler.toString());
            }
            if(alquileres.size() == 1){
                System.out.println("Se ha consultado " + alquileres.size() +
                        " alquiler de la base de datos");
            } else{
                System.out.println("Se han consultado " + alquileres.size() +
                        " alquileres de la base de datos");
            }
        }
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