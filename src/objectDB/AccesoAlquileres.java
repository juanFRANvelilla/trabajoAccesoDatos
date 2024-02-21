package objectDB;

import entrada.Teclado;

import javax.persistence.*;
import java.util.List;

public class AccesoAlquileres {
    private static void menuObjectDB(){
        System.out.println("--------------------------");
        System.out.println("  Submenú de Alquileres  ");
        System.out.println("--------------------------");
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
        do{
            menuObjectDB();
            opcion = Teclado.leerCadena("Elija una opción: ");
            switch (opcion) {
                case "a":
                    insertarAlquiler(emf);
                    break;
                case "b":
                    listarAlquileres(emf);
                    id = Teclado.leerEntero("ID del alquiler a actualizar: ");
                    actualizarAlquilerId(emf, id);
                    break;
                case "c":
                    listarAlquileres(emf);
                    id = Teclado.leerEntero("ID del alquiler a eliminar: ");
                    eliminarAlquiler(emf, id);
                    break;
                case "d":
                    listarAlquileres(emf);
                    break;
                case "e":
                    break;
                case "x":
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }

        } while (!opcion.equals("x"));

    }

    private static void eliminarAlquiler(EntityManagerFactory emf, int id) {
        EntityManager conexion = null;
        EntityTransaction transaccion = null;

        try {
            conexion = emf.createEntityManager();

            conexion = emf.createEntityManager();
            String sentenciaJPQL = "SELECT a FROM Alquiler a WHERE a.id = :id";
            TypedQuery<Alquiler> consulta = conexion.createQuery(sentenciaJPQL, Alquiler.class);
            consulta.setParameter("id", id);

            List<Alquiler> alquileres = consulta.getResultList();

            Alquiler alquiler = alquileres.get(0);

            transaccion = conexion.getTransaction();
            transaccion.begin();
            conexion.remove(alquiler);
            transaccion.commit();

            System.out.println("Se ha eliminado el alquiler: \n" + alquiler.toString());
        } catch (Exception e) {
            if (transaccion != null) {
                transaccion.rollback();
            }
            System.out.println("No se ha encontrado ningun alquiler con ID: " + id);
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }

    private static void actualizarAlquilerId(EntityManagerFactory emf, int id) {
        EntityManager conexion = null;
        EntityTransaction transaccion = null;

        try {
            conexion = emf.createEntityManager();

            conexion = emf.createEntityManager();
            String sentenciaJPQL = "SELECT a FROM Alquiler a WHERE a.id = :id";
            TypedQuery<Alquiler> consulta = conexion.createQuery(sentenciaJPQL, Alquiler.class);
            consulta.setParameter("id", id);

            List<Alquiler> alquileres = consulta.getResultList();

            Alquiler alquiler = alquileres.get(0);
            Alquiler nuevoAlquiler = nuevoAlquiler();

            transaccion = conexion.getTransaction();
            transaccion.begin();
            alquiler.setImporteAlquiler(nuevoAlquiler.getImporteAlquiler());
            alquiler.setNombreInquilino(nuevoAlquiler.getNombreInquilino());
            alquiler.setDireccionPiso(nuevoAlquiler.getDireccionPiso());
            alquiler.setDuracionContrato(nuevoAlquiler.getDuracionContrato());
            transaccion.commit();

            System.out.println("Se ha actualizado el alquiler: \n" + alquiler.toString());
        } catch (Exception e) {
            if (transaccion != null) {
                transaccion.rollback();
            }
            System.out.println("No se ha encontrado ningun alquiler con ID: " + id);
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }

    private static Alquiler nuevoAlquiler(){
        String nombreInquilino = Teclado.leerCadena("Nombre del inquilino: ");
        String direccionPiso = Teclado.leerCadena("Direccion del piso: ");
        double importeAlquiler = Teclado.leerReal("Importe del alquiler: ");
        int duracionContrato = Teclado.leerEntero("Duración del contrato: ");

        return new Alquiler(0,nombreInquilino,direccionPiso,importeAlquiler,duracionContrato);
    }

    private static int nuevoId(EntityManagerFactory emf){
        EntityManager conexion = null;
        Integer nuevoId = 0;

        try {
            conexion = emf.createEntityManager();
            nuevoId = (Integer) conexion.createQuery("SELECT MAX(a.id) FROM Alquiler a")
                    .getSingleResult();

            if (nuevoId == null){
                nuevoId = 0;
            }
        }
        catch (PersistenceException e) {
            System.err.println("La base de datos data/alquileres.odb no existe.");
        }
        finally {
            if (conexion != null) {
                conexion.close();
            }
        }
        return nuevoId += 1;
    }


    public static void insertarAlquiler(EntityManagerFactory emf) {
        Alquiler nuevoAlquiler = nuevoAlquiler();
        EntityManager conexion = null;
        EntityTransaction transaccion = null;


        try {
            conexion = emf.createEntityManager();
            transaccion = conexion.getTransaction();
            transaccion.begin();
            nuevoAlquiler.setId(nuevoId(emf));
            conexion.persist(nuevoAlquiler);
            transaccion.commit();
            System.out.println("Alquiler agregado con éxito.");
        } catch (Exception e) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        } finally {
            conexion.close();
        }
    }

    public static void listarAlquileres(EntityManagerFactory emf) {
        EntityManager conexion = null;
        try {
            conexion = emf.createEntityManager();
            TypedQuery<Alquiler> consulta = conexion.createQuery("SELECT a FROM Alquiler a",
                    Alquiler.class);
            List<Alquiler> alquileres = consulta.getResultList();
            if (alquileres.size() == 0) {
                System.out.println("No hay ningun alquiler en la base de datos.");
            }
            else {
                for (Alquiler alquiler : alquileres) {
                    System.out.println(alquiler.toString());
                }
                System.out.println("Se han consultado " + alquileres.size() +
                        " alquileres de la base de datos");
            }
        }
        catch (PersistenceException e) {
            System.err.println("La base de datos data/alquileres.odb no existe.");
        }
        finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }
}
