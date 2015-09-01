package cn.gov.cbrc.sd.dz.zhaorui.component;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import java.io.FileOutputStream;

public class CreateXL {

	/** Excel 文件要存放的位置，假定在D盘JTest目录�?*/

	public static String outputFile = "D:/gongye.xls";

	public static void main(String argv[]) {

		try {

			// 创建新的Excel 工作�?

			HSSFWorkbook workbook = new HSSFWorkbook();

			// 在Excel工作簿中建一工作表，其名为缺省�?
			// 如要新建�?���?效益指标"的工作表，其语句为：
			// HSSFSheet sheet = workbook.createSheet("效益指标");

			HSSFSheet sheet = workbook.createSheet();

			// 在索�?的位置创建行（最顶端的行�?

			HSSFRow row = sheet.createRow((short) 0);

			// 在索�?的位置创建单元格（左上端�?
			HSSFCell cell = row.createCell((short) 0);
			// 定义单元格为字符串类�?
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			// 在单元格中输入一些内�?
			cell.setCellValue("增加�?");
			// 新建�?��出文件流
			FileOutputStream fOut = new FileOutputStream(outputFile);
			// 把相应的Excel 工作簿存�?
			workbook.write(fOut);
			fOut.flush();
			// 操作结束，关闭文�?
			fOut.close();
			System.out.println("文件生成...");

		} catch (Exception e) {
			System.out.println("已运�?xlCreate() : " + e);
		}
	}
}