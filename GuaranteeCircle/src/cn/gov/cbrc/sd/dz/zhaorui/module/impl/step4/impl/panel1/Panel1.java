package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel3.RegionGraphicShowPanel;

public class Panel1 extends JPanel {
	private StringBuilder resultContent;
	
	private JTextArea resultTextarea;
	
	private GraphicListShowPanel gsPanel;

	public Panel1() {
		super(new BorderLayout());
		resultTextarea=new JTextArea();
		resultTextarea.setLineWrap(true);
		resultTextarea.setEditable(false);
		gsPanel=new GraphicListShowPanel();
		this.add(resultTextarea,BorderLayout.NORTH);
		this.add(gsPanel, BorderLayout.CENTER);
	}

	public void refreshResult(List<Graphic> circles) {
		//刷新“识别结果”说明文字
		resultContent=new StringBuilder();
		resultContent.append("本次识别出的").append(circles.size()).append("个担保圈。");
		resultTextarea.setText(resultContent.toString());
		//刷新“识别结果”表格面板
		gsPanel.refreshData(circles);
	}
}
