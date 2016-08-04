package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
	
	private String reportDate;
	
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
		else if (reportDate == null || String.valueOf(reportDate).length() == 0)
			JOptionPane.showMessageDialog(cmPanel, "“数据期次”未设置！", "错误", JOptionPane.ERROR_MESSAGE);
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
			String orgCode = String.valueOf(sheet.getCell(2, row).getContents()).trim();
			String corpName = String.valueOf(sheet.getCell(3, row).getContents()).trim();
//			if (corpName.equals("") || corpName.equals("null"))
//				continue;
			VIPCustomerGroup group;
			if (index.equals("") || index.equals("null")) {// 如果没有序号，说明当前组还没完，沿用刚才的组对象
				// 取当前vipCustomerGroupList的最后一个元素的下标（注意list的最后一个元素与row的当前行并不是同一个概念）
				int last = vipCustomerGroupList.size() - 1;
				group = vipCustomerGroupList.get(last);
			} else {
				group = new VIPCustomerGroup(groupName);
				vipCustomerGroupList.add(group);
			}
			Corporation corp = totalGraphic.getVertexEqualTo(Corporation.createDefaultCorp(orgCode, corpName));
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
				String corpOrgcode1 = String.valueOf(sheet.getCell(0, row).getContents()).toUpperCase();// 担保人组织机构代码
				String corpName1 = sheet.getCell(1, row).getContents();// 担保人名称
				String corpOrgcode2 = String.valueOf(sheet.getCell(2, row).getContents()).toUpperCase();// 借款人组织机构代码
				String corpName2 = sheet.getCell(3, row).getContents();// 借款人名称
				String outGuarantValue1 = sheet.getCell(4, row).getContents();// 担保人对外担保金额
				String corpOutGuaranteedLoanBalance1 = sheet.getCell(5, row).getContents();// 担保人对外担保贷款对应的贷款余额

				Corporation corp1 = findInCorps(corps, corpOrgcode1, corpName1);// 根据担保人信息在客户信息表里寻找是否有对应的客户
				if (corp1 == null) {// 说明担保人未在银行办理过信贷类业务
					// 则新建一个Corporation对象，并使用默认值初始化之，并加入到Corporation.corps中
					corp1 = Corporation.createDefaultCorp(corpOrgcode1, corpName1);
					corps.put(corp1.getOrgCode(), corp1);
				}
				corp1.setOutGuarantValue(Double.parseDouble(outGuarantValue1));//担保人对外担保金额
				corp1.setOutGuaranteedLoanBalance(Double.parseDouble(corpOutGuaranteedLoanBalance1));//担保人对外担保贷款对应的贷款余额
				
				Corporation corp2 = findInCorps(corps, corpOrgcode2, corpName2);// 根据借款人信息在客户信息表里寻找是否有对应的客户
				if (corp2 == null) {// 说明借款人未在银行办理过信贷类业务----------------很奇怪，查查原因---------------
					// 则新建一个Corporation对象，并使用默认值初始化之，并加入到Corporation.corps中
					corp2 = Corporation.createDefaultCorp(corpOrgcode2, corpName2);
					corps.put(corp2.getOrgCode(), corp2);
				}
//				corp2.setGuaranteedLoanBalance(Double.parseDouble(corpGuaranteedLoanBalance2));
//				corp2.setOutGuaranteedLoanBalance(Double.parseDouble(corpOutGuaranteedLoanBalance2));
				// 将担保人和借款人以及其担保关系加入总图中
				totalGraphic.addVertex(corp1);
				totalGraphic.addVertex(corp2);
				if(corp1.equals(corp2)==false){
				totalGraphic.addEdge(corp1, corp2);}
			}
		}
		totalGraphic.printBasicInfo();
	}

	private Corporation findInCorps(Map<String, Corporation> corps, String corpOrgcode, String corpName) {

		Corporation corp = null;
		// 先直接用组织机构代码找
		if(corpOrgcode.length()!=0&&corpOrgcode.equals("NULL")==false)
			corp = corps.get(corpOrgcode);
		
//		//如果用组织机构代码找不到，就用名字找
//		if(corp==null){
//			Iterator<Corporation> iter=corps.values().iterator();
//			while(iter.hasNext()){
//				Corporation c=iter.next();
//				if(c.getName().equals(corpName))
//					return c;
//			}
//		}
		
		return corp;
	}

	private Map<String, Corporation> initCorps(Workbook book) {
		Map<String, Corporation> corps = new HashMap<String, Corporation>();
		// 解析“客户信息表”
		Sheet sheet = book.getSheet(0);
		int colCount = sheet.getColumns();

		List<String> colNames = new ArrayList<String>();
		// 取所有列名，形成数组
		for (int col = 0; col < colCount; col++){
			String contents=sheet.getCell(col, 0).getContents();
			if(contents==null||String.valueOf(contents).trim().length()==0)
				break;
			colNames.add(contents);
		}

		// 取每一行数据，组成Corporation对象，加入到corps里
		Sheet sheets[] = book.getSheets();
		for (int s = 0; s < sheets.length; s++) {
			int rowCount = sheets[s].getRows();
			for (int row = 1; row < rowCount; row++) {
				Corporation corp;
				LinkedHashMap<String, Object> datas = new LinkedHashMap<String, Object>();
				for (int col = 0; col < colCount; col++) {
					String key = colNames.get(col);
					Object value = sheets[s].getCell(col, row).getContents();
					datas.put(key, value);
				}
				corp = new Corporation(datas);
				corps.put(corp.getOrgCode(), corp);
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

	public void setReportDate(String reportDate) {
		this.reportDate=reportDate;
	}

	public String getReportDate() {
		// TODO Auto-generated method stub
		return reportDate;
	}

}
