package objectDB;

import entrada.Teclado;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class AccesoAlquileres {


    public static List<Alquiler> listarAlquilerByNombre(EntityManagerFactory emf, String nombrePropietario) {
        EntityManager conexion = null;
        List<Alquiler> alquileres = new ArrayList<Alquiler>();

        try {
            conexion = emf.createEntityManager();
            
            String sentenciaJPQL = "SELECT a FROM Alquiler a WHERE a.nombreInquilino = :nombre";
            TypedQuery<Alquiler> consulta = conexion.createQuery(sentenciaJPQL, Alquiler.class);
            consulta.setParameter("nombre", nombrePropietario);
            alquileres = consulta.getResultList();
        } catch (Exception e) {
            System.out.println("Error al realizar la consulta: " + e.getMessage());
        } finally {
            if (conexion != null && conexion.isOpen()) {
                conexion.close();
            }
        }
        return alquileres;
    }

    public static Alquiler listarAlquilerById(EntityManagerFactory emf, int id) {
        EntityManager conexion = null;
        List<Alquiler> alquileres = new ArrayList<Alquiler>();
        Alquiler alquiler = null;

        try {
            conexion = emf.createEntityManager();

            String sentenciaJPQL = "SELECT a FROM Alquiler a WHERE a.id = :id";
            TypedQuery<Alquiler> consulta = conexion.createQuery(sentenciaJPQL, Alquiler.class);
            consulta.setParameter("id", id);
            alquileres = consulta.getResultList();
            alquiler = alquileres.get(0);
        } catch (Exception e) {
        } finally {
            if (conexion != null && conexion.isOpen()) {
                conexion.close();
            }
        }
        return alquiler;
    }

    public static Alquiler eliminarAlquiler(EntityManagerFactory emf, int id) {
        EntityManager conexion = null;
        EntityTransaction transaccion = null;
        Alquiler alquiler = null;

        try {
            conexion = emf.createEntityManager();

            conexion = emf.createEntityManager();
            String sentenciaJPQL = "SELECT a FROM Alquiler a WHERE a.id = :id";
            TypedQuery<Alquiler> consulta = conexion.createQuery(sentenciaJPQL, Alquiler.class);
            consulta.setParameter("id", id);

            List<Alquiler> alquileres = consulta.getResultList();

            alquiler = alquileres.get(0);

            transaccion = conexion.getTransaction();
            transaccion.begin();
            conexion.remove(alquiler);
            transaccion.commit();
        } catch (Exception e) {
            if (transaccion != null) {
                transaccion.rollback();
            }
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
        return alquiler;
    }

    public static Alquiler actualizarAlquilerId(EntityManagerFactory emf, int id) {
        Alquiler alquiler = null;
        EntityManager conexion = null;
        EntityTransaction transaccion = null;

        try {
            conexion = emf.createEntityManager();

            conexion = emf.createEntityManager();
            String sentenciaJPQL = "SELECT a FROM Alquiler a WHERE a.id = :id";
            TypedQuery<Alquiler> consulta = conexion.createQuery(sentenciaJPQL, Alquiler.class);
            consulta.setParameter("id", id);

            List<Alquiler> alquileres = consulta.getResultList();

            alquiler = alquileres.get(0);
            Alquiler nuevoAlquiler = nuevoAlquiler();

            transaccion = conexion.getTransaction();
            transaccion.begin();
            alquiler.setImporteAlquiler(nuevoAlquiler.getImporteAlquiler());
            alquiler.setNombreInquilino(nuevoAlquiler.getNombreInquilino());
            alquiler.setDireccionPiso(nuevoAlquiler.getDireccionPiso());
            alquiler.setDuracionContrato(nuevoAlquiler.getDuracionContrato());
            transaccion.commit();

        } catch (Exception e) {
            if (transaccion != null) {
                transaccion.rollback();
            }
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
        return alquiler;
    }

    private static Alquiler nuevoAlquiler(){
        boolean tipoContartoValido = false;
        String tipoContrato = "";
        while (!tipoContartoValido){
            tipoContrato = Teclado.leerCadena("Tipo de contrato: ");
            if (tipoContrato.equals("mensual") || tipoContrato.equals("anual") || tipoContrato.equals("temporada")){
                tipoContartoValido = true;
            }
            else {
                System.out.println("Tipo de contrato no válido. Introduzca mensual, anual o temporada.");
            }
        }
        String nombreInquilino = Teclado.leerCadena("Nombre del inquilino: ");
        String direccionPiso = Teclado.leerCadena("Direccion del piso: ");
        double importeAlquiler = Teclado.leerReal("Importe del alquiler: ");
        int duracionContrato = Teclado.leerEntero("Duración del contrato: ");

        return new Alquiler(tipoContrato,nombreInquilino,direccionPiso,importeAlquiler,duracionContrato);
    }

    public static int nuevoId(EntityManagerFactory emf){
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


    public static boolean insertarAlquiler(EntityManagerFactory emf) {
        boolean insertado = false;
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
            insertado = true;
        } catch (Exception e) {
            if (transaccion != null && transaccion.isActive()) {
                transaccion.rollback();
            }
        } finally {
            conexion.close();
        }
        return insertado;
    }

    public static List<Alquiler> listarAlquileres(EntityManagerFactory emf) {
        List<Alquiler> alquileres = new ArrayList<Alquiler>();
        EntityManager conexion = null;
        try {
            conexion = emf.createEntityManager();
            TypedQuery<Alquiler> consulta = conexion.createQuery("SELECT a FROM Alquiler a",
                    Alquiler.class);
            alquileres = consulta.getResultList();

        }
        catch (PersistenceException e) {
            System.err.println("La base de datos data/alquileres.odb no existe.");
        }
        finally {
            if (conexion != null) {
                conexion.close();
            }
        }
        return alquileres;
    }
}
