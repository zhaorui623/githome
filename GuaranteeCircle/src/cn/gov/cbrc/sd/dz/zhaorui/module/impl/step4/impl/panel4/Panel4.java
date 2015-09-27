package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel4;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.GraphicClassify;

/**
 * 风险分类识别结果展示页面
 * 
 * @author zr
 *
 */
public class Panel4 extends JPanel {

	private StringBuilder resultContent;
	
	private JTextArea resultTextarea;
	
	private GraphicClassifyShowPanel rgsPanel;

	public Panel4() {
		super(new BorderLayout());
		resultTextarea=new JTextArea();
		resultTextarea.setLineWrap(true);
		resultTextarea.setEditable(false);
		rgsPanel=new GraphicClassifyShowPanel();
		this.add(resultTextarea,BorderLayout.NORTH);
		this.add(rgsPanel, BorderLayout.CENTER);
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
		//刷新“识别结果”说明文字
		resultContent=new StringBuilder();
		resultContent.append("本次识别出的").append(circles.size()).append("个担保圈共分布在").append(classifyGraphicsMap.keySet().size()).append("个类别。");
		resultTextarea.setText(resultContent.toString());
		//刷新“识别结果”表格面板
		rgsPanel.refreshData(classifyGraphicsMap);
	}
}
