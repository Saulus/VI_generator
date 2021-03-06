package vi;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import configuration.VIset;


// TODO: Auto-generated Javadoc
/**
 * The Class SQLbuilder.
 */
public class SQLbuilder {
	
	/** The kgtable. */
	private String kgtable;
	
	/** The has default. */
	private boolean hasDefault = false;
	
	/** The kgdefault. */
	private String kgdefault;
	
	/** The has pidtable. */
	private boolean hasPidtable = false;
	
	/** The pidtable. */
	private String pidtable;
	
	/** The has years. */
	private boolean hasYears = false;
	
	/** The years. */
	private String years; //including parenthesis (see constructor)
	
	/** The outputtable. */
	private String outputtable;
	
	/** The view. */
	private String view;
	
	
	/**
	 * Instantiates a new SQlbuilder.
	 *
	 * @param set the set
	 * @param defaultset the defaultset
	 * @param pidtable the pidtable
	 * @param years the years
	 * @param outputtable the outputtable
	 * @param view the view
	 * @throws InvalidFormatException the invalid format exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public SQLbuilder(VIset set, VIset defaultset, String pidtable,String years, String outputtable, String view) {
		this.kgtable=set.kgtable;
		if (defaultset != null) {
			this.kgdefault=defaultset.kgtable;
			this.hasDefault=true;
		}
		if (!pidtable.isEmpty()) {
			this.pidtable=pidtable;
			this.hasPidtable=true;
		}
		if (!years.isEmpty()) {
			if (years.startsWith("(")) this.years=years;
			else  this.years="("+years+")";
			this.hasYears=true;
		}
		this.outputtable=outputtable;
		this.view = view;
	}
	
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	
	/**
	 * Builds the.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String build(Indikatoren indikatoren) {
		//1. create view statement to combine set-kg and default-kg, and reduce to PIDs 
		String limit = "";
		if (hasPidtable || hasYears) {
			String pidlimit = "PID in (select PID from " + pidtable +")";
			String yearlimit = "Bezugsjahr in " + years;
			if (hasPidtable && hasYears) limit = " where " + pidlimit + " and " + yearlimit;
			else if (hasPidtable) limit = " where " + pidlimit;
			else if (hasYears) limit = " where " + yearlimit;
		}
		String statement1 = "create view "+ view + " as select * from (" +
				"(select * from " + kgtable + limit + ") as t1";
		if (hasDefault) statement1 += " INNER JOIN (select * from " + kgdefault + limit + ") as t2 ON t1.pid=t2.pid and t1.Bezugsjahr=t2.Bezugsjahr);    ";
		else statement1 += ");"; 
		
		//2. iterate over indicators
		String statement2="create table " + outputtable + " as select ";
		if(hasYears) statement2 += "Bezugsjahr, ";
		boolean hasIn = false;
		for (Indikator in : indikatoren.getAllIndikators()) {
				String zaehler = in.getZaehler();
				if (!isNumeric(zaehler)) zaehler = "sum(" + zaehler + ")";
				String zaehlerteiler = in.getZaehlerTeiler();
				if (!isNumeric(zaehlerteiler)) zaehlerteiler = "sum(" + zaehlerteiler + ")";
				String nenner = in.getNenner();
				if (!isNumeric(nenner)) nenner = "sum(" + nenner + ")";
				String nennerteiler = in.getNennerTeiler();
				if (!isNumeric(nennerteiler)) nennerteiler = "sum(" + nennerteiler + ")";
				statement2 += "("+zaehler+"/"+zaehlerteiler+")/("+nenner+"/"+nennerteiler+") as " +
						in.getId()
						+ ", ";
				hasIn = true;
		}
		if (!hasIn) statement2 += "0 as indikator  "; 
		statement2 = statement2.substring(0, statement2.length()-2) + " from " + view;
		if(hasYears) statement2 += " group by Bezugsjahr"; 
		statement2 += ";";
		return statement1 + statement2;
	}

}
