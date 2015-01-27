package configuration;

import org.xmappr.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class Set.
 * Elements from konfiguration.xml are loaded into here, into variables defined acc. to xml tag.
 * Class for Tags konfiguration->sets->set
 */
public class VIset {
	
	/** The name. */
	@Element
	public String name; 
	
	/** The excelfile-pfad. */
	@Element
	public String excelfile;
	
	/** The kgtable. */
	@Element
	public String kgtable;
	
	/** The isdefault. */
	@Element(defaultValue="false")
	public String isdefault;
	
	/**
	 * Checks if is default (=needed for all Sets).
	 *
	 * @return true, if is sorted
	 */
	public boolean isDefault() {
		return isdefault.toLowerCase().equals("true");
	}

}
