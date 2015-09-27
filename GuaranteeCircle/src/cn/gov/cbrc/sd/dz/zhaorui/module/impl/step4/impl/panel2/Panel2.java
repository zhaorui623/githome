package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2;

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
import javax.swing.JTextArea;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.Panel1;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel4.Panel4;

/**
 * 区域分布识别结果展示页面
 * 
 * @author zr
 *
 */
public class Panel2 extends JSplitPane {

	private StringBuilder resultContent;

	private JTextArea resultTextarea;

	private RegionGraphicShowPanel rgsPanel;
	
	private Panel1 bottomPanel;

	public Panel2() {
		super(JSplitPane.VERTICAL_SPLIT, true);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		resultTextarea = new JTextArea();
		resultTextarea.setLineWrap(true);
		resultTextarea.setEditable(false);
		rgsPanel = new RegionGraphicShowPanel(this);
		topPanel.add(resultTextarea, BorderLayout.NORTH);
		topPanel.add(rgsPanel, BorderLayout.CENTER);
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
				Panel2.this.setDividerLocation(0.5);
			}
			public void componentShown(ComponentEvent e) {
				Panel2.this.setDividerLocation(0.5);
			}
		});
		
	}

	public void refreshResult(List<Graphic> circles) {
		Map<Region, List<Graphic>> regionGraphicsMap = new HashMap<Region, List<Graphic>>();
		for (Graphic g : circles) {
			Region region = g.getRegion();
			if (regionGraphicsMap.containsKey(region))
				regionGraphicsMap.get(region).add(g);
			else {
				List<Graphic> list = new ArrayList<Graphic>();
				list.add(g);
				regionGraphicsMap.put(region, list);
			}
		}
		// 刷新“识别结果”说明文字
		resultContent = new StringBuilder();
		resultContent.append("本次识别出的").append(circles.size()).append("个担保圈共分布在")
				.append(regionGraphicsMap.keySet().size()).append("个地区。");
		resultTextarea.setText(resultContent.toString());
		// 刷新“识别结果”表格面板
		rgsPanel.refreshData(regionGraphicsMap);
	}

	public Panel1 getBottomPanel() {
		return bottomPanel;
	}
}
