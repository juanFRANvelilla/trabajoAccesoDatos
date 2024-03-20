package xsistDB;

public class Servicio {
    private int id;
    private int idAlquiler;
    private String tipoServicio;
    private double costoMensual;

    public Servicio(int id, int idAlquiler, String tipoServicio, double costoMensual) {
        this.id = id;
        this.idAlquiler = idAlquiler;
        this.tipoServicio = tipoServicio;
        this.costoMensual = costoMensual;
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

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public double getCostoMensual() {
        return costoMensual;
    }

    public void setCostoMensual(double costoMensual) {
        this.costoMensual = costoMensual;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", idAlquiler=" + idAlquiler +
                ", tipoServicio='" + tipoServicio + '\'' +
                ", costoMensual=" + costoMensual +
                '}';
    }
}
