package configuration;

import org.xmappr.Element;

public class Indikatorsheet {
	
	@Element(defaultValue="Indikatoren")
	public String name;
	
	@Element(defaultValue="C2")
	public String viset;
	
	@Element(defaultValue="C3")
	public String jahr;
	
	@Element(defaultValue="12")
	public String typzeile;
	
	@Element(defaultValue="13")
	public String deskriptivzeile;
	
	@Element(defaultValue="14")
	public String richtungszeile;
	
	@Element(defaultValue="A")
	public String typspalte;
	
	@Element(defaultValue="B")
	public String idspalte;
	
	@Element(defaultValue="F")
	public String gruppenspalte;
	
	@Element(defaultValue="G")
	public String vergleichsspalte;

	@Element(defaultValue="H")
	public String effektspalte;

}
