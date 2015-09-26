package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;

/**
 * 区域分布识别结果展示页面
 * 
 * @author zr
 *
 */
public class Panel2 extends JPanel {

	private StringBuilder resultContent;
	
	private JTextArea resultTextarea;
	
	private RegionGraphicShowPanel rgsPanel;

	public Panel2() {
		super(new BorderLayout());
		resultTextarea=new JTextArea();
		resultTextarea.setLineWrap(true);
		resultTextarea.setEditable(false);
		rgsPanel=new RegionGraphicShowPanel();
		this.add(resultTextarea,BorderLayout.NORTH);
		this.add(rgsPanel, BorderLayout.CENTER);
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
		//刷新“识别结果”说明文字
		resultContent=new StringBuilder();
		resultContent.append("本次识别出的").append(circles.size()).append("个担保圈共分布在").append(regionGraphicsMap.keySet().size()).append("个地区。");
		resultTextarea.setText(resultContent.toString());
		//刷新“识别结果”表格面板
		rgsPanel.refreshData(regionGraphicsMap);
	}
}
