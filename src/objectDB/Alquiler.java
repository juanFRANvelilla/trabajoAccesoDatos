package objectDB;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Alquiler {
    @Id
    int id;
    String tipoContrato;
    String nombreInquilino;
    String direccionPiso;
    Double importeAlquiler;
    int duracionContrato;

    public Alquiler(String tipoContrato, String nombreInquilino, String direccionPiso, Double importeAlquiler, int duracionContrato) {
        this.tipoContrato = tipoContrato;
        this.nombreInquilino = nombreInquilino;
        this.direccionPiso = direccionPiso;
        this.importeAlquiler = importeAlquiler;
        this.duracionContrato = duracionContrato;
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
        return "Alquiler{" +
                "id=" + id +
                ", tipoContrato='" + tipoContrato + '\'' +
                ", nombreInquilino='" + nombreInquilino + '\'' +
                ", direccionPiso='" + direccionPiso + '\'' +
                ", importeAlquiler=" + importeAlquiler +
                ", duracionContrato=" + duracionContrato +
                '}';
    }
}
