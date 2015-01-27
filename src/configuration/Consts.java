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
	
	/** The Constant IDcol. */
	public static final String IDcol = "Externe_ID";
	
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
		
	 /**
 	 * Instantiates a new consts.
 	 */
 	private Consts(){
		    //this prevents even the native class from 
		    //calling this ctor as well :
		    throw new AssertionError();
	 }
}