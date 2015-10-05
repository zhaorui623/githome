package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JDialog;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2.CorporationListShowPanel;

public class GraphicDetailShowDialog extends JDialog {

	int WIDTH=800,HEIGHT=500;
	
	public GraphicDetailShowDialog(Graphic circle) throws Exception {
		super((Frame) GC.getParent());
						
		//添加表格面板到对话框的中央
		CorporationListShowPanel panel=new CorporationListShowPanel();
		panel.refreshData(circle.vertexSet());
		this.add(panel,BorderLayout.CENTER);	
		
		//设置对话框的位置和一些属性
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
		final Rectangle frameBounds = new Rectangle((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2,
				WIDTH, HEIGHT);
		this.setBounds(frameBounds);				
		this.setTitle(circle.getName());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		this.setAlwaysOnTop(true);
		this.setModal(true);
	}

	
}
