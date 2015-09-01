package cn.gov.cbrc.sd.dz.zhaorui.module;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.*;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;

public abstract class Module {

	// 左侧导航栏上的按钮
	protected JToggleButton navigatorButton;
	ImageIcon icon,greyicon;

	// 菜单
	protected JMenu menu;
	protected List<JMenuItem> menuitems;

	// 工作区域内的所有标签页
	protected List<Component> tabs;

	protected WorkPane workPane;

	protected GC gc;

	protected String id;

	private String name;
	

	public static Map<String, Module> modules = new HashMap<String, Module>();

	@SuppressWarnings("unchecked")
	public static void createModules(GC galileo) {
		Document doc = ResourceManager.getXMLDocument("modules.xml");
		NodeList list = doc.getElementsByTagName("module");
		for (int i = 0; i < list.getLength(); i++) {
			Element moduleElement = (Element) list.item(i);
			String id = moduleElement.getAttribute("id");
			if (true) {
				String className = moduleElement.getAttribute("class");
				String moduleName = moduleElement.getAttribute("name");
				String iconStr = moduleElement.getAttribute("icon");
				Class[] classargs = { String.class, GC.class,
						String.class, String.class };
				Object[] args = { id, galileo, moduleName, iconStr };

				try {
					Module module = (Module) Class.forName(className)
							.getConstructor(classargs).newInstance(args);
					modules.put(module.getId(), module);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public Module(final String id, final GC galileo, String name,
			String iconName) {
		this.id = id;
		this.gc = galileo;
		this.workPane = galileo.getWorkPane();
		this.name = name;
		this.menuitems = new ArrayList<JMenuItem>();

		// 将导航栏按钮添加到导航栏
		NavigatorBar navigator = galileo.getNavigatorBar();
		navigatorButton = new JToggleButton();
		Dimension dimesion = new Dimension(60, 60);
		navigatorButton.setSize(dimesion);
		// navigatorButton.setText(name);
		navigatorButton.setToolTipText(name);
		navigatorButton.setLocation(0, 61 * modules.size());
		icon = ResourceManager.getIcon(iconName, false);
		greyicon = ResourceManager.getIcon(iconName, true);
		navigatorButton.setIcon(greyicon);
		navigator.add(navigatorButton);

		// 将菜单添加至菜单栏
//		GalileoMenuBar menubar = galileo.getMenuBar();
//		menu = new JMenu(name);
//		menubar.addMenu(menu);
//		initMenuItems();
//		addMenuItems();

		navigatorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (workPane.isInited() == false)
					workPane.initTabPane();
				List<Component> tabs = getTabs();
				workPane.setTabs(tabs);
				galileo.setCurrentModule(id);
			}
		});
		final JPopupMenu popup = new JPopupMenu();
		navigatorButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(navigatorButton, e.getX(), e.getY());
				}
			}
		});
		navigatorButton.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED)
					navigatorButton.setIcon(icon);
				if(e.getStateChange()==ItemEvent.DESELECTED)
					navigatorButton.setIcon(greyicon);
			}
		});
		JMenuItem refresh = new JMenuItem("刷新");
		popup.add(refresh);
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
	}

	public void refresh() {
		tabs = null;
		getTabs();
		navigatorButton.getActionListeners()[0].actionPerformed(null);
	}

	protected abstract void initMenuItems();

	public void addMenuItems() {
		for (JMenuItem item : menuitems) {
			menu.add(item);
			item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					navigatorButton.doClick();
				}
			});
		}
	}

	// 获取左侧导航栏上的按钮
	public JToggleButton getNavigatorButton() {
		return navigatorButton;
	}

	// 获取工作区域内的所有标签页
	protected abstract List<Component> getTabs();

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static Module getModule(String moduleName) {

		return modules.get(moduleName);
	}

	public static void refreshAll() {
		Object[] ms = modules.values().toArray();

		for (int i = 0; i < ms.length; i++) {
			Module m = (Module) ms[i];
			m.refresh();
		}
	}

	public static void gotoStep(int step) {
		modules.get(String.valueOf(step)).getNavigatorButton().doClick();
	}
}
