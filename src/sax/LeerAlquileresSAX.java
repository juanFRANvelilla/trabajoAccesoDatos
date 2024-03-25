package sax;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class LeerAlquileresSAX {
    private static final String URLXML = "src/data/alquileres.xml";

    public static void main(String[] args) {
        try {
            XMLReader lectorXML = XMLReaderFactory.createXMLReader();
            MyContentManager mcm = new MyContentManager();
            lectorXML.setContentHandler(mcm);
            InputSource origen = new InputSource(URLXML);
            lectorXML.parse(origen);
        }
        catch (FileNotFoundException fnfe) {
            System.out.println("Error de Fichero No Encontrado: " + fnfe.getMessage());
            fnfe.printStackTrace();
        }
        catch (IOException ioe) {
            System.out.println("Error de Entrada/Salida: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        catch (SAXException saxe) {
            System.out.println("Error de API Simple para XML: " + saxe.getMessage());
            saxe.printStackTrace();
        }
    }
}
