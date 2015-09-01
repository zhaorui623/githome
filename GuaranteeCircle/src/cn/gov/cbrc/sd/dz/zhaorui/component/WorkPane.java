package cn.gov.cbrc.sd.dz.zhaorui.component;

/**
 * 主工作区面板
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.ImageProducer;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;

@SuppressWarnings("serial")
public class WorkPane extends JPanel {

	JTabbedPane tabbedPane;
	
	Color color1, color2;
	
	List<Component> tabs;

	JLabel background;

	private boolean isInitedTabsPane=false;
	
	public WorkPane() {
		super();
		color1 = new Color(51, 96, 145);
		color2 = new Color(212, 208, 200);
//		this.setBackground(color1);
		this.setLayout(new BorderLayout());		
		ImageIcon icon=ResourceManager.getIcon("background.png", false);		
		background=new JLabel(icon);
		this.add(background);
	}
	
	public boolean isBackgroundShowing(){
		return !isInitedTabsPane;
	}
	
	public void initTabPane(){
		this.remove(background);
		tabbedPane = new JTabbedPane();		
		this.add(tabbedPane,"Center");
		isInitedTabsPane=true;
	}

	public void setTabs(List<Component> tabs) {
		this.tabs = tabs;
		this.setBackground(color2);
		tabbedPane.removeAll();
		for (Component tab : tabs)
			tabbedPane.addTab(tab.getName(), null, tab, tab.getName());
	}

	public void setCurrentTab(String tabName) {
		for (Component comp : tabs)
			if (comp.getName().equals(tabName))
				tabbedPane.setSelectedComponent(comp);

	}

	public boolean isInited() {
		return isInitedTabsPane;
	}

}
