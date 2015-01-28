package vi;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Indikatoren {
	
	private HashMap<String,Indikator> indikator = new LinkedHashMap <String,Indikator>();

	public Indikatoren(Excelfile efile) {
		//get all indikatoren for set from excelfile
		Indikator in;
		while (efile.hasNextIndikator()) {
			in = efile.getNextIndikator();
			if (in != null) {
				indikator.put(in.getId(), in);
			}
		}
	}
	
	public void addValue(String id, int year, float value, boolean isIG) {
		if (indikator.containsKey(id)) indikator.get(id).setValue(year, value, isIG);
	}
	
	public Collection<Indikator> getAllIndikators() {
		return indikator.values();
	}
	
	public Indikator getIndikator(String id) {
		if (indikator.containsKey(id)) return indikator.get(id);
		return null;
	}

}
