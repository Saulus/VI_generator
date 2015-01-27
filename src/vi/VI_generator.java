package vi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.xmappr.Xmappr;

import configuration.Consts;
import configuration.Konfiguration;
import configuration.VIset;

import static org.kohsuke.args4j.ExampleMode.ALL;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


/**
 * The Class VI_generator.
 */
public class VI_generator {
	
	 /** The configfile. */
 	@Option(name="-c",usage="Konfigurationsdatei, z.B. konfiguration.xml",metaVar="path/to/file.xml")
	 private File configfile;
	 
	 /** The viset. */
 	@Option(name="-vi",usage="VI Set für das ein Excel erstellt werden soll; Sets entsprechend konfiguration.xml",metaVar="Diabetes") 
	 private String viset = "";
 	
 	/** The Modus. */
 	@Option(name="-modus",usage="Modus: sql = Erstelle Zwischen-SQL aus VISet Vorlagen oder xls = Erstelle finales Excel aus Template und input-csv",metaVar="sql") 
	 private String modus = "";
	 
	 /** The outputtable. */
 	@Option(name="-sqlo",usage="SQL: Outputtable: Name der erstellten Zieltabelle in SQL") 
	 private String outputtable = "vi_output";
	 
	 /** The years. */
 	@Option(name="-sqly",usage="SQL: Jahre, ggfs. getrennt durch Kommata, z.B. 2012,2013") 
	 private String years = "2012";
	 
	 /** The pidtable. */
 	@Option(name="-sqlpid",usage="SQL: PID-Tabelle, mit den PIDs auf die einzuschränken ist, für das SQL; OPTIONAL") 
	 private String pidtable = "";

 	
 	 /** The igcsv. */
 	@Option(name="-xlsIG",usage="XLS: Input-csv der IG-Gruppe in der Form Bezugsjahr, Spalte2=Indikator, Spalte3=Indikator",metaVar="path/to/file.csv") 
	 private File igcsv;
	 
	 /** The ivcsv. */
 	@Option(name="-xlsVG",usage="XLS-OPTIONAL; Input-csv der Vergleichsgruppe in der Form Bezugsjahr, Spalte2=Indikator, Spalte3=Indikator",metaVar="path/to/file.csv") 
	 private File ivcsv;
	 
	 /** The out. */
 	@Option(name="-xlso",usage="XLS: Outputdatei mit Pfad",metaVar="path/to/file.xlsx") 
	 private String outfile = "output";
 	
 	private boolean sqlmodus = false;
 	
 	 /** The has compare group. */
 	private boolean hasCompareGroup = false;


	 

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		new VI_generator().doMain(args);
	}
		
	/**
	 * Do main.
	 *
	 * @param args the args
	 */
	public void doMain(String[] args) {
		// 0. Get Configs
		 CmdLineParser parser = new CmdLineParser(this);
		 
		 try {
			// parse the arguments.
			parser.parseArgument(args);
			if(configfile == null || !configfile.isFile() || !configfile.canRead())
				throw new CmdLineException(parser,"Kann konfigurationsfile nicht lesen!");			
			if(modus.toLowerCase().equals(Consts.sqlmodus)) this.sqlmodus = true;
			else if(modus.toLowerCase().equals(Consts.xlsmodus)) this.sqlmodus = false;
			else throw new CmdLineException(parser,"Bitte Modus angeben!");
			
			if (this.sqlmodus) {
				if (viset.isEmpty() || outputtable.isEmpty())
					throw new CmdLineException(parser,"Argumente nicht vollständig");
			} else {
				if (igcsv == null || !igcsv.isFile() || !igcsv.canRead()) 
					throw new CmdLineException(parser,"Kann input-csv der Gruppe nicht lesen!");
				if (ivcsv != null && (!ivcsv.isFile() || !ivcsv.canRead()))
					throw new CmdLineException(parser,"Kann input-csv der Vergleichsgruppe nicht lesen!");
				hasCompareGroup = ivcsv != null;
				if (viset.isEmpty() || outfile.isEmpty())
					throw new CmdLineException(parser,"Argumente nicht vollständig");
			}
		 } catch( CmdLineException e ) {
				System.err.println(e.getMessage());
				System.err.println("java SampleMain [options...] arguments...");
				parser.printUsage(System.err);
				System.err.println();
				// print option sample. This is useful some time
				System.err.println(" Beispiel: java SampleMain"+parser.printExample(ALL));
				System.exit(1);
			}
		Konfiguration config = null;
		try {
			FileReader reader = new FileReader(configfile);
			Xmappr xm = new Xmappr(Konfiguration.class);
			config = (Konfiguration) xm.fromXML(reader);
			reader.close();
		} catch (Exception e) {
			System.err.println("Fehler: Die Konfigurationsdatei "+ configfile.getName()  + " konnte nicht eingelesen werden.");
			System.exit(1);
		}
		
		VIset myset = config.getSetByName(viset);
		VIset defaultsset = config.getDefaultSet(); 
		
		//1. read in Excelfile = Documentation for indikator
		Excelfile efile = null;
		try {
			efile=new Excelfile(myset.excelfile);
		} catch (Exception e) {
			System.err.println("Fehler bei Öffnen des Sets "+ myset.name + " und File: "+ myset.excelfile);
			e.printStackTrace();
			System.exit(1);
		}
		
		//2. read in all indikatoren
		Indikatoren indikatoren = new Indikatoren(efile);
		
		//3. Now start working
		//either SQL
		if (this.sqlmodus) {
			SQLbuilder builder = new SQLbuilder(myset,defaultsset,pidtable,years,outputtable,config.intermediate_view);
			String query = builder.build(indikatoren);
			//Print new query in command line
			System.out.println(query);
		} else {
		//or XLS
			//read in all input-csvs
			InputFile in_file;
			ArrayList<Integer> years = null;
			try {
				in_file = new InputFile(igcsv, indikatoren, true);
				years = in_file.getYears();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
			if (hasCompareGroup) {
				try {
					in_file = new InputFile(igcsv, indikatoren, false);
				} catch (Exception e) {
					System.err.println(e.getMessage());
					System.exit(1);
				}
			}
			
			//open excel template file
			Workbook template_wb = null;
			try {
				template_wb = WorkbookFactory.create(new FileInputStream(config.template.excelfile));
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
			
			XLSbuilder builder = new XLSbuilder(template_wb, config);
			Workbook newwb = builder.build(myset.name,years,indikatoren);
			
			//Schreibe neues Workbook
			FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream(outfile);
				newwb.write(fileOut);
				fileOut.close();
			} catch (IOException e) {
				System.err.println("Fehler: Die neue Excel-Datei "+ outfile  + " konnte nicht geschrieben werden.");
				System.exit(1);
			}
			System.out.println("Die Excel-Datei "+ outfile  + " wurde erfolgreich erstellt.");
		}	
	}

}
