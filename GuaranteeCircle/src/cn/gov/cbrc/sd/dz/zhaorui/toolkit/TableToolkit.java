package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.io.File;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class TableToolkit {
	public static void fitTableColumns(JTable myTable) {
		JTableHeader header = myTable.getTableHeader();
		int rowCount = myTable.getRowCount();

		Enumeration columns = myTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
			int width = (int) myTable.getTableHeader().getDefaultRenderer()
					.getTableCellRendererComponent(myTable, column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) myTable.getCellRenderer(row, col)
						.getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			header.setResizingColumn(column); // 此行很重要
			column.setWidth(width + myTable.getIntercellSpacing().width);
		}
	}

	public static void exp2Excel(JTable table, File excelFile) throws Exception {
		if (table == null)
			return;
		if (excelFile == null)
			throw new Exception("未指定导出文件！");
//		if (excelFile.exists())
//			if (excelFile.delete() == false)
//				throw new Exception(excelFile.getAbsolutePath() + "正在被使用中！");
		WritableWorkbook book = Workbook.createWorkbook(excelFile);
		WritableSheet sheet =book.createSheet("sheet1", 0);		
		TableModel model = table.getModel();
		int colCount = model.getColumnCount();
		int rowCount = model.getRowCount();

		// 写表头
		for (int col = 0; col < colCount; col++) {
			WritableCell cell = new Label(col, 0, model.getColumnName(col));
			sheet.addCell(cell);
		}
		// 写表的数据
		for (int row = 1; row <= rowCount; row++) {
			for (int col = 0; col < colCount; col++) {
				WritableCell cell = null;
				String value = String.valueOf(model.getValueAt(row-1, col));
				Class clas = model.getColumnClass(col);
				if (clas.equals(String.class))
					cell = new Label(col,row, value);
				else if (clas.equals(Double.class))
					cell = new Number(col, row, Double.parseDouble(value));
				if (cell != null)
					sheet.addCell(cell);
			}
		}
		book.write();
		book.close();
	}
}
