package DOM;

import objectDB.Alquiler;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class LeerAlquileresDom {
    private static final String URLXML = "src/data/alquileres.xml";

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse(new File(URLXML));
            documento.getDocumentElement().normalize();

            System.out.printf("Nodo Ra�z: %s %n", documento.getDocumentElement().getNodeName());
            NodeList listaNodosAlquiler = documento.getElementsByTagName("alquiler");
            System.out.printf("N�mero de Nodos alquiler: %d %n", listaNodosAlquiler.getLength());

            for (int i = 0; i < listaNodosAlquiler.getLength(); i++) {
                Node nodoAlquiler = listaNodosAlquiler.item(i);
                if (nodoAlquiler.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodoAlquiler;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String nombre = element.getElementsByTagName("nombreInquilino").item(0).getTextContent();
                    String direccionPiso = element.getElementsByTagName("direccionPiso").item(0).getTextContent();
                    double importeAlquiler = Double.parseDouble(element.getElementsByTagName("importeAlquiler").item(0).getTextContent());
                    int duracionContrato = Integer.parseInt(element.getElementsByTagName("duracionContrato").item(0).getTextContent());

                    Alquiler alquiler = new Alquiler(id,nombre, direccionPiso, importeAlquiler, duracionContrato);
                    System.out.println(alquiler.toString());
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
    }
}
