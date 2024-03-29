/**
	/Text.java
	(C) Giovanni Capuano 2011
*/

/* Rappresenta un appunto. */
public class Text {
	private int giorno, mese, anno, ora, minuto, importanza;
	private String appunto, categoria, titolo;
	
	public Text(int giorno, int mese, int anno, int ora, int minuto, int importanza, String appunto, String categoria, String titolo) {
		this.giorno = giorno;
		this.mese = mese;
		this.anno = anno;
		this.ora = ora;
		this.minuto = minuto;
		this.importanza = importanza;
		this.appunto = appunto;
		this.categoria = categoria;
		this.titolo = titolo;
	}
	
	public int getGiorno() {
		return giorno;
	}
	
	public int getMese() {
		return mese;
	}
	
	public int getAnno() {
		return anno;
	}
	
	public int getOra() {
		return ora;
	}
	
	public int getMinuto() {
		return minuto;
	}
	
	public int getImportanza() {
		return importanza;
	}
	
	public String getAppunto() {
		return appunto;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public String getTitolo() {
		return titolo;
	}
	
	public void setAppunto(String appunto) {
		this.appunto = appunto;
	}
	
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
}
