package configuration;
// TODO: Auto-generated Javadoc

/**
 * The Class Consts.
 */

/**
 * @author HellwigP
 * Collected constants of general utility.
 */

public final class Consts {
	
	/** The Constant statusCol. */
	public static final String sqlmodus = "sql";
	public static final String xlsmodus = "xls";
	
	/** The Constant IDcol. */
	public static final String IDcol = "Externe_ID";
	
	public static final String BEZEICHNERcol = "Bezeichner";
	
	public static final String FRAGEcol = "Fragestellung";
	
	public static final String TYPcol = "Indikatortyp";
	
	public static final String RICHTUNGcol = "Richtung";
	
	public static final String RICHTUNGdesk = "deskriptiv";
	
	public static final String EINHEITcol = "Einheit";
	
	/** The Constant IDZaehlerCol. */
	public static final String IDZaehlerCol = "Zaehler_ID";
	
	/** The Constant IDZaehlerTeilerCol. */
	public static final String IDZaehlerTeilerCol = "Zaehler_Teiler";
	
	/** The Constant IDNennerCol. */
	public static final String IDNennerCol = "Nenner_ID";
	
	/** The Constant IDNennerTeilerCol. */
	public static final String IDNennerTeilerCol = "Nenner_Teiler";
	
	/** The Constant konstantePrefix. */
	public static final String konstantePrefix = "ISK_";
	
	/** The Constant VIsheetNum. */
	public static final int VIsheetNum = 0;
	
	/** The Constant statusCol. */
	public static final String statusCol = "Status";
	
	/** The Constant statusUsed. */
	public static final String[] statusUsed = {"Validierung","Freigegeben","Nur Ident"};
	
	public static final float noValue = -100000;
		
	 /**
 	 * Instantiates a new consts.
 	 */
 	private Consts(){
		    //this prevents even the native class from 
		    //calling this ctor as well :
		    throw new AssertionError();
	 }
}