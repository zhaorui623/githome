package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel4;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.model.GraphicClassify;

public class GraphicClassifyShowPanel extends JScrollPane {

	static GrpahicClassifyShowTable table;

	public GraphicClassifyShowPanel() {
		super();
	}

	/**
	 * 根据传入的 “分类->担保圈列表”构造“结果展示表”
	 * 
	 * @param classifyGraphicsMap
	 */
	public void refreshData(Map<GraphicClassify, List<Graphic>> classifyGraphicsMap) {
		GraphicClassifyShowTableModel model = new GraphicClassifyShowTableModel(classifyGraphicsMap);
		table = new GrpahicClassifyShowTable(model);
		table.setRowSorter(new TableRowSorter<TableModel>(model));
		this.setViewportView(table);
		this.repaint();
	}

}
