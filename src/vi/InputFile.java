package vi;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;


// TODO: Auto-generated Javadoc
/**
 * The Class InputFile.
 */
public class InputFile {
	
	/** The years. */
	private ArrayList<Integer> years = new ArrayList<Integer>();
	
	
	/**
	 * Instantiates a new input file.
	 *
	 * @param inputcsv the inputcsv
	 * @throws Exception the exception
	 */
	public InputFile (File inputcsv, Indikatoren indikatoren, boolean isIG) throws Exception {
		CSVReader reader = new CSVReader(new FileReader(inputcsv), ';', '"');
		
		String [] colnames;
		if ((colnames = reader.readNext()) == null || !colnames[0].equalsIgnoreCase("Bezugsjahr")) {
			reader.close();
			throw new Exception("Input-CSV nicht in richtigem Format (Spalte1=Bezugsjahr)");
		}
		String[] nextline;
		while ((nextline = reader.readNext()) != null && !nextline[0].isEmpty()) {
			int year = Integer.valueOf(nextline[0]);
			for(int i=1; i<colnames.length; i++) {
				try {
					Float value = Float.valueOf(nextline[i].replace(',', '.'));
					indikatoren.addValue(colnames[i],year,value, isIG);
				} catch (NumberFormatException nfe) {
					
				}
			}
			years.add(year);
		}
		reader.close();
		//make Uppercase
	}
	
	/**
	 * Gets the years.
	 *
	 * @return the years
	 */
	public ArrayList<Integer> getYears() {
		return years;
	}
	

}
