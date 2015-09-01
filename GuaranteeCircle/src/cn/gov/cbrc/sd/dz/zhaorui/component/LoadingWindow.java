package cn.gov.cbrc.sd.dz.zhaorui.component;

import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;

class LoadingWindow extends JWindow{
		
		public LoadingWindow(Container parent){
			super();
			ImageIcon icon=ResourceManager.getIcon("loading.gif", false);
			JLabel label=new JLabel(icon);
			this.add(label);
			this.pack();
			this.setLocation(parent.getX()+(int) (parent.getWidth() / 2 - icon.getIconWidth() / 2), parent.getY()+(int) (parent.getHeight() / 2 - icon.getIconHeight() / 2));
			
		}
	}