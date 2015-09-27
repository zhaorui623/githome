package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel4;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.Panel1;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2.Panel2;
import cn.gov.cbrc.sd.dz.zhaorui.model.GraphicClassify;

public class GraphicClassifyShowPanel extends JScrollPane {

	GrpahicClassifyShowTable table;

	Panel4 panel4;

	public GraphicClassifyShowPanel(Panel4 panel4) {
		super();
		this.setAutoscrolls(true);
		this.panel4 = panel4;
		addListeners();
	}

	private void addListeners() {
		if (table != null) {
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					int row = table.getSelectedRow();
					int col = table.getColumnCount();
					if (row >= 0) {
						row = table.convertRowIndexToModel(row);
						List<Graphic> circles = (List<Graphic>) table.getModel().getValueAt(row, col);
						if (panel4 != null) {
							Panel1 bottomPanel = panel4.getBottomPanel();
							if (bottomPanel != null)
								bottomPanel.refreshResult(circles);
						}
					}
				}
			});
		}
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
		addListeners();
		this.setViewportView(table);
		this.repaint();
	}

}
