package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;

public class Step3Module extends Module {
	
	ProcessPanel pPanel;
	
	public static final int PROCEDURE_COUNT=7;

	public Step3Module(String id,GC gc,String name,String iconName) {
		super(id, gc, name,iconName);
	}

	@Override
	protected List<Component> getTabs() {
		if(tabs==null){
			tabs=new ArrayList<Component>();
			//"第三步：识别过程"选项卡
			pPanel=new ProcessPanel(this);
			tabs.add(pPanel);
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

	private Procedure procedure=new Procedure();
	public void procedure1() throws Exception {
		InfoPane.getInstance().info("执行procedure1！");
		Thread.sleep(1000);
	}

	public void procedure2() throws Exception {
		InfoPane.getInstance().info("执行procedure2！");
		Thread.sleep(2000);
		
	}

	public void procedure3() throws Exception {
		InfoPane.getInstance().info("执行procedure3！");
		Thread.sleep(3000);
		
	}

	public void procedure4() throws Exception {
		InfoPane.getInstance().info("执行procedure4！");
		Thread.sleep(4000);
		
	}

	public void procedure5() throws Exception {
		InfoPane.getInstance().info("执行procedure5！");
		Thread.sleep(5000);
		
	}

	public void procedure6() throws Exception {
		InfoPane.getInstance().info("执行procedure6！");
		Thread.sleep(6000);
		
	}

	public void procedure7() throws Exception {
		InfoPane.getInstance().info("执行procedure7！");
		Thread.sleep(7000);
		
	}
	public Procedure getProcedure() {
		return procedure;
	}

}
