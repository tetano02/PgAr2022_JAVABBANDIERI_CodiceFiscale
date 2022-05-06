package it.unibs.fp.codiceFiscale;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Persona {
	
	private static final int SECONDO_VALORE = 2;
	private static final int MASSIMA_LUNGHEZZA_STRINGA = 3;
	
	private String nome;
	private String cognome;
	private String sesso;
	private String data;
	private String comune;
	private String codiceFiscale;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public void setCodiceFiscale() throws XMLStreamException {
		StringBuffer codiceAttuale=new StringBuffer();
		codiceAttuale.append(generaCognome(this.cognome));
		codiceAttuale.append(generaNome(this.nome));
		codiceAttuale.append(generaData(this.data));
		codiceAttuale.append(generaComune(this.comune));
		codiceAttuale.append(MetodoLetteraFinale.generaLetteraFinale(codiceAttuale.toString()));
		this.codiceFiscale=codiceAttuale.toString();
	}
	
	private String generaCognome(String cognome) {
		int conta=0;
		StringBuffer cognomeCodice=new StringBuffer();
		for(int i=0; i<cognome.length(); i++) {
			if(!isVocale(cognome.charAt(i)) && cognomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA)
				cognomeCodice.append(cognome.charAt(i));
		}
		while(cognomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA && conta!=cognome.length()) {
				if(isVocale(cognome.charAt(conta)))
					cognomeCodice.append(cognome.charAt(conta));
				conta++;
		}
		while(cognomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA) {
			cognomeCodice.append("X");
		}
		return cognomeCodice.toString().toUpperCase(); //.toUpperCase è stato inserito nel caso in cui il nome sia stato scritto in minuscolo
	}
	
	private String generaNome(String nome) {
		int k=0;
		int contaConsonanti=0;
		StringBuffer nomeCodice=new StringBuffer();
		
		int conta=numeroConsonanti(nome);
		if(conta<=MASSIMA_LUNGHEZZA_STRINGA) {
			for(int i=0; i<nome.length(); i++) {
				if(!isVocale(nome.charAt(i)) && nomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA)
					nomeCodice.append(nome.charAt(i));
			}
			while(nomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA && k!=nome.length()) {
				if(isVocale(nome.charAt(k)))
					nomeCodice.append(nome.charAt(k));
				k++;
			}
			while(nomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA) {
				nomeCodice.append("X");
			}
			return nomeCodice.toString();
		}
		
		for(int i=0; i<nome.length(); i++) {
			if(!isVocale(nome.charAt(i)) && nomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA) {
				contaConsonanti++;
				if(contaConsonanti!=SECONDO_VALORE)
					nomeCodice.append(nome.charAt(i));
			}
		}
		while(nomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA && k!=nome.length()) {
				if(isVocale(nome.charAt(k)))
					nomeCodice.append(nome.charAt(k));
				k++;
		}
		while(nomeCodice.length()<MASSIMA_LUNGHEZZA_STRINGA) {
			nomeCodice.append("X");
		}
		return nomeCodice.toString().toUpperCase(); //.toUpperCase è stato inserito nel caso in cui il nome sia stato scritto in minuscolo
	}
	
	private boolean isVocale(char c) {
		c=Character.toLowerCase(c);
		if(c=='a' || c=='e' || c=='i'|| c=='o'|| c=='u')
			return true;
		return false;
	}
	
	private int numeroConsonanti(String str) {
		int conta=0;
		for(int i=0; i<str.length(); i++) {
			if(!isVocale(str.charAt(i)))
				conta++;
		}
		return conta;
	}
	
	private String generaData(String data) {
		StringBuffer dataBuffer=new StringBuffer();
		dataBuffer.append(data.substring(2,4));
		dataBuffer.append(generaMese(data));
		dataBuffer.append(generaGiorno(data));
		return dataBuffer.toString();
	}
	
	private String generaMese(String str) {
		switch(str.substring(5,7)) {
		case "01":
			return "A";
		case "02":
			return "B";
		case "03":
			return "C";
		case "04":
			return "D";
		case "05":
			return "E";
		case "06":
			return "H";
		case "07":
			return "L";
		case "08":
			return "M";
		case "09":
			return "P";
		case "10":
			return "R";
		case "11":
			return "S";
		case "12":
			return "T";
		}
		return "";
	}
	
	private String generaGiorno(String str) {
		int giorno=Integer.parseInt(str.substring(str.length()-2, str.length()));
		if(this.sesso.equals("F"))
			giorno+=40;
		String strAttuale=Integer.toString(giorno);
		if(strAttuale.length()<SECONDO_VALORE)
			strAttuale="0"+strAttuale;
		if(str.substring(str.length()-5,str.length()-3).equals("02")) 
			if(!((giorno>=1 && giorno<=28) || (giorno>=41 && giorno<=68)))
				return ""; //Return in caso i giorni di febbraio siano più di 28, così il codice sarà assente
		if(str.substring(str.length()-5,str.length()-3).equals("04") || str.substring(str.length()-5,str.length()-3).equals("06") || 
				str.substring(str.length()-5,str.length()-3).equals("09") || str.substring(str.length()-5,str.length()-3).equals("11")) {
			if(!((giorno>=1 && giorno<=30) || (giorno>=41 && giorno<=70)))
				return "";//Return in caso i giorni dei mesi da 30 giorni siano più di 30, così il codice sarà assente
		}else
			if(!((giorno>=1 && giorno<=31) || (giorno>=41 && giorno<=71)))
				return "";//Return in caso i giorni dei mesi da 31 giorni siano più di 31, così il codice sarà assente
		return strAttuale.toString();
	}
	
	private String generaComune(String str) throws XMLStreamException {
		XMLInputFactory xmlif = null; 
		XMLStreamReader xmlcom = null; //Legge il file xml contenente i comuni
		String element=null;
		boolean trovato=false; //Variabile booleana che diventa vera non appena viene trovato il comune di nascita
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlcom = xmlif.createXMLStreamReader("comuni.xml", new FileInputStream("comuni.xml"));
			} catch (Exception e) {
			System.out.println("Impossibile leggere il file dei comuni");
			System.out.println(e.getMessage());
			}
		while (xmlcom.hasNext()) { // continua a leggere finché ha eventi a disposizione
			switch (xmlcom.getEventType()) { // switch sul tipo di evento
			case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
			element=xmlcom.getLocalName();
			break;
			case XMLStreamConstants.CHARACTERS:
			if(element.equals("nome"))
				if(xmlcom.getText().equals(str)) {
					trovato=true;
				}
			if(element.equals("codice") && trovato==true)
				return xmlcom.getText();
			break;
			default:
				break;
			}
		xmlcom.next();
		}
		return "A000"; //return se il comune non viene trovato, il codice A000 non esiste
	}
	
	public boolean isCodicePresente() throws XMLStreamException {
		XMLInputFactory xmlif = null; 
		XMLStreamReader xmlcf = null; //Legge il file xml contenente i codici fiscali
		String element=null;
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlcf = xmlif.createXMLStreamReader("codiciFiscali.xml", new FileInputStream("codiciFiscali.xml"));
			} catch (Exception e) {
			System.out.println("Impossibile leggere il file dei codici fiscali");
			System.out.println(e.getMessage());
			}
		while(xmlcf.hasNext()) {
			switch(xmlcf.getEventType()) {
			case XMLStreamConstants.START_ELEMENT:
				element=xmlcf.getLocalName();
			break;
			case XMLStreamConstants.CHARACTERS:
			if(element.equals("codice")  && xmlcf.getText().equals(this.codiceFiscale))
				return true;
			break;
			default:
				break;
			}
			xmlcf.next();
		}
		return false;
	}
	}
