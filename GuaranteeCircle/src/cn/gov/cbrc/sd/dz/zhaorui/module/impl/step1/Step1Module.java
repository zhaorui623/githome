package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.ExecutableType;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import jxl.Workbook;

public class Step1Module extends Module {

	private File customerInfoFile, guaranteeInfoFile;
	private Workbook guaranteeInfoBook, customerInfo;

	private DataSourcePanel cmPanel;

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
			return true;
		}
		return false;
	}

}
