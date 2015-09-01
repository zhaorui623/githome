package cn.gov.cbrc.sd.dz.zhaorui.component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;

public class AboutDialog extends JDialog{

	private static AboutDialog instance=null;
	
	public AboutDialog(){
		super((JFrame)GC.getParent(),true);
		this.setTitle("关于");
		this.setSize(345,150);
		this.setResizable(false);
		this.setLocationRelativeTo(GC.getParent());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setLayout(null);
		ImageIcon icon=ResourceManager.getIcon("login.png", false);
		this.setIconImage(GC.icon.getImage());
		
		JLabel iconlabel=new JLabel();
		iconlabel.setIcon(icon);
		double scale=1;
		iconlabel.setSize((int)(icon.getIconWidth()*scale), (int)(icon.getIconHeight()*scale));
		iconlabel.setLocation(0,0);
		this.add(iconlabel);
		
		JLabel copyRight=new JLabel("Copyright © 2015 中国银监会德州监管分局.");
		copyRight.setSize(300,25);
		copyRight.setLocation(10, 70);
		this.add(copyRight);
		
		JButton ok=new JButton("确定");
		ok.setSize(65,22);
		ok.setLocation(265, 95);
		this.add(ok);
		
		ok.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}			
		});
	}
	
	public static void showDialog() {
		if(instance==null)
			instance=new AboutDialog();
		instance.setVisible(true);
	}

}
