package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.type.ExecutableType;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import jxl.Sheet;
import jxl.Workbook;

public class Step1Module extends Module {

	private File customerInfoFile = new File("D:\\TDDOWNLOAD\\eclipse\\githome\\GuaranteeCircle\\客户信息表201506.xls"),
			guaranteeInfoFile = new File("D:\\TDDOWNLOAD\\eclipse\\githome\\GuaranteeCircle\\担保关系表201506.xls");
	private Workbook guaranteeInfoBook, customerInfo;

	private DataSourcePanel cmPanel;
	

	private Graphic totalGraphic;

	public Step1Module(String id, GC gc, String name, String iconName) {
		super(id, gc, name, iconName);
		navigatorButton.doClick();
	}

	@Override
	protected List<Component> getTabs() {
		if (tabs == null) {
			tabs = new ArrayList<Component>();
			// "第一步：选择数据源"选项卡
			cmPanel = new DataSourcePanel(this);
			tabs.add(cmPanel);
		}
		return tabs;
	}

	@Override
	protected void initMenuItems() {
		menuitems.add(new JMenuItem(this.getName()));
	}

	public GC getGC() {
		// TODO Auto-generated method stub
		return this.gc;
	}

	public File getCustomerInfoFile() {
		return customerInfoFile;
	}

	public void setCustomerInfoFile(File customerInfoFile) {
		this.customerInfoFile = customerInfoFile;
	}

	public File getGuaranteeInfoFile() {
		return guaranteeInfoFile;
	}

	public void setGuaranteeInfoFile(File guaranteeInfoFile) {
		this.guaranteeInfoFile = guaranteeInfoFile;
	}

	public boolean doImport() throws Exception {
		if (guaranteeInfoFile == null || guaranteeInfoFile.exists() == false)
			JOptionPane.showMessageDialog(cmPanel, "“担保信息表”未选择或文件不存在！", "错误", JOptionPane.ERROR_MESSAGE);
		else if (customerInfoFile == null || customerInfoFile.exists() == false)
			JOptionPane.showMessageDialog(cmPanel, "“客户信息表”未选择或文件不存在！", "错误", JOptionPane.ERROR_MESSAGE);
		else {
			guaranteeInfoBook = Workbook.getWorkbook(guaranteeInfoFile);
			customerInfo = Workbook.getWorkbook(customerInfoFile);

			// 导入所有“客户信息表”中所有客户到内存中，形成“客户对象总表”
			initCorps(customerInfo);//存储在Corportion.corps里
			//在内存构建整张担保关系拓扑图对象
			initGraphic(Corporation.getAllCorps(),guaranteeInfoBook);//存储在TotalGraphic.graphic里
			return true;
		}
		return false;
	}

	private void initGraphic(Collection<Corporation> corps, Workbook book) {
		totalGraphic=new Graphic();
		//解析“担保关系表”
		Sheet sheet=book.getSheet(0);
		int rowCount = sheet.getRows();
		for (int row = 1; row < rowCount; row++) {
				String corpName1=sheet.getCell(0,row).getContents();//担保人
				String corpName2=sheet.getCell(1,row).getContents();//借款人
				Corporation corp1=Corporation.getCorpByName(corpName1);//担保人
				if(corp1==null){//说明担保人未在银行办理过信贷类业务
					//则新建一个Corporation对象，并使用默认值初始化之，并加入到Corporation.corps中
					corp1=Corporation.createDefaultCorp(corpName1);
					Corporation.addCorp(corp1);
				}
				Corporation corp2=Corporation.getCorpByName(corpName2);//借款人
				if(corp2==null){//说明借款人未在银行办理过信贷类业务----------------很奇怪，查查原因---------------
					//则新建一个Corporation对象，并使用默认值初始化之，并加入到Corporation.corps中
					corp2=Corporation.createDefaultCorp(corpName2);
					Corporation.addCorp(corp2);
				}
				//将担保人和借款人以及其担保关系加入总图中
				totalGraphic.addVertex(corp1);
				totalGraphic.addVertex(corp2);
				totalGraphic.addEdge(corp1,corp2);
		}
		totalGraphic.printBasicInfo();
	}

	private void initCorps(Workbook book) {
		Corporation.initCorps();
		//解析“客户信息表”
		Sheet sheet = book.getSheet(0);
		int rowCount = sheet.getRows(), colCount = sheet.getColumns();
		String colNames[] = new String[colCount];
		// 取所有列名，形成数组
		for (int col = 0; col < colCount; col++)
			colNames[col] = sheet.getCell(col, 0).getContents();
		// 取每一行数据，组成Corporation对象，加入到corps里
		for (int row = 1; row < rowCount; row++) {
			Corporation corp;
			LinkedHashMap<String, Object> datas = new LinkedHashMap<String, Object>();
			for (int col = 0; col < colCount; col++) {
				String key = colNames[col];
				Object value = sheet.getCell(col, row).getContents();
				datas.put(key, value);
			}
			corp = new Corporation(datas);
			Corporation.addCorp(corp);
		}		
	}

	public Graphic getTotalGraphic() {
		return this.totalGraphic;
	}

}
