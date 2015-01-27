package vi;

import configuration.Consts;

// TODO: Auto-generated Javadoc
/**
 * The Class Indikator.
 */
public class Indikator {
	
	/** The id. */
	private String id;
	
	/** The zaehler. */
	private String zaehler;
	
	/** The zaehler teiler. */
	private String zaehlerTeiler;
	
	/** The nenner. */
	private String nenner;
	
	/** The nenner teiler. */
	private String nennerTeiler;

	/**
	 * Instantiates a new indikator.
	 *
	 * @param id the id
	 * @param idZaehler the id zaehler
	 * @param idZaehlerTeiler the id zaehler teiler
	 * @param idNenner the id nenner
	 * @param idNennerTeiler the id nenner teiler
	 */
	public Indikator(String id, String idZaehler,String idZaehlerTeiler,String idNenner,String idNennerTeiler) {
		this.id=id.replaceAll("\\s",""); //removes any whitespace from column names!!! 
		this.zaehler=idZaehler;
		this.zaehlerTeiler=idZaehlerTeiler;
		this.nenner=idNenner;
		this.nennerTeiler=idNennerTeiler;
		if (zaehler.startsWith(Consts.konstantePrefix)) zaehler = zaehler.substring(Consts.konstantePrefix.length()); 
		if (zaehlerTeiler.startsWith(Consts.konstantePrefix)) zaehlerTeiler = zaehlerTeiler.substring(Consts.konstantePrefix.length());
		if (nenner.startsWith(Consts.konstantePrefix)) nenner = nenner.substring(Consts.konstantePrefix.length()); 
		if (nennerTeiler.startsWith(Consts.konstantePrefix)) nennerTeiler = nennerTeiler.substring(Consts.konstantePrefix.length());
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
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
	

}
