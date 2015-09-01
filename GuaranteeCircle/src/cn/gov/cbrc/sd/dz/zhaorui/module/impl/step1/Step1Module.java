package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;

public class Step1Module extends Module {
	
	DataSourcePanel cmPanel;

	public Step1Module(String id,GC gc,String name,String iconName) {
		super(id, gc, name,iconName);
		navigatorButton.doClick();
	}

	@Override
	protected List<Component> getTabs() {
		if(tabs==null){
			tabs=new ArrayList<Component>();
			//"第一步：选择数据源"选项卡
			cmPanel=new DataSourcePanel(this);
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

}
