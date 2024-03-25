package sax;

import java.util.ArrayList;
import java.util.List;

import objectDB.Alquiler;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

class MyContentManager2 extends DefaultHandler {

	// lista de alquileres
	private List<Alquiler> listaAlquilers;

	private String texto;
	private Alquiler alquiler;

	// Constructor
	public MyContentManager2() {
		super();
		listaAlquilers = new ArrayList<Alquiler>();
	}

	// Asociado al evento de comienzo del documento XML.
	public void startDocument()
	throws SAXException {
	}

	// Asociado al evento de fin del documento XML.
	public void endDocument()
	throws SAXException {
	}

	// Asociado al evento de comienzo de un elemento.
	public void startElement(String uri, String nombreLocal, String nombreCualificado, Attributes atributos)
	throws SAXException {
		if (nombreLocal.equals("alquiler")) {
			alquiler = new Alquiler();
			for (int i = 0 ; i < atributos.getLength() ; i++) {
				alquiler.insertar(atributos.getQName(i), atributos.getValue(i));
			}
		}
	}

	// Asociado al evento de fin de un elemento.
	public void endElement(String uri, String nombreLocal, String nombreCualificado)
	throws SAXException {
		if (nombreLocal.equals("alquiler")) {
			listaAlquilers.add(alquiler);
		}
		else if (nombreLocal.equals("id")) {
			alquiler.setId(Integer.parseInt(texto));
		}
		else if (nombreLocal.equals("nombreInquilino")) {
			alquiler.setNombreInquilino(texto);
		}
		else if (nombreLocal.equals("direccionPiso")) {
			alquiler.setDireccionPiso(texto);
		}
		else if (nombreLocal.equals("importeAlquiler")) {
			alquiler.setImporteAlquiler(Double.parseDouble(texto));
		}
		else if (nombreLocal.equals("duracionContrato")) {
			alquiler.setDuracionContrato(Integer.parseInt(texto));
		}
	}

	// Asociado al evento de una cadena de caracteres.
	public void characters(char[] vectorCaracteres, int inicio, int longitud)
	throws SAXException {
		String cadena = new String(vectorCaracteres, inicio, longitud);
		cadena = cadena.replaceAll("[\t\n]", "");
		cadena = cadena.trim();
		texto = cadena;
	}

	public List<Alquiler> getListaAlquilers() {
		return listaAlquilers;
	}

}
