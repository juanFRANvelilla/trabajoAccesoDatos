import entrada.Teclado;
import neodatisDB.AccesoComentarios;
import neodatisDB.Comentario;
import objectDB.AccesoAlquileres;
import objectDB.Alquiler;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;
import xml.AccesoXML;
import xsistDB.AccesoServicios;
import xsistDB.Servicio;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void mainMenu(){
        System.out.print("\n----------------");
        System.out.print(" Menú Principal ");
        System.out.println("----------------");
        System.out.println("1. Registro de Alquileres (con ObjectDB)");
        System.out.println("2. Registro de Comentarios (con Neodatis)");
        System.out.println("3. Registro de Servicios Asociados (con existDB)");
        System.out.println("4. Gestión de Información en XML (con Xtream)");
        System.out.println("5. Salir");
        System.out.println();
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("src/data/alquileres.odb");
        ODB odb = ODBFactory.open("ComentariosInquilinos.neodatis");

        int opcion = 0;

        do {
            mainMenu();
            opcion = Teclado.leerEntero("Elija una opción: ");
            switch (opcion) {
                case 1:
                    accionesAlquileres(emf);
                    break;
                case 2:
                    accionesComentarios(emf, odb);
                    break;
                case 3:
                    Collection coleccion = null;
                    try {
                        Class cl = Class.forName("org.exist.xmldb.DatabaseImpl");
                        Database database = (Database) cl.newInstance();
                        DatabaseManager.registerDatabase(database);

                        String url = "xmldb:exist://localhost:8080/exist/xmlrpc/db/serviciosAsociados";
                        coleccion = DatabaseManager.getCollection(url, "admin", "admin");
                        XPathQueryService service =
                                (XPathQueryService) coleccion.getService("XPathQueryService", "1.0");
                        if (coleccion != null) {
                            System.out.println("Conectado con exito a la coleccion de la base de datos");
                            accionesServicios(emf, coleccion, service);
                        }

                    } catch (XMLDBException e) {
                        System.out.println("Error al conectarte a la base de datos xistDB");
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (coleccion != null) {
                            try {
                                coleccion.close();
                            }
                            catch (XMLDBException xmldbe) {
                                System.out.println("Error de base de datos XML: " + xmldbe.getMessage());
                            }
                        }
                    }

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
        odb.close();
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
        System.out.println();
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
                    mostrarAlquileres(emf);
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
                    mostrarAlquileres(emf);
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
                    mostrarAlquileres(emf);
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

    private static void mostrarAlquileres(EntityManagerFactory emf) {
        List<Alquiler> alquileres = AccesoAlquileres.listarAlquileres(emf);
        printearAlquileres(alquileres);
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

    private static boolean comprobarIdAlquiler(EntityManagerFactory emf, int idAlquiler){
        if(AccesoAlquileres.listarAlquilerById(emf,idAlquiler) != null){
            return true;
        } else {
            System.out.println("No se ha encontrado ningun alquiler con ID: " + idAlquiler);
            return false;
        }
    }

    public static void menuComentarios(){
        System.out.print("\n------");
        System.out.print(" Menú de comentarios ");
        System.out.println("------");
        System.out.println("a. Insertar comentario");
        System.out.println("b. Eliminar comentario");
        System.out.println("c. Buscar comentarios");
        System.out.println("x. Volver al Menú Principal");
    }

    public static void accionesComentarios(EntityManagerFactory emf, ODB odb) {
        List<Comentario> comentarios = new ArrayList<Comentario>();
        Comentario comentario;
        String opcion = "";
        int id, idAlquiler;
        do {
            menuComentarios();
            opcion = Teclado.leerCadena("Elija una opción: ");
            switch (opcion) {
                case "a":
                    id = Teclado.leerEntero("ID: ");
                    if(AccesoComentarios.listarComentarioById(odb, id) == null){
                        mostrarAlquileres(emf);
                        idAlquiler = Teclado.leerEntero("ID alquiler: ");
                        if(comprobarIdAlquiler(emf, idAlquiler)) {
                            String descripcion = Teclado.leerCadena("Comentario: ");
                            AccesoComentarios.insertarComentario(odb, new Comentario(id,idAlquiler,descripcion));
                            System.out.println("Comentario insertado en la base de datos correctamente.");
                        }
                    } else {
                        System.out.println("El ID del comentario ya existe. No se permiten ID duplicados");
                    }
                    break;
                case "b":
                    comentarios = AccesoComentarios.listarComentarios(odb);
                    printearComentarios(comentarios);
                    id = Teclado.leerEntero("ID del comentario a eliminar: ");
                    comentario = AccesoComentarios.listarComentarioById(odb,id);
                    if(comentario != null){
                        AccesoComentarios.borrarComentario(odb, comentario);
                        System.out.println("Comentario borrado correctamente.");
                    }
                    else {
                        System.out.println("No hay ningun comentario registrado con ID: " + id);
                    }

                    break;
                case "c":
                    mostrarAlquileres(emf);
                    idAlquiler = Teclado.leerEntero("ID del alquiler: ");
                    if(comprobarIdAlquiler(emf, idAlquiler)) {
                        comentarios = AccesoComentarios.listarComentarioByIdAlquiler(odb,idAlquiler);
                        printearComentarios(comentarios);
                    }

                    break;
                case "x":
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        while(!opcion.equals("x"));
    }

    private static void printearComentarios(List<Comentario> comentarios) {
        for(Comentario c : comentarios){
            System.out.println(c.toString());
        }
    }

    private static void mostrarServicios(Collection coleccion, XPathQueryService service){
        List<Servicio> servicios;
        servicios = AccesoServicios.listarServicios(coleccion, service);
        if(servicios.size() == 0){
            System.out.println("No hay servicios en la base de datos");
        }
        else{
            for(Servicio servicio: servicios){
                System.out.println(servicio.toString());
            }
            System.out.println("Se han consultado " + servicios.size() + " servicios");
        }
    }

    private static void menuServicios(){
        System.out.print("\n-------");
        System.out.print("Submenú de registro de servicios asociados");
        System.out.println("-------");
        System.out.println("a. Insertar servicio");
        System.out.println("b. Actualizar servicio");
        System.out.println("c. Eliminar servicio");
        System.out.println("d. Eliminar servicios por ID de alquiler");
        System.out.println("e. Listar servicios por ID de alquiler");
        System.out.println("x. Volver al Menú Principal");
        System.out.println();
    }



    private static void accionesServicios(EntityManagerFactory emf, Collection coleccion, XPathQueryService service) {
        String opcion;
        Servicio servicio;
        int idServicio, idAlquiler;
        List<Alquiler> alquileres;
        List<Servicio> servicios;


        do{
            menuServicios();
            opcion = Teclado.leerCadena("Elija una opción: ");
            switch (opcion) {
                case "a":
                    mostrarServicios(coleccion,service);
                    idServicio = Teclado.leerEntero("ID servicio: ");
                    if(AccesoServicios.listarServicioById(service,idServicio) == null){
                        mostrarAlquileres(emf);
                        idAlquiler = Teclado.leerEntero("ID del alquiler: ");
                        if(comprobarIdAlquiler(emf, idAlquiler)) {
                            servicio = crearServicio(idServicio, idAlquiler);
                            AccesoServicios.insertarServicio(service, servicio, emf);
                            System.out.println("Se ha insetado correctamente el servicio: \n" + servicio.toString());
                        }
                    }
                    else {
                        System.out.println("Error al insertar, ya existe un servicio con ID: " + idServicio);
                    }
                    break;
                case "b":
                    mostrarServicios(coleccion,service);
                    idServicio = Teclado.leerEntero("ID servicio: ");
                    servicio = AccesoServicios.listarServicioById(service,idServicio);
                    if(servicio != null){
                        servicio = actualizarServicio(servicio);
                        if(servicio == null){
                            System.out.println("El nuevo costo del servicio no respueta los limites");
                        } else{
                            mostrarAlquileres(emf);
                            idAlquiler = Teclado.leerEntero("ID del alquiler: ");
                            if(comprobarIdAlquiler(emf, idAlquiler)) {
                                servicio.setIdAlquiler(idAlquiler);
                                if(AccesoServicios.actualizarServicio(coleccion,service,servicio)){
                                    System.out.println("Se ha actualizado con exito un servicio en la base de datos");
                                }
                            }
                        }
                    } else {
                        System.out.println("No hay ningun servicio con ID: " + idServicio);
                    }
                    break;
                case "c":
                    mostrarServicios(coleccion,service);
                    idServicio = Teclado.leerEntero("ID del servicio a eliminar: ");
                    if(AccesoServicios.listarServicioById(service, idServicio) != null){
                        AccesoServicios.eliminarServicioById(service, idServicio);
                        System.out.println("Servicio con ID: " + idServicio + " borrado exitosamente de la base de datos");
                    } else {
                        System.out.println("No hay ningun servicio con ID: " + idServicio);
                    }
                    break;
                case "d":
                    mostrarAlquileres(emf);
                    idAlquiler = Teclado.leerEntero("ID del alquiler: ");
                    if(comprobarIdAlquiler(emf, idAlquiler)) {
                        AccesoServicios.eliminarServicioByIdAlquiler(service, idAlquiler);
                        System.out.println("Servicios asociados al ID alquiler: " + idAlquiler + " eliminados exitosamente de la base de datos");
                    }
                    break;
                case "e":
                    mostrarAlquileres(emf);
                    idAlquiler = Teclado.leerEntero("ID del alquiler: ");
                    if(comprobarIdAlquiler(emf, idAlquiler)) {
                        servicios = AccesoServicios.listarServiciosByIdAlquiler(service, idAlquiler);
                        printearServicios(servicios);
                    }
                    break;
                case "x":
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        } while(!opcion.equals("x"));
    }

    private static void printearServicios(List<Servicio> servicios) {
        if(servicios.size() > 0){
            for(Servicio servicio: servicios){
                System.out.println(servicio.toString());
            }
            if(servicios.size() == 1){
                System.out.println("Se ha consultado " + servicios.size() + " servicio de la base de datos");
            } else {
                System.out.println("Se han consultado " + servicios.size() + " servicios de la base de datos");
            }

        } else {
            System.out.println("No se ha encontrado ningun servicio en la base de datos");
        }
    }

    private static Servicio actualizarServicio(Servicio servicio) {
        double limiteMin = servicio.getCostoMensual() * 0.75;
        double limiteMax = servicio.getCostoMensual() * 1.25;
        String tipoServicio = Teclado.leerCadena("Tipo de servicio: ");
        double costoServico = Teclado.leerReal("Costo mensual: (debe estar comprendido entre " + limiteMin + " y " + limiteMax + ") ");
        if(costoServico < limiteMin || costoServico > limiteMax){
            return null;
        }
        return new Servicio(servicio.getId(), servicio.getIdAlquiler(), tipoServicio, costoServico);
    }

    private static Servicio crearServicio(int codigo, int idAlquiler) {
        String tipoServicio = Teclado.leerCadena("Tipo de servicio: ");
        double costo = Teclado.leerReal("Costo mensual del servicio: ");
        return new Servicio(codigo, idAlquiler, tipoServicio, costo);
    }


    private static void menuXML() {
        System.out.print("\n------");
        System.out.print(" Submenú Gestión de Información en XML ");
        System.out.println("------");
        System.out.println("a. Exportar");
        System.out.println("b. Importar");
        System.out.println("x. Volver al menú principal");
    }


    public static void accionesXML(EntityManagerFactory emf){
        String opcion = "";
        List<Alquiler> listaAlquileres;

        do {
            menuXML();
            opcion = Teclado.leerCadena("Elija una opción: ");

            switch (opcion) {
                case "a":
                    listaAlquileres = AccesoXML.exportarAlquileres(emf);
                    System.out.println("Se han escrito " + listaAlquileres.size() +
                            " empleados en el fichero XML.");
                    break;
                case "b":
                    listaAlquileres = AccesoXML.importarAlquileres(emf);
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