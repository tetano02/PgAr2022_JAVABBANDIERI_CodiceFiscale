package it.unibs.fp.codiceFiscale;

public class CodiceFiscale {
	private String nome;
	private String cognome;
	private String anno;
	private String mese;
	private String giorno;
	private String comune;
	private char letteraFinale;
	private String codiceIntero;
	
	public CodiceFiscale(String str) {
		this.codiceIntero=str;
	}
	
	public String getCognome() {
		return cognome;
	}
	public void setCognome() {
		this.cognome=codiceIntero.substring(0,3);
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome() {
		this.nome=this.codiceIntero.substring(3,6);
	}
	public String getAnno() {
		return anno;
	}
	public void setAnno() {
		this.anno=this.codiceIntero.substring(6,8);
	}
	public String getMese() {
		return mese;
	}
	public void setMese() {
		this.mese=this.codiceIntero.substring(8,9);
	}
	public String getGiorno() {
		return giorno;
	}
	public void setGiorno() {
		this.giorno=this.codiceIntero.substring(9,11);
	}
	public String getComune() {
		return comune;
	}
	public void setComune() {
		this.comune=this.codiceIntero.substring(11,15);
	}
	public char getLetteraFinale() {
		return letteraFinale;
	}
	public void setLetteraFinale() {
		this.letteraFinale = this.codiceIntero.charAt(15);
	}
	public String getCodiceIntero() {
		return codiceIntero;
	}
	public void setCodiceIntero(String codiceIntero) {
		this.codiceIntero = codiceIntero;
	}
	
	public boolean isValido() {
		if(controllaLunghezza()) //Controlla che la lunghezza sia corretta
			return false;
		if(!controllaCaratteriPosizione())
			return false;
		if(!controllaGiornoMese())
			return false;
		if(!controllaUltimaLettera())
			return false;
		return true;
	}
	
	private boolean controllaLunghezza() {
		if(this.codiceIntero.length()!=16)
			return true;
		//Le parti del codice vengono inizializzate solo se la lunghezza è corretta, altrimenti a prescindere è sbagliato il codice fiscale
		this.setNome();
		this.setCognome();
		this.setAnno();
		this.setMese();
		this.setGiorno();
		this.setComune();
		this.setLetteraFinale();
		return false;
			
	}

	private boolean controllaCaratteriPosizione() {
		for(int i=0; i<this.codiceIntero.length(); i++) {
			if( i==6 || i==7 || i==9 || i==10 || i==12 || i==13 || i==14) {
				if(!Character.isDigit(this.codiceIntero.charAt(i))) 
					return false;
			}else {
				if(!Character.isAlphabetic(this.codiceIntero.charAt(i)))
					return false;
			}
		}
		return true;
	}
	
	private boolean controllaGiornoMese() {
		int giorno=Integer.parseInt(this.giorno);
		if(this.mese.equals("B")) { //Controlla se è febbraio
			if((giorno>=1 && giorno<=28) || (giorno>=41 && giorno<=68)) //Controlla i giorni ammissibili
				return true;
			return false;
		}else if(this.mese.equals("S") || this.mese.equals("D") || this.mese.equals("H") || this.mese.equals("P")) { //Controlla se è un mese da 30 giorni
			if((giorno>=1 && giorno<=30) || (giorno>=41 && giorno<=70)) //Controlla i giorni ammissibili
				return true;
			return false;
		}else if(this.mese.equals("A") || this.mese.equals("C") || this.mese.equals("E") || this.mese.equals("L") || this.mese.equals("M") || this.mese.equals("R") || this.mese.equals("T")){ //Controlla se è un mese da 31 giorni
			if((giorno>=1 && giorno<=31) || (giorno>=41 && giorno<=71)) //Controlla i giorni ammissibili
				return true;
			return false;
		}
		//Se non entra in nessun if vuol dire che il mese inserito non è corretto
		return false;
		}
	
	private boolean controllaUltimaLettera() {
		if(this.letteraFinale==MetodoLetteraFinale.generaLetteraFinale(this.codiceIntero.substring(0,15)))
			return true;
		return false;
	}
	}
