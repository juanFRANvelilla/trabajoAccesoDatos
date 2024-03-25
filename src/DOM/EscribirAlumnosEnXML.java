package DOM;

import objectDB.Alquiler;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class EscribirAlumnosEnXML {

	private static final String URLXML = "src/data/alquileres.xml";

	// Lee todos los alquileres de un fichero binario con acceso aleatorio.
	// Devuelve una lista con los alquileres le�dos del fichero binario.
	public static List<Alquiler> leerAlquileresDeBinario()
	throws IOException {
		RandomAccessFile flujoEntrada = null;
		try {
			List<Alquiler> listaAlquileres = new ArrayList<Alquiler>();
			File fichero = new File("src/data/alquileres_aleat_datos.dat");
			flujoEntrada = new RandomAccessFile(fichero, "r");
			flujoEntrada.seek(0);
			while (flujoEntrada.getFilePointer() < flujoEntrada.length()) {
				int id = flujoEntrada.readInt();

				char[] vectorNombreInquilino = new char[Alquiler.LONGITUD_NOMBRE];
				for (int i = 0; i < vectorNombreInquilino.length; i++) {
					vectorNombreInquilino[i] = flujoEntrada.readChar();
				}
				String nombreInquilino = new String(vectorNombreInquilino);

				char[] vectorDireccionPiso = new char[Alquiler.LONGITUD_NOMBRE];
				for (int i = 0; i < vectorDireccionPiso.length; i++) {
					vectorDireccionPiso[i] = flujoEntrada.readChar();
				}
				String direccionPiso = new String(vectorDireccionPiso);

				double importeAlquiler = flujoEntrada.readDouble();

				int duracionContrato = flujoEntrada.readInt();

				if (id > 0) { // Suponiendo que un id > 0 indica un registro válido.
					Alquiler alquiler = new Alquiler(nombreInquilino, direccionPiso, importeAlquiler, duracionContrato);
					listaAlquileres.add(alquiler);
				}
			}
			return listaAlquileres;
		}
		finally {
			if (flujoEntrada != null) {
				flujoEntrada.close();
			}
		}
	}

	// Crea un nodo con una etiqueta y un texto bajo un nodo padre del documento DOM.
	public static void crearNodo(String etiqueta, String texto, Element padre, Document documento)
	throws DOMException {
		Element nodoElemento = documento.createElement(etiqueta);
		Text nodoTexto = documento.createTextNode(texto);
		padre.appendChild(nodoElemento);
		nodoElemento.appendChild(nodoTexto);
	}

	// Lee todos los alquileres del fichero binario 'alquileres_aleat_datos.dat',
	// escribe estos alquileres en el fichero XML 'alquileres.xml' y
	// escribe en consola el n�mero de alquileres escritos en el fichero XML.


	public static List<Alquiler> leerAlquileresDeXml(){
		List<Alquiler> listaAlquileres = new ArrayList<Alquiler>();
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

					Alquiler alquiler = new Alquiler(id, nombre, direccionPiso, importeAlquiler, duracionContrato);
					listaAlquileres.add(alquiler);
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
		return listaAlquileres;
	}
	public static void main(String args[]) {
		try {
			List<Alquiler> listaAlquileres = leerAlquileresDeXml();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();
			Document documento = implementation.createDocument(null, "alquileres", null);
			documento.setXmlVersion("1.0");

			for (Alquiler alquiler : listaAlquileres) {
				int id = alquiler.getId();
				String nombre = alquiler.getNombreInquilino();
				String direccion = alquiler.getDireccionPiso();
				double importe = alquiler.getImporteAlquiler();
				int duracion = alquiler.getDuracionContrato();
				if (id > 0) {
					Element raiz = documento.createElement("alquiler");
					documento.getDocumentElement().appendChild(raiz);
					crearNodo("id", Integer.toString(id), raiz, documento);
					crearNodo("nombre", nombre.trim(), raiz, documento);
					crearNodo("direccion", direccion.trim(), raiz, documento);
					crearNodo("importe", Double.toString(importe), raiz, documento);
					crearNodo("duracion", Integer.toString(duracion), raiz, documento);
				}
			}

			Source origen = new DOMSource(documento);
			Result destino = new StreamResult(new File("src/data/alquileresEscritosDom.xml"));
			Transformer transformador = TransformerFactory.newInstance().newTransformer();
			transformador.transform(origen, destino);

			System.out.println("Se han escrito " + listaAlquileres.size() +
			                   " alquileres en el fichero XML.");
		} catch (ParserConfigurationException pce) {
			System.out.println("Error de Configuraci�n del Analizador Sint�ctico: " + pce.getMessage());
			pce.printStackTrace();
		}
		catch (DOMException dome) {
			System.out.println("Error de Modelo de Objetos de Documento: " + dome.getMessage());
			dome.printStackTrace();
		}
		catch (TransformerConfigurationException tce) {
			System.out.println("Error de Configuraci�n del Transformador: " + tce.getMessage());
			tce.printStackTrace();
		}
		catch (TransformerException te) {
			System.out.println("Error de Transformaci�n: " + te.getMessage());
			te.printStackTrace();
		}
	}

}
