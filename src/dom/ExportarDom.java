package dom;

import neodatisDB.Comentario;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import xsistDB.Servicio;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportarDom {
    private static final String URLSERVICIOS = "src/data/servicios.xml";
    private static final String URLCOMENTARIOS = "src/data/comentarios.xml";

    public static void exportarServicios(List<Servicio> servicioList) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();

            Element rootElement = documento.createElement("servicios");
            documento.appendChild(rootElement);

            for (Servicio servicio : servicioList) {
                Element servicioElement = documento.createElement("servicio");
                rootElement.appendChild(servicioElement);

                Element idElement = documento.createElement("id");
                idElement.appendChild(documento.createTextNode(String.valueOf(servicio.getId())));
                servicioElement.appendChild(idElement);

                Element idAlquilerElement = documento.createElement("idAlquiler");
                idAlquilerElement.appendChild(documento.createTextNode(String.valueOf(servicio.getIdAlquiler())));
                servicioElement.appendChild(idAlquilerElement);

                Element tipoServicioElement = documento.createElement("tipoServicio");
                tipoServicioElement.appendChild(documento.createTextNode(servicio.getTipoServicio()));
                servicioElement.appendChild(tipoServicioElement);

                Element costoMensualElement = documento.createElement("costoMensual");
                costoMensualElement.appendChild(documento.createTextNode(String.valueOf(servicio.getCostoMensual())));
                servicioElement.appendChild(costoMensualElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File(URLSERVICIOS));
            transformer.transform(source, result);

            System.out.println("Datos exportados correctamente a " + URLSERVICIOS);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }


    public static void exportarComentarios(List<Comentario> comentarioList) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();

            Element rootElement = documento.createElement("comentarios");
            documento.appendChild(rootElement);

            for (Comentario comentario : comentarioList) {
                Element comentarioElement = documento.createElement("comentario");
                rootElement.appendChild(comentarioElement);

                Element idElement = documento.createElement("id");
                idElement.appendChild(documento.createTextNode(String.valueOf(comentario.getId())));
                comentarioElement.appendChild(idElement);

                Element idAlquilerElement = documento.createElement("idAlquiler");
                idAlquilerElement.appendChild(documento.createTextNode(String.valueOf(comentario.getIdAlquiler())));
                comentarioElement.appendChild(idAlquilerElement);

                Element tipoComentarioElement = documento.createElement("comentarioDescripcion");
                tipoComentarioElement.appendChild(documento.createTextNode(comentario.getComentario()));
                comentarioElement.appendChild(tipoComentarioElement);

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File(URLCOMENTARIOS));
            transformer.transform(source, result);

            System.out.println("Datos exportados correctamente a " + URLCOMENTARIOS);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

}
