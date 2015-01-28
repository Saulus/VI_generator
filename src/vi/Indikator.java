package vi;

import java.util.HashMap;

import configuration.Consts;

/**
 * The Class Indikator.
 */
public class Indikator {
	
	//Dokumentationsinfos
	/** The id. */
	private String id;
	
	private String name;
	
	private String bezeichner;
	
	private String fragestellung;
	
	private String indikatortyp;
	
	private String richtung;
	
	private String einheit;
	
	/** The zaehler. */
	private String zaehler;
	
	/** The zaehler teiler. */
	private String zaehlerTeiler;
	
	/** The nenner. */
	private String nenner;
	
	/** The nenner teiler. */
	private String nennerTeiler;
	
	
	//Werte: year/Indikatorvalue
	private HashMap<Integer,Indikatorvalue> value = new HashMap<Integer,Indikatorvalue>();

	/**
	 * Instantiates a new indikator.
	 *
	 * @param id the id
	 * @param idZaehler the id zaehler
	 * @param idZaehlerTeiler the id zaehler teiler
	 * @param idNenner the id nenner
	 * @param idNennerTeiler the id nenner teiler
	 */
	public Indikator(String id, String name, String bezeichner, String fragestellung, String indikatortyp, String richtung, String einheit, String idZaehler,String idZaehlerTeiler,String idNenner,String idNennerTeiler) {
		this.name=name;
		this.id=id.replaceAll("\\s",""); //removes any whitespace from column names!!!
		this.setBezeichner(bezeichner);
		this.setFragestellung(fragestellung);
		this.setIndikatortyp(indikatortyp);
		this.setRichtung(richtung);
		this.setEinheit(einheit);
		this.zaehler=parseFractionpart(idZaehler);
		this.zaehlerTeiler=parseFractionpart(idZaehlerTeiler);
		this.nenner=parseFractionpart(idNenner);
		this.nennerTeiler=parseFractionpart(idNennerTeiler);
		if (zaehler.startsWith(Consts.konstantePrefix)) zaehler = zaehler.substring(Consts.konstantePrefix.length()); 
		if (zaehlerTeiler.startsWith(Consts.konstantePrefix)) zaehlerTeiler = zaehlerTeiler.substring(Consts.konstantePrefix.length());
		if (nenner.startsWith(Consts.konstantePrefix)) nenner = nenner.substring(Consts.konstantePrefix.length()); 
		if (nennerTeiler.startsWith(Consts.konstantePrefix)) nennerTeiler = nennerTeiler.substring(Consts.konstantePrefix.length());
	}
	
	private String parseFractionpart(String str) {
		String ret = str.replaceAll("\\s",""); //removes any whitespace
		return ret;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Gets the zaehler.
	 *
	 * @return the zaehler
	 */
	public String getZaehler() {
		return zaehler;
	}

	/**
	 * Gets the zaehler teiler.
	 *
	 * @return the zaehler teiler
	 */
	public String getZaehlerTeiler() {
		return zaehlerTeiler;
	}

	/**
	 * Gets the nenner.
	 *
	 * @return the nenner
	 */
	public String getNenner() {
		return nenner;
	}

	/**
	 * Gets the nenner teiler.
	 *
	 * @return the nenner teiler
	 */
	public String getNennerTeiler() {
		return nennerTeiler;
	}

	public String getBezeichner() {
		return bezeichner;
	}

	public void setBezeichner(String bezeichner) {
		this.bezeichner = bezeichner;
	}

	public String getFragestellung() {
		return fragestellung;
	}

	public void setFragestellung(String fragestellung) {
		this.fragestellung = fragestellung;
	}

	public String getIndikatortyp() {
		return indikatortyp;
	}

	public void setIndikatortyp(String indikatortyp) {
		this.indikatortyp = indikatortyp;
	}

	public String getRichtung() {
		return richtung;
	}

	public void setRichtung(String richtung) {
		this.richtung = richtung;
	}

	public String getEinheit() {
		return einheit;
	}

	public void setEinheit(String einheit) {
		this.einheit = einheit;
	}

	public float getValueIG(int year) {
		if (value.containsKey(year)) return value.get(year).getValueIG();
		return Consts.noValue;
	}
	
	public float getValueVG(int year) {
		if (value.containsKey(year)) return value.get(year).getValueVG();
		return Consts.noValue;
	}

	public void setValue(int year, float v, boolean isIG) {
		if (value.containsKey(year)) {
			if (isIG) value.get(year).setValueIG(v);
			else value.get(year).setValueVG(v);
		} else {
			Indikatorvalue iv = new Indikatorvalue();
			if (isIG) iv.setValueIG(v);
			else iv.setValueVG(v);
			value.put(year, iv);
		}
	}	

}
