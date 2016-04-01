package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.type.ExecutableType;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.VIPCustomerGroup;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import jxl.Sheet;
import jxl.Workbook;

public class Step1Module extends Module {

	private File customerInfoFile = new File("D:\\TDDOWNLOAD\\eclipse\\githome\\GuaranteeCircle\\客户信息表201506.xls"),
			guaranteeInfoFile = new File("D:\\TDDOWNLOAD\\eclipse\\githome\\GuaranteeCircle\\担保关系表201506.xls"),
			vipCustomerFile = new File("D:\\TDDOWNLOAD\\eclipse\\githome\\GuaranteeCircle\\重点客户清单.xls");
	private Workbook guaranteeInfoBook, customerInfo, vipCustomerBook;

	private DataSourcePanel cmPanel;

	private List<VIPCustomerGroup> vipCustomerGroupList;

	private Graphic totalGraphic;

	private boolean skipVIPCustomerAnlys = true;;

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

	public void setVipCustomerFile(File vipCustomerFile) {
		this.vipCustomerFile = vipCustomerFile;
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
			Map<String, Corporation> corps = initCorps(customerInfo);// 存储在corps里
			// 在内存构建整张担保关系拓扑图对象
			initGraphic(corps, guaranteeInfoBook);// 存储在Step1Module.totalGraphic里
			if (vipCustomerFile != null && vipCustomerFile.exists()) {
				vipCustomerBook = Workbook.getWorkbook(vipCustomerFile);
				// 生成重点客户列表
				initVIPCustomerGroupList(totalGraphic, vipCustomerBook);// 存储在Step1Module.vipCustomerGroupList里
				skipVIPCustomerAnlys = false;
			} else {
				InfoPane.getInstance().info("“重点客户清单”未选择或文件不存在，将不进行重点客户维度分析");
				skipVIPCustomerAnlys = true;
			}
			return true;
		}
		return false;
	}

	private void initVIPCustomerGroupList(Graphic totalGraphic, Workbook book) {
		vipCustomerGroupList = new ArrayList<VIPCustomerGroup>();
		Sheet sheet = book.getSheet(0);
		int rowCount = sheet.getRows();
		for (int row = 2; row < rowCount; row++) {
			String index = String.valueOf(sheet.getCell(0, row).getContents()).trim();
			String groupName = String.valueOf(sheet.getCell(1, row).getContents()).trim();
			String corpName = String.valueOf(sheet.getCell(2, row).getContents()).trim();
			if (corpName.equals("") || corpName.equals("null"))
				continue;
			VIPCustomerGroup group;
			if (index.equals("") || index.equals("null")) {// 如果没有序号，说明当前组还没完，沿用刚才的组对象
				// 取当前vipCustomerGroupList的最后一个元素的下标（注意list的最后一个元素与row的当前行并不是同一个概念）
				int last = vipCustomerGroupList.size() - 1;
				group = vipCustomerGroupList.get(last);
			} else {
				group = new VIPCustomerGroup(groupName);
				vipCustomerGroupList.add(group);
			}
			Corporation corp = totalGraphic.getVertexByName(corpName);
			if (corp != null) // 只处理担保关系表中涵盖的客户
				group.addCorporation(corp);
		}
	}

	private void initGraphic(Map<String, Corporation> corps, Workbook book) {
		totalGraphic = new Graphic();
		// 解析“担保关系表”
		Sheet[] sheets = book.getSheets();
		for (Sheet sheet : sheets) {
			int rowCount = sheet.getRows();
			for (int row = 1; row < rowCount; row++) {
				String corpName1 = sheet.getCell(0, row).getContents();// 担保人
				String corpName2 = sheet.getCell(1, row).getContents();// 借款人
				Corporation corp1 = corps.get(corpName1);// 担保人
				if (corp1 == null) {// 说明担保人未在银行办理过信贷类业务
					// 则新建一个Corporation对象，并使用默认值初始化之，并加入到Corporation.corps中
					corp1 = Corporation.createDefaultCorp(corpName1);
					corps.put(corp1.getName(), corp1);
				}
				Corporation corp2 = corps.get(corpName2);// 借款人
				if (corp2 == null) {// 说明借款人未在银行办理过信贷类业务----------------很奇怪，查查原因---------------
					// 则新建一个Corporation对象，并使用默认值初始化之，并加入到Corporation.corps中
					corp2 = Corporation.createDefaultCorp(corpName2);
					corps.put(corp2.getName(), corp2);
				}
				// 将担保人和借款人以及其担保关系加入总图中
				totalGraphic.addVertex(corp1);
				totalGraphic.addVertex(corp2);
				totalGraphic.addEdge(corp1, corp2);
			}
		}
		totalGraphic.printBasicInfo();
	}

	private Map<String, Corporation> initCorps(Workbook book) {
		Map<String, Corporation> corps = new HashMap<String, Corporation>();
		// 解析“客户信息表”
		Sheet sheet = book.getSheet(0);
		int colCount = sheet.getColumns();
		String colNames[] = new String[colCount];
		// 取所有列名，形成数组
		for (int col = 0; col < colCount; col++)
			colNames[col] = sheet.getCell(col, 0).getContents();

		// 取每一行数据，组成Corporation对象，加入到corps里
		Sheet sheets[] = book.getSheets();
		for (int s = 0; s < sheets.length; s++) {
			int rowCount = sheets[s].getRows();
			for (int row = 1; row < rowCount; row++) {
				Corporation corp;
				LinkedHashMap<String, Object> datas = new LinkedHashMap<String, Object>();
				for (int col = 0; col < colCount; col++) {
					String key = colNames[col];
					Object value = sheets[s].getCell(col, row).getContents();
					datas.put(key, value);
				}
				corp = new Corporation(datas);
				corps.put(corp.getName(), corp);
			}
		}
		return corps;
	}

	public Graphic getTotalGraphic() {
		return this.totalGraphic;
	}

	public List<VIPCustomerGroup> getVIPCustomerGroupList() {
		return this.vipCustomerGroupList;
	}

	public boolean isSkipVIPCustomerAnlys() {
		return skipVIPCustomerAnlys;
	}

	public void setSkipVIPCustomerAnlys(boolean skipVIPCustomerAnlys) {
		this.skipVIPCustomerAnlys = skipVIPCustomerAnlys;
	}

}
