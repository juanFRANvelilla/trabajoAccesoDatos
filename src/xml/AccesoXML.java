package xml;

import com.objectdb.o._RollbackException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import entrada.Teclado;
import objectDB.AccesoAlquileres;
import objectDB.Alquiler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AccesoXML {
    public static List<Alquiler> importarAlquileres(EntityManagerFactory emf){
        XStream xStream = new XStream();

        xStream.alias("alquileres", ListaAlquileres.class);
        xStream.alias("alquiler", Alquiler.class);
        xStream.aliasField("nombreInquilino", Alquiler.class, "nombreInquilino");
        xStream.aliasField("direccionPiso", Alquiler.class, "direccionPiso");
        xStream.aliasField("importeAlquiler", Alquiler.class, "importeAlquiler");
        xStream.aliasField("duracionContrato", Alquiler.class, "duracionContrato");
        xStream.addImplicitCollection(ListaAlquileres.class, "listaAlquileres");
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.omitField(Alquiler.class, "id");

        List<Alquiler> listaAlquileres = new ArrayList<Alquiler>();

        EntityManager conexion = null;
        EntityTransaction transaccion = null;

        try{
            ListaAlquileres objetoListaAlquileres =
                    (ListaAlquileres) xStream
                            .fromXML(new FileInputStream("src/data/alquileres.xml"));

            listaAlquileres = objetoListaAlquileres.getListaAlquileres();

            conexion = emf.createEntityManager();
            transaccion = conexion.getTransaction();
            transaccion.begin();

//            conexion.createQuery("DELETE FROM Alquiler").executeUpdate();
            int id = AccesoAlquileres.nuevoId(emf);

            for(Alquiler alquiler: listaAlquileres){
                alquiler.setId(id);
                conexion.persist(alquiler);
                id++;
            }


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
        return listaAlquileres;
    }
}
