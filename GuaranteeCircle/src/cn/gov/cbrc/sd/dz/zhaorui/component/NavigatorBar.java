package cn.gov.cbrc.sd.dz.zhaorui.component;
/**
 * 左侧导航栏
 */
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class NavigatorBar extends JToolBar {

//	/**
//	 * 导航栏的七个按钮:基站、无线网络控制器、核心网、操作维护中心、网络规划、网络优化、管理入口
//	 */
//	private JToggleButton nb,rnc,cn,omc,plan,optimize,manage;	

	private ButtonGroup buttonGroup;
	
	public NavigatorBar()
	{
		super();
//		FlowLayout layout=new FlowLayout();
//		layout.setVgap(1);
//		this.setLayout(layout);
		this.setLayout(null);
		this.setFloatable(false);
		this.setOrientation(NavigatorBar.VERTICAL);
		this.setPreferredSize(new Dimension(60,0));
		//addButtons();
	}

	/*private void addButtons() {
		Dimension dimesion=new Dimension(60,60);
		
		nb=new JToggleButton(ResourceManager.getIcon("navigator1.png", false));
		nb.setLocation(0, 0);
		nb.setSize(dimesion);
		this.add(nb);
		buttonGroup.add(nb);
		
		rnc=new JToggleButton(ResourceManager.getIcon("navigator2.png", false));
		rnc.setLocation(0, 61);
		rnc.setSize(dimesion);
		this.add(rnc);
		buttonGroup.add(rnc);
		
		cn=new JToggleButton(ResourceManager.getIcon("navigator3.png", false));
		cn.setLocation(0, 122);
		cn.setSize(dimesion);
		this.add(cn);
		buttonGroup.add(cn);
		
		omc=new JToggleButton(ResourceManager.getIcon("navigator4.png", false));
		omc.setLocation(0, 183);
		omc.setSize(dimesion);
		this.add(omc);
		buttonGroup.add(omc);
		
		plan=new JToggleButton(ResourceManager.getIcon("navigator5.png", false));
		plan.setLocation(0, 244);
		plan.setSize(dimesion);
		this.add(plan);
		buttonGroup.add(plan);
		
		optimize=new JToggleButton(ResourceManager.getIcon("navigator6.png", false));
		optimize.setLocation(0, 305);
		optimize.setSize(dimesion);
		this.add(optimize);
		buttonGroup.add(optimize);
		
		manage=new JToggleButton(ResourceManager.getIcon("navigator7.png", false));
		manage.setLocation(0, 366);
		manage.setSize(dimesion);
		this.add(manage);
		buttonGroup.add(manage);		
	}*/
	public void add(JToggleButton button){
		if(buttonGroup==null)
			buttonGroup=new ButtonGroup();
		buttonGroup.add(button);
		
		this.add((Component)button);
		
	}
}
