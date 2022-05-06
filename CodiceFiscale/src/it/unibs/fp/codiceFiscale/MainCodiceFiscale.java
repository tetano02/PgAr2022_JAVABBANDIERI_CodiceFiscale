package it.unibs.fp.codiceFiscale;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class MainCodiceFiscale {

	public static void main(String[] args) throws XMLStreamException {
		//Lettura dei file xml utili 
		ArrayList<Persona> elencoPersone=new ArrayList<Persona>(); //ArrayList contenente l'elenco di persone
		XMLInputFactory xmlif = null; 
		XMLStreamReader xmlip = null; //Legge il file xml contenente gli input persone
		String element=null; //Contiene il tag in cui il testo CHARACTER è contenuto
		try {
		xmlif = XMLInputFactory.newInstance();
		xmlip = xmlif.createXMLStreamReader("inputPersone.xml", new FileInputStream("inputPersone.xml"));
		} catch (Exception e) {
		System.out.println("Impossibile leggere tutti i file");
		System.out.println(e.getMessage());
		}
		//Vengono immagazzinati i dati del file inputPersone.xml all'interno di oggetti di tipo Persona
		while (xmlip.hasNext()) { // continua a leggere finché ha eventi a disposizione
			switch (xmlip.getEventType()) { // switch sul tipo di evento
			case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
			if(xmlip.getLocalName().equals("persona"))
				elencoPersone.add(new Persona()); //Inizializza una persona 
			element=xmlip.getLocalName();
			break;
			case XMLStreamConstants.COMMENT:
			System.out.println("// commento " + xmlip.getText());
			break; // commento: ne stampa il contenuto
			case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
				if (xmlip.getText().trim().length() > 0) { //Aiuta a non inserire testo contenente solo spazio
					switch(element){
					case "nome":
						elencoPersone.get(elencoPersone.size()-1).setNome(xmlip.getText());
					break;
					case "cognome":
						elencoPersone.get(elencoPersone.size()-1).setCognome(xmlip.getText());
						break; 
					case "data_nascita":
					elencoPersone.get(elencoPersone.size()-1).setData(xmlip.getText());
					break;
					case "sesso":
						elencoPersone.get(elencoPersone.size()-1).setSesso(xmlip.getText());
				break;
				case "comune_nascita":
						elencoPersone.get(elencoPersone.size()-1).setComune(xmlip.getText());
				break;
				default:
				break;
				}
			}
			break;
			default:
				break;
			}
			xmlip.next();
			}
		//Setta il codice fiscale di ogni persona
		
		ArrayList<String> codiciPresenti=new ArrayList<String>();
		for(int i=0; i<elencoPersone.size(); i++) {
			elencoPersone.get(i).setCodiceFiscale();
			}
		//Salvataggio di tutti i codici fiscali in un ArrayList
		ArrayList<String> elencoCodiciFiscali=new ArrayList<String>();
		XMLStreamReader xmlcf = null; //Legge il file xml contenente i codici fiscali
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlcf = xmlif.createXMLStreamReader("codiciFiscali.xml", new FileInputStream("codiciFiscali.xml"));
			} catch (Exception e) {
			System.out.println("Impossibile leggere il file dei codici fiscali");
			System.out.println(e.getMessage());
			}
		while(xmlcf.hasNext()) {
			switch(xmlcf.getEventType()) {
			case XMLStreamConstants.CHARACTERS:
				if (xmlcf.getText().trim().length() > 0) 
					elencoCodiciFiscali.add(xmlcf.getText());
			}
			xmlcf.next();
		}
		//Scrittura del file xml
		XMLOutputFactory xmlof = null;
		XMLStreamWriter xmlout = null;
		try {
		xmlof = XMLOutputFactory.newInstance();
		xmlout = xmlof.createXMLStreamWriter(new FileOutputStream("codiciPersone.xml"),"utf-8");
		xmlout.writeStartDocument("utf-8", "1.0");
		} catch (Exception e) {
		System.out.println("Errore nell'inizializzazione del writer:");
		System.out.println(e.getMessage());
		}
		xmlout.writeStartElement("output");//Apre <output>
		xmlout.writeStartElement("persone");//Apre <persone>
		xmlout.writeAttribute("persone", "numero="+elencoPersone.size());
		for(int i=0; i<elencoPersone.size(); i++) {
			xmlout.writeStartElement("persona");
			xmlout.writeAttribute("persona", "id="+i);
			xmlout.writeStartElement("nome");
			xmlout.writeCharacters(elencoPersone.get(i).getNome());
			xmlout.writeEndElement();
			xmlout.writeStartElement("cognome");
			xmlout.writeCharacters(elencoPersone.get(i).getCognome());
			xmlout.writeEndElement();
			xmlout.writeStartElement("sesso");
			xmlout.writeCharacters(elencoPersone.get(i).getSesso());
			xmlout.writeEndElement();
			xmlout.writeStartElement("comune_di_nascita");
			xmlout.writeCharacters(elencoPersone.get(i).getComune());
			xmlout.writeEndElement();
			xmlout.writeStartElement("data_di_nascita");
			xmlout.writeCharacters(elencoPersone.get(i).getData());
			xmlout.writeEndElement();
			xmlout.writeStartElement("codice_fiscale");
			if(elencoPersone.get(i).isCodicePresente()) {
				xmlout.writeCharacters(elencoPersone.get(i).getCodiceFiscale());
				codiciPresenti.add(elencoPersone.get(i).getCodiceFiscale()); //Inseriamo quello calcolato da noi per semplicità, tanto è identico a quello nel file
			}else
				xmlout.writeCharacters("ASSENTE");
			xmlout.writeEndElement();
			xmlout.writeEndElement();
		}
		xmlout.writeEndElement(); //Chiude </persone>
		xmlout.writeStartElement("codici"); //Apre <codici>
		xmlout.writeStartElement("invalidi");
		for(int i=0; i<elencoCodiciFiscali.size(); i++) {
			CodiceFiscale codiceAttuale= new CodiceFiscale(elencoCodiciFiscali.get(i));
			if(!codiceAttuale.isValido()) {
				xmlout.writeStartElement("codice");
				xmlout.writeCharacters(elencoCodiciFiscali.get(i));
				xmlout.writeEndElement();
				elencoCodiciFiscali.remove(i); //Viene rimosso dall'arrayList così da non venire ripetuto nella stampa degli spaiati
				i--; //i viene decrementato così da evitare di saltare una posizione di controllo
			}
		}
		xmlout.writeEndElement();
		xmlout.writeStartElement("spaiati"); //Apre <spaiati>
		boolean trovato;
		for(int i=0; i<elencoCodiciFiscali.size(); i++) {
			trovato=false;
			for(int j=0; j<codiciPresenti.size(); j++) {
				if(elencoCodiciFiscali.get(i).equals(codiciPresenti.get(j)))
					trovato=true;
			}
			if(trovato==false) {
				xmlout.writeStartElement("codice");
				xmlout.writeCharacters(elencoCodiciFiscali.get(i));
				xmlout.writeEndElement();
			}
		}
		xmlout.writeEndElement(); 
		xmlout.writeEndElement(); //Chiude </codici>
		xmlout.writeEndElement(); //Chiude </output>
		xmlout.close();
		}
}
