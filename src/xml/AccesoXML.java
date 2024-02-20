package xml;

import com.objectdb.o._RollbackException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import objectDB.Alquiler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class AccesoXML {
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

            //Inicio la conexi�n con la base de datos indicada en la Factor�a
            conexion = emf.createEntityManager();

            //Inicio transaccion
            transaccion = conexion.getTransaction();
            transaccion.begin();

            for(Alquiler alquiler: listaAlquileres){
                System.out.println(alquiler.toString());
                conexion.persist(alquiler);
            }

            //Commit de la transacci�n (guardar operaciones)
            transaccion.commit();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (_RollbackException e) {
            //Rollback autom�tico al intentar insertar objetos con un id ya existente.
            System.err.println("Se ha producido un error en la inserci�n de los datos. "
                    + "La base de datos ya existe.");
        }
        catch (Exception e) {
            //Si se ha producido un error antes del commit, forzamos un rollback.
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
