package configuration;

import org.xmappr.Element;
import org.xmappr.RootElement;


/**
 * The Class Konfiguration.
 * Elements from konfiguration.xml are loaded into here, into variables defined acc. to xml tag.
 * Class for Tags konfiguration
 */
@RootElement
public class Konfiguration {
	
	/** The sets. */
	@Element
	public VIsets visets;
		
	/** The isdefault. */
	@Element(defaultValue="myview")
	public String intermediate_view;
	
	/** The template. */
	@Element
	public Template template;
	
	
	/**
	 * Gets the sets the by name.
	 *
	 * @param name the name
	 * @return the sets the by name
	 */
	public VIset getSetByName(String name) {
		return visets.getSetByName(name);
	}
	
	/**
	 * Gets the default set.
	 *
	 * @return the default set
	 */
	public VIset getDefaultSet() {
		return visets.getDefaultSet();
	}
	
	
}
