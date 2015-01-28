package configuration;

import org.xmappr.Element;

public class Template {
			
		/** The excelfile-pfad. */
		@Element
		public String excelfile;
		
		/** The indiaktorsheet. */
		@Element
		public Indikatorsheet indikatorsheet;
		
		/**
		 * Gets the excelfile path.
		 *
		 * @return the excelfile path
		 */
		public String getExcelfile() {
			return excelfile;
		}


	}

