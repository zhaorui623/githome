package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;

public class Step2Module extends Module {

	ConfigPanel cmPanel;

	HugeCircleSplitAlgorithm hcsAlgm;


	public Step2Module(String id, GC gc, String name, String iconName) {
		super(id, gc, name, iconName);
	}

	@Override
	protected List<Component> getTabs() {
		if (tabs == null) {
			tabs = new ArrayList<Component>();
			// "第二步：配置参数"选项卡
			try {
				cmPanel = new ConfigPanel(this);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "加载配置参数面板出错！", "错误", JOptionPane.ERROR_MESSAGE);
				InfoPane.getInstance().error(e.toString());
				e.printStackTrace();
			}
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

	public HugeCircleSplitAlgorithm getHcsAlgm() {
		return hcsAlgm;
	}

	public void setHcsAlgm(HugeCircleSplitAlgorithm hcsAlgm) {
		this.hcsAlgm = hcsAlgm;
	}


}
