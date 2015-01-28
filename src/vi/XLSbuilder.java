package vi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ConditionalFormatting;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import configuration.Consts;
import configuration.Konfiguration;

public class XLSbuilder {
	
	private Workbook template_wb;
	private Konfiguration config;
	
	//indezes for rows + columns
	private int typzeile;
	private int deskriptivzeile;
	private int richtungszeile;
	private int typspalte;
	private int idspalte;
	private int gruppenspalte;
	private int vergleichsspalte;
	private int effektspalte;
	
	private CellReference yearcell;
	private CellReference vicell;
	

	public XLSbuilder(Workbook template_wb, Konfiguration config) {
		this.template_wb = template_wb;
		this.config=config;
		
		//get indizes for columns
		CellReference ref = new CellReference(config.template.indikatorsheet.typspalte+config.template.indikatorsheet.typzeile);
		typzeile = ref.getRow();
		typspalte = ref.getCol();
		ref = new CellReference(config.template.indikatorsheet.idspalte+config.template.indikatorsheet.deskriptivzeile);
		deskriptivzeile = ref.getRow();
		idspalte = ref.getCol();
		ref = new CellReference(config.template.indikatorsheet.gruppenspalte+config.template.indikatorsheet.richtungszeile);
		richtungszeile = ref.getRow();
		gruppenspalte = ref.getCol();	
		ref = new CellReference(config.template.indikatorsheet.vergleichsspalte+config.template.indikatorsheet.richtungszeile);
		vergleichsspalte = ref.getCol();	
		ref = new CellReference(config.template.indikatorsheet.effektspalte+config.template.indikatorsheet.richtungszeile);
		effektspalte = ref.getCol();	
		
		yearcell = new CellReference(config.template.indikatorsheet.jahr);
		vicell = new CellReference(config.template.indikatorsheet.viset);
	}
	
	public static void copyRow(Row srcRow, Row destRow) {
		destRow.setHeight(srcRow.getHeight());
		for (int j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
			Cell oldCell = srcRow.getCell(j);
			Cell newCell = destRow.getCell(j);
			if (oldCell != null) {
				if (newCell == null) {
					newCell = destRow.createCell(j);
				}
				copyCell(oldCell, newCell, true);
			}
		}
	}
	
		
	public static void copyCell(Cell oldCell, Cell newCell, boolean copyStyle) {
		if (copyStyle) {
			//normal style
			newCell.setCellStyle(oldCell.getCellStyle());
		}
		switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellType(Cell.CELL_TYPE_BLANK);
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				//replaces reference to OWN row number in formula (but: not any other references)
				newCell.setCellFormula(oldCell.getCellFormula().replaceAll("([A-Z])"+(oldCell.getRowIndex()+1), "$1"+(newCell.getRowIndex()+1)));
				break;
			default:
			break;
		}
	}
	
	
	//is cell in cellrange?
	private boolean containsCell(CellRangeAddress cra, Cell cell){
		if(cra.getFirstRow()<=cell.getRowIndex() && cra.getLastRow()>=cell.getRowIndex()){
			if(cra.getFirstColumn()<=cell.getColumnIndex() && cra.getLastColumn()>=cell.getColumnIndex()){
				return true;
			}
		}
		return false;
	}

	//expands (or rewrites) conditional formatting to new cell range, based on source style cell
	private void retargetConditionalFormatting(Cell styleCell, List<CellRangeAddress> newcellrange){
		SheetConditionalFormatting xscf = styleCell.getSheet().getSheetConditionalFormatting();
		//get all cond. formats for sheet
		for(int idx = 0;idx<xscf.getNumConditionalFormattings();idx++){
			ConditionalFormatting cf = xscf.getConditionalFormattingAt(idx);
			List<CellRangeAddress> cra = Arrays.asList(cf.getFormattingRanges());
			boolean contains = false;
			//test if stylecell is in cond. format range 
			for(CellRangeAddress c:cra){
				if(containsCell(c, styleCell)) {
					contains = true;
				}
			}
			//if so, apply new range
			if (contains) {
				//get all rules of cond. format
				List<ConditionalFormattingRule> cfs = new ArrayList<ConditionalFormattingRule>();
				for(int ci=0;ci<cf.getNumberOfRules();ci++){
					cfs.add(cf.getRule(ci));
				}
				//apply new range
				xscf.addConditionalFormatting(newcellrange.toArray(new CellRangeAddress[newcellrange.size()]),cfs.toArray(new ConditionalFormattingRule[cfs.size()]));
				xscf.removeConditionalFormatting(idx);
			}
		}
	}
	
	public Workbook build(String viset, ArrayList<Integer> years, Indikatoren indikatoren) {
		Cell cell;
		//init rows in template sheet
		Sheet tmpsheet=template_wb.getSheet(config.template.indikatorsheet.name);
		Row typrow = tmpsheet.getRow(typzeile);
		Row deskrow = tmpsheet.getRow(deskriptivzeile);
		Row richrow = tmpsheet.getRow(richtungszeile);
		Row newRow;
		//for all years
		for (int year : years) {
			//create new sheet from template
			Sheet newsheet=template_wb.cloneSheet(template_wb.getSheetIndex(config.template.indikatorsheet.name));
			//set new name and move to front
			template_wb.setSheetName(template_wb.getSheetIndex(newsheet),config.template.indikatorsheet.name + Integer.toString(year));
			template_wb.setSheetOrder(config.template.indikatorsheet.name + Integer.toString(year),0);
			//write Year
			newRow = newsheet.getRow(yearcell.getRow());
			if (newRow != null) {
			   cell = newRow.getCell(yearcell.getCol());
			   if (cell != null) cell.setCellValue(year);
			}
			//write VI set
			newRow = newsheet.getRow(vicell.getRow());
			if (newRow != null) {
			   cell = newRow.getCell(vicell.getCol());
			   if (cell != null) cell.setCellValue(viset);
			}
			//remove template rows
			newsheet.removeRow(newsheet.getRow(typzeile));
			newsheet.removeRow(newsheet.getRow(deskriptivzeile));
			newsheet.removeRow(newsheet.getRow(richtungszeile));
			
			//init cell range addessses for cond. formats for deskriptiv und richtung
			List<CellRangeAddress> deskriptivrange = new ArrayList<CellRangeAddress>();
			List<CellRangeAddress> richtungrange = new ArrayList<CellRangeAddress>();
			//Add real indikators to new year sheet
			int rowIndex = typzeile;
			String typ = "";
			for (Indikator in : indikatoren.getAllIndikators()) {
				//write new typ-row?
				if (!typ.equals(in.getIndikatortyp())) {
					typ = in.getIndikatortyp();
					newRow = newsheet.createRow(rowIndex);
					copyRow(typrow, newRow);
					newRow.getCell(typspalte).setCellValue(typ);
					rowIndex++;
				}
				//write indikator line
				newRow = newsheet.createRow(rowIndex);
				if (in.getRichtung().equals(Consts.RICHTUNGdesk)) {
					copyRow(deskrow, newRow); //deskriptiv-Zeile
					deskriptivrange.add(new CellRangeAddress(newRow.getRowNum(),newRow.getRowNum(),effektspalte,effektspalte)); //first row, last row, first column, last column
				}
				else {
					copyRow(richrow, newRow); //hoch oder niedrig-Zeile
					richtungrange.add(new CellRangeAddress(newRow.getRowNum(),newRow.getRowNum(),effektspalte,effektspalte)); //first row, last row, first column, last column
				}
				//fill with actual values
				newRow.getCell(typspalte).setCellValue(typ);
				newRow.getCell(idspalte).setCellValue(in.getName());
				newRow.getCell(idspalte+1).setCellValue(in.getBezeichner());
				newRow.getCell(idspalte+2).setCellValue(in.getRichtung());
				newRow.getCell(idspalte+3).setCellValue(in.getEinheit());
				//set IG value
				if (in.getValueIG(year) != Consts.noValue)
					newRow.getCell(gruppenspalte).setCellValue(in.getValueIG(year));
				else newRow.getCell(gruppenspalte).setCellValue("");
				//set VG value
				if (in.getValueVG(year) != Consts.noValue)
					newRow.getCell(vergleichsspalte).setCellValue(in.getValueVG(year));
				else newRow.getCell(vergleichsspalte).setCellValue("");
				rowIndex++;
			}
			
			//apply conditional formating
			retargetConditionalFormatting(newsheet.getRow(deskriptivzeile).getCell(effektspalte),deskriptivrange);
			retargetConditionalFormatting(newsheet.getRow(richtungszeile).getCell(effektspalte),richtungrange);
		}
		
		//remove template sheet
		template_wb.removeSheetAt(template_wb.getSheetIndex(tmpsheet));
		
		//Set to recalculate formulas once opened
		template_wb.setForceFormulaRecalculation(true);
		
		return template_wb;
	}
	
	

}
