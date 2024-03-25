package xsistDB;

import entrada.Teclado;
import objectDB.AccesoAlquileres;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import javax.persistence.EntityManagerFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class AccesoServicios {
    public static List<Servicio> listarServicios(Collection coleccion, XPathQueryService service){
        List<Servicio> servicios = new ArrayList<Servicio>();
        try {
            String sentenciaBuscarServicioPorId =
                    "for $s in /ServiciosAsociados/Servicio " +
                            " return $s";


            ResourceSet resultados = service.query(sentenciaBuscarServicioPorId);

            ResourceIterator iterador = resultados.getIterator();
            while (iterador.hasMoreResources()) {
                Resource recurso = iterador.nextResource();
                String xml = (String) recurso.getContent();
                Servicio servicio = obtenerServicioDeXml(xml);
                servicios.add(servicio);
            }
        } catch (XMLDBException e) {
            throw new RuntimeException(e);
        }
        return servicios;
    }

    public static boolean actualizarServicio(Collection coleccion, XPathQueryService service, Servicio servicio){
        try{
            String sentenciaActualizarServicio =
                    "update replace " +
                            "/ServiciosAsociados/Servicio[ID='" + servicio.getId() + "'] " +
                            "with " +
                            "<Servicio>" +
                            "<ID>" + servicio.getId() + "</ID>" +
                            "<TipoServicio>" + servicio.getTipoServicio() + "</TipoServicio>" +
                            "<CostoMensual>" + String.format("%.2f", servicio.getCostoMensual()) + "</CostoMensual>" +
                            "<IdAlquiler>" + servicio.getIdAlquiler() + "</IdAlquiler>" +
                            "</Servicio>";

            ResourceSet resultado = service.query(sentenciaActualizarServicio);
            int serviciosActualizados = Integer.parseInt(resultado.getIterator().nextResource().getContent().toString());
            if(serviciosActualizados > 0){
                return true;
            }
        } catch (XMLDBException e) {
            throw new RuntimeException(e);
        }
        return false;
    }



    private static Servicio obtenerServicioDeXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            Element servicioElement = document.getDocumentElement();

            NodeList idNodeList = servicioElement.getElementsByTagName("ID");
            NodeList tipoServicioNodeList = servicioElement.getElementsByTagName("TipoServicio");
            NodeList costoMensualNodeList = servicioElement.getElementsByTagName("CostoMensual");
            NodeList idAlquilerNodeList = servicioElement.getElementsByTagName("IdAlquiler");

            int id = Integer.parseInt(idNodeList.item(0).getTextContent());
            String tipoServicio = tipoServicioNodeList.item(0).getTextContent();
            String costoMensualStr = costoMensualNodeList.item(0).getTextContent().replace(',', '.');
            double costoMensual = Double.parseDouble(costoMensualStr);

            int idAlquiler = Integer.parseInt(idAlquilerNodeList.item(0).getTextContent());

            return new Servicio(id, idAlquiler, tipoServicio, costoMensual);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void eliminarServicioById(XPathQueryService service, int idServicio){
        try{
            String sentenciaEliminarServicio =
                    "update delete " +
                            "/ServiciosAsociados/Servicio[ID='" + idServicio + "']";
            service.query(sentenciaEliminarServicio);

        } catch (XMLDBException e) {
            throw new RuntimeException(e);
        }
    }

    public static void eliminarServicioByIdAlquiler(XPathQueryService service, int idAlquiler){
        try{
            String sentenciaEliminarServicio =
                    "update delete " +
                            "/ServiciosAsociados/Servicio[IdAlquiler='" + idAlquiler + "']";
            service.query(sentenciaEliminarServicio);

        } catch (XMLDBException e) {
            throw new RuntimeException(e);
        }
    }
    

    public static List<Servicio> listarServiciosByIdAlquiler(XPathQueryService service, int idAlquiler){
        List<Servicio> servicios = new ArrayList<Servicio>();
        try {
            String sentenciaBuscarServicioPorId =
                    "for $s in /ServiciosAsociados/Servicio[IdAlquiler=" + idAlquiler + "]" +
                            " return $s";


            ResourceSet resultados = service.query(sentenciaBuscarServicioPorId);

            ResourceIterator iterador = resultados.getIterator();
            while (iterador.hasMoreResources()) {
                Resource recurso = iterador.nextResource();
                String xml = (String) recurso.getContent();
                Servicio servicio = obtenerServicioDeXml(xml);
                servicios.add(servicio);
            }
        } catch (XMLDBException e) {
            throw new RuntimeException(e);
        }
        return servicios;
    }



    public static Servicio listarServicioById(XPathQueryService service, int id) {
        Servicio servicio = null;
        try {
            String sentenciaBuscarServicioPorId =
                    "for $prod in /ServiciosAsociados/Servicio " +
                            "where $prod/ID = " + id +
                            " return $prod";


            ResourceSet resultados = service.query(sentenciaBuscarServicioPorId);

            ResourceIterator iterador = resultados.getIterator();
            if (iterador.hasMoreResources()) {
                Resource recurso = iterador.nextResource();
                String xml = (String) recurso.getContent();
                servicio = obtenerServicioDeXml(xml);
            }
        } catch (XMLDBException e) {
            throw new RuntimeException(e);
        }
        return servicio;
    }


    public static void insertarServicio(XPathQueryService service, Servicio servicio, EntityManagerFactory emf) {
        try {
            int servicioId = servicio.getId();
            String tipoServicio = servicio.getTipoServicio();
            double costoMensual = servicio.getCostoMensual();
            int idAlquiler = servicio.getIdAlquiler();

            String sentenciaInsertarServicio =
                    "update insert " +
                            "<Servicio>" +
                            "<ID>" + servicioId + "</ID>" +
                            "<TipoServicio>" + tipoServicio + "</TipoServicio>" +
                            "<CostoMensual>" + String.format("%.2f", costoMensual) + "</CostoMensual>" +
                            "<IdAlquiler>" + idAlquiler + "</IdAlquiler>" +
                            "</Servicio> " +
                            "into /ServiciosAsociados";

            service.query(sentenciaInsertarServicio);
        } catch (XMLDBException e) {
            throw new RuntimeException(e);
        }
    }
}
