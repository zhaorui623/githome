package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.GraphicListShowPanel;

public class Panel2 extends JPanel {


	private StringBuilder resultContent;
	
	private JTextArea resultTextarea;
	
	private CorporationListShowPanel dataPanel;

	public Panel2() {
		super(new BorderLayout());
		resultTextarea=new JTextArea();
		resultTextarea.setLineWrap(true);
		resultTextarea.setEditable(false);
		dataPanel=new CorporationListShowPanel();
		this.add(resultTextarea,BorderLayout.NORTH);
		this.add(dataPanel, BorderLayout.CENTER);
	}

	public void refreshResult(Set<Corporation> corps) throws Exception {
		//刷新“识别结果”说明文字
		resultContent=new StringBuilder();
		resultContent.append("本次识别出涉圈企业").append(corps.size()).append("个。");
		resultTextarea.setText(resultContent.toString());
		//刷新“识别结果”表格面板
		dataPanel.refreshData(corps);
	}

	public JTable getTable() {
		
		return dataPanel.getTable();
	}
}
