package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import jxl.Cell;
import jxl.format.CellFormat;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelToolkit {

	public static void addNumberCell(WritableSheet sheet, int col, int row, double value)
			throws RowsExceededException, WriteException {
		Cell cell = sheet.getCell(1, row);
		if (cell != null && cell.getCellFormat() != null)
			sheet.addCell(new Number(col, row, value, cell.getCellFormat()));
		else
			sheet.addCell(new Number(col, row, value));
	}

}
