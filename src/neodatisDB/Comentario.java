package neodatisDB;

public class Comentario {
    private int id;
    private int idAlquiler;
    private String comentario;

    public Comentario(int id, int idAlquiler, String comentario) {
        this.id = id;
        this.idAlquiler = idAlquiler;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "id=" + id +
                ", idAlquiler=" + idAlquiler +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
