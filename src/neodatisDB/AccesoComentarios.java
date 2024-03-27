package neodatisDB;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import java.util.ArrayList;
import java.util.List;

public class AccesoComentarios {


    public static void insertarComentario(ODB odb, Comentario comentario){
        odb.store(comentario);
    }


    public static Comentario listarComentarioById(ODB odb, int id){
        Comentario comentario = null;
        IQuery query = new CriteriaQuery(Comentario.class, Where.equal("id", id));
        Objects<Comentario> comentarios = odb.getObjects(query);
        if(comentarios.size() > 0){
            comentario = comentarios.getFirst();
        }
        return comentario;
    }

    public static List<Comentario> listarComentarioByIdAlquiler(ODB odb, int id){
        List<Comentario> comentariosList = new ArrayList<Comentario>();
        IQuery query = new CriteriaQuery(Comentario.class, Where.equal("idAlquiler", id));
        Objects<Comentario> comentariosObjeto = odb.getObjects(query);
        for(Comentario c : comentariosObjeto){
            comentariosList.add(c);
        }
        return comentariosList;
    }

    public static List<Comentario> listarComentarios(ODB odb){
        List<Comentario> comentariosList = new ArrayList<Comentario>();
        Objects<Comentario> comentariosObjeto = odb.getObjects(Comentario.class);
        for(Comentario c : comentariosObjeto){
            comentariosList.add(c);
        }
        return comentariosList;
    }

    public static void borrarComentario(ODB odb, Comentario comentario){
        odb.delete(comentario);
    }
}
