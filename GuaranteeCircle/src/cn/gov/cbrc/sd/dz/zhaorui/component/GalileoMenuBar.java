package cn.gov.cbrc.sd.dz.zhaorui.component;
/**
 * 菜单栏
 */
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import cn.gov.cbrc.sd.dz.zhaorui.GC;

@SuppressWarnings("serial")
public class GalileoMenuBar extends JMenuBar {
		
	JMenu systemMenu=null,helpMenu=null;
	
	JMenuItem exit=null,about=null;
	
	
	public GalileoMenuBar(){
		super();
		
		systemMenu=new JMenu("系统");
		systemMenu.setMnemonic(KeyStroke.getKeyStroke('S',Event.ALT_MASK).getKeyCode());
		exit=new JMenuItem("退出");
		exit.setAccelerator(KeyStroke.getKeyStroke('X',Event.ALT_MASK));
		systemMenu.add(exit);
		this.add(systemMenu);

		helpMenu=new JMenu("帮助");
		helpMenu.setMnemonic(KeyStroke.getKeyStroke('H',Event.ALT_MASK).getKeyCode());
		about=new JMenuItem("关于");
		helpMenu.add(about);
		this.add(helpMenu);
//		this.setHelpMenu(helpMenu);	
		
		addListeners();
	}

	private void addListeners() {
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}			
		});
		about.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog.showDialog();
			}			
		});
	}

	public void addMenu(JMenu menu) {
		this.remove(helpMenu);
		this.add(menu);
		this.add(helpMenu);
	}
}
