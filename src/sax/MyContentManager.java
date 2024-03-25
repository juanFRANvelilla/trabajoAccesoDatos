package sax;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

class MyContentManager extends DefaultHandler {
	
	// nivel de indentaci�n o tabulado
	int nivelIndentacion;
	
	// Constructor
	public MyContentManager() {
		super();
		this.nivelIndentacion = 0;
	}
	
	private void escribirIndentacion() {
		for (int i = 1 ; i <= this.nivelIndentacion ; i++) {
			System.out.print("    ");
		}
	}
	
	// Asociado al evento de comienzo del documento XML.
	public void startDocument()
	throws SAXException {
		System.out.println("Comienzo del Documento XML");
	}
	
	// Asociado al evento de fin del documento XML.
	public void endDocument()
	throws SAXException {
		System.out.println("Fin del Documento XML");
	}
	
	// Asociado al evento de comienzo de un elemento.
	public void startElement(String uri, String nombreLocal, String nombreCualificado, Attributes atributos) 
	throws SAXException {
		this.escribirIndentacion();
		System.out.printf("Comienzo de Elemento: %s\n", nombreLocal);
		this.nivelIndentacion++;
		if (atributos.getLength() > 0) {
			this.escribirIndentacion();
			System.out.print("Atributos de Elemento: ");
			for (int i = 0 ; i < atributos.getLength() ; i++) {
				System.out.print(atributos.getQName(i) + "=" + atributos.getValue(i) + " ");
			}
			System.out.println();
		}
	}
	
	// Asociado al evento de fin de un elemento.
	public void endElement(String uri, String nombreLocal, String nombreCualificado)
	throws SAXException {
		this.nivelIndentacion--;
		this.escribirIndentacion();
		System.out.printf("Fin de Elemento: %s\n", nombreLocal);
	}
	
	// Asociado al evento de una cadena de caracteres.
	public void characters(char[] vectorCaracteres, int inicio, int longitud) 
	throws SAXException {
		String cadena = new String(vectorCaracteres, inicio, longitud);
		cadena = cadena.replaceAll("[\t\n]", "");
		cadena = cadena.trim();
		this.escribirIndentacion();
		System.out.printf("Caracteres: %s\n", cadena);		
	}	

}
