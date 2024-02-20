package xml;


import objectDB.Alquiler;

import java.util.ArrayList;
import java.util.List;

public class ListaAlquileres {
    private List<Alquiler> listaAlquileres;

    public ListaAlquileres() {
        this.listaAlquileres = new ArrayList<Alquiler>();
    }

    public void add(Alquiler alquiler){
        listaAlquileres.add(alquiler);
    }

    public List<Alquiler> getListaAlquileres() {
        return listaAlquileres;
    }

    public void setListaAlquileres(List<Alquiler> listaAlquileres) {
        this.listaAlquileres = listaAlquileres;
    }
}
