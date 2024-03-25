package sax;

import java.io.*;
import java.util.List;

import objectDB.Alquiler;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class LeerAlumnosDeXML2 {

	public static void main(String[] args) {
		try {
			XMLReader  lectorXML = XMLReaderFactory.createXMLReader();
			MyContentManager2 mcm2 = new MyContentManager2();  
			lectorXML.setContentHandler(mcm2);
			InputSource origen = new InputSource("src/data/alquileres.xml");
			lectorXML.parse(origen);  
			List<Alquiler> listaAlquilers = mcm2.getListaAlquilers();
			for (Alquiler alquiler : listaAlquilers) {
				System.out.println(alquiler.toString());
			}
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
