package configuration;

import java.util.List;
import org.xmappr.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class Sets.
 * Elements from konfiguration.xml are loaded into here, into variables defined acc. to xml tag.
 * Class for Tags konfiguration->sets
 * 
 */
public class VIsets {
	
	/** The datei. */
	@Element
	public List<VIset> viset; 
	
	/**
	 * Gets the sets the by name.
	 *
	 * @param name the name
	 * @return the sets the by name
	 */
	public VIset getSetByName(String name) {
		for (VIset myset: viset) {
			if (name.toLowerCase().equals(myset.getName())) return myset;
		}
		return null;
	}
	
	/**
	 * Gets the default set.
	 *
	 * @return the default set
	 */
	public VIset getDefaultSet() {
		for (VIset myset: viset) {
			if (myset.isDefault()) return myset;
		}
		return null;
	}
}
