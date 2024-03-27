package dom;

import neodatisDB.Comentario;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import xsistDB.Servicio;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportarDom {
    private static final String URLSERVICIOS = "src/data/servicios.xml";
    private static final String URLCOMENTARIOS = "src/data/comentarios.xml";
    
    public static List<Servicio> importarServicios(){
        List<Servicio> servicioList = new ArrayList<Servicio>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(new File(URLSERVICIOS));
            documento.getDocumentElement().normalize();

            NodeList listaNodosServicio = documento.getElementsByTagName("servicio");

            for (int i = 0; i < listaNodosServicio.getLength(); i++) {
                Node nodoServicio = listaNodosServicio.item(i);
                if (nodoServicio.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodoServicio;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    int idAlquiler = Integer.parseInt(element.getElementsByTagName("idAlquiler").item(0).getTextContent());
                    String tipoServicio = element.getElementsByTagName("tipoServicio").item(0).getTextContent();
                    Double costoMensual = Double.parseDouble(element.getElementsByTagName("costoMensual").item(0).getTextContent());

                    Servicio servicio = new Servicio(id,idAlquiler,tipoServicio,costoMensual);
                    servicioList.add(servicio);
                }
            }
        }
        catch (ParserConfigurationException pce) {
            System.out.println("Error de Configuraci�n del Analizador Sint�ctico: " + pce.getMessage());
            pce.printStackTrace();
        }
        catch (IOException ioe) {
            System.out.println("Error de Entrada/Salida: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        catch (SAXException saxe) {
            System.out.println("Error de API Simple para XML: " + saxe.getMessage());
            saxe.printStackTrace();
        }
        catch (DOMException dome) {
            System.out.println("Error de Modelo de Objetos de Documento: " + dome.getMessage());
            dome.printStackTrace();
        }
        return servicioList;
    }
    
    
    public static List<Comentario> importarComentarios() {
        List<Comentario> comentarioList = new ArrayList<Comentario>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(new File(URLCOMENTARIOS));
            documento.getDocumentElement().normalize();

            NodeList listaNodosComentario = documento.getElementsByTagName("comentario");

            for (int i = 0; i < listaNodosComentario.getLength(); i++) {
                Node nodoComentario = listaNodosComentario.item(i);
                if (nodoComentario.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodoComentario;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    int idAlquiler = Integer.parseInt(element.getElementsByTagName("idAlquiler").item(0).getTextContent());
                    String comentarioContenido = element.getElementsByTagName("comentarioDescripcion").item(0).getTextContent();

                    Comentario comentario = new Comentario(id, idAlquiler, comentarioContenido);
                    comentarioList.add(comentario);
                }
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("Error de Configuraci�n del Analizador Sint�ctico: " + pce.getMessage());
            pce.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Error de Entrada/Salida: " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (SAXException saxe) {
            System.out.println("Error de API Simple para XML: " + saxe.getMessage());
            saxe.printStackTrace();
        } catch (DOMException dome) {
            System.out.println("Error de Modelo de Objetos de Documento: " + dome.getMessage());
            dome.printStackTrace();
        }
        return comentarioList;
    }
}
