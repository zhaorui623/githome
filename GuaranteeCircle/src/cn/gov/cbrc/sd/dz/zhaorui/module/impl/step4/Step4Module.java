package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3.Step3Module;

public class Step4Module extends Module {
	
	ResultPanel resultPanel;

	public Step4Module(String id,GC gc,String name,String iconName) {
		super(id, gc, name,iconName);
	}

	@Override
	protected List<Component> getTabs() {
		if(tabs==null){
			tabs=new ArrayList<Component>();
			//"第四步：查看结果"选项卡
			resultPanel=new ResultPanel(this);
			tabs.add(resultPanel);
		}		
		try {
			resultPanel.refreshResult(((Step3Module)gc.getModule("3")).getResultGraphics());
		} catch (Exception e) {
			e.printStackTrace();
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

}
