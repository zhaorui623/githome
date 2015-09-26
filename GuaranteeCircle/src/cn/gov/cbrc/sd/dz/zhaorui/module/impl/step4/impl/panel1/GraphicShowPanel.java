package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;

public class GraphicShowPanel extends JScrollPane {

	static GraphicShowTable table;

	public GraphicShowPanel() {
		super();
	}

	/**
	 * 根据传入的 “地区->担保圈列表”构造“结果展示表”
	 * 
	 * @param regionGraphicsMap
	 */
	public void refreshData(List<Graphic> circles) {
		GraphicShowTableModel model = new GraphicShowTableModel(circles);
		table = new GraphicShowTable(model);
		table.setRowSorter(new TableRowSorter<TableModel>(model));
		this.setViewportView(table);
		this.repaint();
	}

}
