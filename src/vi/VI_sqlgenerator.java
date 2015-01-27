package vi;
import java.io.File;
import java.io.FileReader;
import org.xmappr.Xmappr;

import configuration.Konfiguration;
import configuration.VIset;

import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


// TODO: Auto-generated Javadoc
/**
 * The Class VI_sqlgenerator.
 */
public class VI_sqlgenerator {
	
	 /** The configfile. */
 	@Option(name="-c",usage="Konfigurationsdatei, z.B. konfiguration.xml",metaVar="path/to/file.xml")
	 private File configfile;
	 
	 /** The viset. */
 	@Option(name="-vi",usage="VI Set für das SQL erstellt werden soll; Sets entsprechend konfiguration.xml") 
	 private String viset = "";
	 
	 /** The outputtable. */
 	@Option(name="-o",usage="Name der erstellten Zieltabelle in SQL") 
	 private String outputtable = "(vi_output)";
	 
	 /** The years. */
 	@Option(name="-y",usage="Jahre, ggfs. getrennt durch Kommata, z.B. 2012,2013") 
	 private String years = "2012";
	 
	 /** The pidtable. */
 	@Option(name="-pid",usage="PID-Tabelle, mit den PIDs auf die einzuschränken ist, für das SQL; OPTIONAL") 
	 private String pidtable = "";
	 

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		new VI_sqlgenerator().doMain(args);
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
			if (viset.isEmpty() || outputtable.isEmpty())
				throw new CmdLineException(parser,"Argumente nicht vollständig");
		} catch( CmdLineException e ) {
			System.err.println(e.getMessage());
			System.err.println("java SampleMain [options...] arguments...");
			parser.printUsage(System.err);
			System.err.println();
			// print option sample. This is useful some time
			System.err.println(" Example: java SampleMain"+parser.printExample(ALL));
			System.exit(1);
		}
		Konfiguration config = null;
		try {
			FileReader reader = new FileReader(configfile);
			Xmappr xm = new Xmappr(Konfiguration.class);
			config = (Konfiguration) xm.fromXML(reader);
			reader.close();
		} catch (Exception e) {
			System.out.println("Fehler: Die Konfigurationsdatei "+ configfile.getName() + " konnte nicht eingelesen werden.");
			e.printStackTrace();
			System.exit(1);
		}
		VIset myset = config.getSetByName(viset);
		VIset defaultsset = config.getDefaultSet(); 
		SQLbuilder builder = null;
		try {
			builder = new SQLbuilder(myset,defaultsset,pidtable,years,outputtable,config.getView());
		} catch (Exception e) {
				System.out.println("Fehler bei Öffnen des Sets "+ myset.getName() + " und File: "+ myset.getExcelfile());
				e.printStackTrace();
				System.exit(1);
		}
		String query = "";
		try {
			query = builder.build();
		} catch (Exception e) {
			System.out.println("Fehler bei Bearbeiten des Sets "+ myset.getName() + " und File: "+ myset.getExcelfile());
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println(query);
	}

}
