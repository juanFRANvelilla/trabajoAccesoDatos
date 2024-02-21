package xml;

import com.objectdb.o._RollbackException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import entrada.Teclado;
import objectDB.Alquiler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class AccesoXML {

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
                    importarAlquileres(emf);
                    break;
                case "x":
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (!opcion.equals("x"));
    }




    public static void importarAlquileres(EntityManagerFactory emf){
        XStream xStream = new XStream();

        xStream.alias("alquileres", ListaAlquileres.class);
        xStream.alias("alquiler", Alquiler.class);
        xStream.aliasField("id", Alquiler.class, "id");
        xStream.aliasField("nombreInquilino", Alquiler.class, "nombreInquilino");
        xStream.aliasField("direccionPiso", Alquiler.class, "direccionPiso");
        xStream.aliasField("importeAlquiler", Alquiler.class, "importeAlquiler");
        xStream.aliasField("duracionContrato", Alquiler.class, "duracionContrato");
        xStream.addImplicitCollection(ListaAlquileres.class, "listaAlquileres");
        xStream.addPermission(AnyTypePermission.ANY);



        EntityManager conexion = null;
        EntityTransaction transaccion = null;

        try{
            ListaAlquileres objetoListaAlquileres =
                    (ListaAlquileres) xStream
                            .fromXML(new FileInputStream("src/data/alquileres.xml"));

            List<Alquiler> listaAlquileres = objetoListaAlquileres.getListaAlquileres();

            conexion = emf.createEntityManager();
            transaccion = conexion.getTransaction();
            transaccion.begin();

            conexion.createQuery("DELETE FROM Alquiler").executeUpdate();

            for(Alquiler alquiler: listaAlquileres){
                conexion.persist(alquiler);
            }
            System.out.println("Número de alquileres importados: " + listaAlquileres.size());

            transaccion.commit();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (_RollbackException e) {
            System.err.println("Se ha producido un error en la inserci�n de los datos. "
                    + "La base de datos ya existe.");
        }
        catch (Exception e) {
            if(transaccion!=null) {
                if (transaccion.isActive()) {
                    transaccion.rollback();
                }
            }
            throw e;
        }
        finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }
}
