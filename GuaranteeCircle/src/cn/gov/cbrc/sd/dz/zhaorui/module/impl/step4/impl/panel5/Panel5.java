package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel5;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.GraphicClassify;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.Panel1;

/**
 * 风险分类识别结果展示页面
 * 
 * @author zr
 *
 */
public class Panel5 extends JSplitPane {

	private StringBuilder resultContent;

	private JTextArea resultTextarea;

	private GraphicClassifyShowPanel gcPanel;
	
	private Panel1 bottomPanel;

	public Panel5() {
		super(JSplitPane.VERTICAL_SPLIT, true);
		JPanel topPanel = new JPanel(new BorderLayout());
		resultTextarea = new JTextArea();
		resultTextarea.setLineWrap(true);
		resultTextarea.setEditable(false);
		gcPanel = new GraphicClassifyShowPanel(this);
		topPanel.add(resultTextarea, BorderLayout.NORTH);
		topPanel.add(gcPanel, BorderLayout.CENTER);
		this.setTopComponent(topPanel);
		
		bottomPanel=new Panel1();
		this.setBottomComponent(bottomPanel);
		
		this.setAutoscrolls(true);
		this.setOneTouchExpandable(true);
		this.setDividerSize(10);

		addListeners();
		
		
	}

	private void addListeners() {
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Panel5.this.setDividerLocation(0.5);
			}
			public void componentShown(ComponentEvent e) {
				Panel5.this.setDividerLocation(0.5);
			}
		});
	}

	public void refreshResult(List<Graphic> circles) {
		Map<GraphicClassify, List<Graphic>> classifyGraphicsMap = new HashMap<GraphicClassify, List<Graphic>>();
		for (Graphic g : circles) {
			GraphicClassify classify = g.getRiskClassify();
			if (classifyGraphicsMap.containsKey(classify))
				classifyGraphicsMap.get(classify).add(g);
			else {
				List<Graphic> list = new ArrayList<Graphic>();
				list.add(g);
				classifyGraphicsMap.put(classify, list);
			}
		}
		// 刷新“识别结果”说明文字
		resultContent = new StringBuilder();
		resultContent.append("本次识别出的").append(circles.size()).append("个担保圈共分布在")
				.append(classifyGraphicsMap.keySet().size()).append("个类别。");
		resultTextarea.setText(resultContent.toString());
		// 刷新“识别结果”表格面板
		gcPanel.refreshData(classifyGraphicsMap);
	}

	public Panel1 getBottomPanel() {
		return bottomPanel;
	}

	public JTable getTable() {

		return gcPanel.getTable();
	}
}
