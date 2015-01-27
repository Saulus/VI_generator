package vi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import configuration.Consts;

// TODO: Auto-generated Javadoc
/**
 * The Class Excelfile.
 */
public class Excelfile {
	
	/** The my workbook. */
	private Workbook myWorkbook;
	
	/** The my sheet. */
	private Sheet mySheet;
	
	/** The current row num. */
	private int currentRowNum = 0;
	
	/** The id col num. */
	private int idColNum;
	
	/** The zaehler col num. */
	private int zaehlerColNum;
	
	/** The zaehler teiler col num. */
	private int zaehlerTeilerColNum;
	
	/** The nenner col num. */
	private int nennerColNum;
	
	/** The nenner teiler col num. */
	private int nennerTeilerColNum;
	
	/** The status col num. */
	private int statusColNum;
	

	/**
	 * Instantiates a new excelfile.
	 *
	 * @param filename the filename
	 * @throws InvalidFormatException the invalid format exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Excelfile(String filename) throws InvalidFormatException, FileNotFoundException, IOException {
		myWorkbook = WorkbookFactory.create(new FileInputStream(filename));
		mySheet = myWorkbook.getSheetAt(Consts.VIsheetNum);
		//set col-Nums from VI set sheet
		Row r = mySheet.getRow(currentRowNum);
		DataFormatter df = new DataFormatter();
		for (Cell cell : r) {
			 switch (df.formatCellValue(cell)) {
             	case Consts.IDcol: idColNum = cell.getColumnIndex();
             	case Consts.IDZaehlerCol: zaehlerColNum = cell.getColumnIndex();
             	case Consts.IDZaehlerTeilerCol: zaehlerTeilerColNum = cell.getColumnIndex();
             	case Consts.IDNennerCol: nennerColNum = cell.getColumnIndex();
             	case Consts.IDNennerTeilerCol: nennerTeilerColNum = cell.getColumnIndex();
             	case Consts.statusCol: statusColNum =cell.getColumnIndex();
			 }
        }
		currentRowNum++;
	}
	
	/**
	 * Checks for next indikator.
	 *
	 * @return true, if successful
	 */
	public boolean hasNextIndikator() {
		return !mySheet.getRow(currentRowNum).getCell(idColNum).getRichStringCellValue().toString().isEmpty();
	}
	
	/**
	 * Gets the next indikator.
	 *
	 * @return the next indikator
	 */
	public Indikator getNextIndikator() {
		DataFormatter df = new DataFormatter();
		boolean statusOk = false;
		for (String element : Consts.statusUsed) { //test if status is ok
		    if (df.formatCellValue(mySheet.getRow(currentRowNum).getCell(statusColNum)).equals(element)) {
		    	statusOk = true;
		        break;
		    }
		}
		Indikator ind = null;
		if (statusOk) {
			ind = new Indikator(
					df.formatCellValue(mySheet.getRow(currentRowNum).getCell(idColNum)),
					df.formatCellValue(mySheet.getRow(currentRowNum).getCell(zaehlerColNum)),
					df.formatCellValue(mySheet.getRow(currentRowNum).getCell(zaehlerTeilerColNum)),
					df.formatCellValue(mySheet.getRow(currentRowNum).getCell(nennerColNum)),
					df.formatCellValue(mySheet.getRow(currentRowNum).getCell(nennerTeilerColNum))
			);
			//test if all columns are filled
			if (ind.getId().isEmpty() || ind.getNenner().isEmpty() || ind.getNennerTeiler().isEmpty() || ind.getZaehler().isEmpty() || ind.getZaehlerTeiler().isEmpty())
				ind = null;
		}
		currentRowNum++;
		return ind;
	}

}
