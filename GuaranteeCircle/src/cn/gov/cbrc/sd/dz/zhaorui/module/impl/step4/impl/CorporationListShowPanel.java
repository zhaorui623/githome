package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;

public class CorporationListShowPanel extends JScrollPane {

	static CorporationListShowTable table;

	/**
	 * 根据传入的 “企业列表”构造“担保圈内企业展示表”
	 * @param set
	 * @throws Exception 
	 */
	public CorporationListShowPanel(Set<Corporation> corporations) throws Exception {
		CorporationListShowTableModel model = new CorporationListShowTableModel(corporations);
		table = new CorporationListShowTable(model);
		table.setRowSorter(new TableRowSorter<TableModel>(model));
		this.setViewportView(table);
		this.setAutoscrolls(true);
//		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.repaint();
	}
}
