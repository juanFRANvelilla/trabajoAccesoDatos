package objectDB;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Alquiler {
    public static final int TAMAGNO_REGISTRO = 76;
    public static final int LONGITUD_NOMBRE = 30;

    @Id
    int id; // 4 bytes
    String nombreInquilino;   // 30 caracteres de 2 bytes = 60 bytes
    String direccionPiso;  // 30 caracteres de 2 bytes = 60 bytes
    Double importeAlquiler;   // 8 bytes
    int duracionContrato; // 4 bytes
    private Map<String,String> atributos;

    public Alquiler() {
        atributos = new HashMap<String,String>();
    }
    public Alquiler(String nombreInquilino, String direccionPiso, Double importeAlquiler, int duracionContrato) {
        this.nombreInquilino = nombreInquilino;
        this.direccionPiso = direccionPiso;
        this.importeAlquiler = importeAlquiler;
        this.duracionContrato = duracionContrato;
    }

    public Alquiler(int id, String nombreInquilino, String direccionPiso, Double importeAlquiler, int duracionContrato) {
        this.id = id;
        this.nombreInquilino = nombreInquilino;
        this.direccionPiso = direccionPiso;
        this.importeAlquiler = importeAlquiler;
        this.duracionContrato = duracionContrato;
    }

    public void insertar(String clave, String valor) {
        atributos.put(clave, valor);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreInquilino() {
        return nombreInquilino;
    }

    public void setNombreInquilino(String nombreInquilino) {
        this.nombreInquilino = nombreInquilino;
    }

    public String getDireccionPiso() {
        return direccionPiso;
    }

    public void setDireccionPiso(String direccionPiso) {
        this.direccionPiso = direccionPiso;
    }

    public Double getImporteAlquiler() {
        return importeAlquiler;
    }

    public void setImporteAlquiler(Double importeAlquiler) {
        this.importeAlquiler = importeAlquiler;
    }

    public int getDuracionContrato() {
        return duracionContrato;
    }

    public void setDuracionContrato(int duracionContrato) {
        this.duracionContrato = duracionContrato;
    }

    @Override
    public String toString() {
        return "ALQUILER {" +
                "id=" + id +
                ", nombreInquilino='" + nombreInquilino + '\'' +
                ", direccionPiso='" + direccionPiso + '\'' +
                ", importeAlquiler=" + importeAlquiler +
                ", duracionContrato=" + duracionContrato +
                '}';
    }
}
