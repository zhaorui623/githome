package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.ResultPanel;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.NumberToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.TableToolkit;

public class GraphicListShowTable extends JTable {

	private GraphicListShowTableModel model;

	public GraphicListShowTable(GraphicListShowTableModel model) {
		super(model);
		this.model = model;
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableToolkit.fitTableColumns(this);
	}

}

class GraphicListShowTableModel extends javax.swing.table.AbstractTableModel {

	private Vector<Vector> rowData;
	private Vector<String> columnNames;
	private String[] COLUMN_NAMES = { "担保圈名称", "所属地区", "风险分类", "涉及企业个数", "担保圈贷款余额","担保圈被担保贷款余额",  "不良贷款余额","不良率(%)","关注类贷款余额", "逾期90以内贷款余额",
			"表外业务余额" };

	public GraphicListShowTableModel(List<Graphic> circles) {
		super();
		columnNames = initColumnNames();
		rowData = initRowData(circles);
	}

	private Vector<Vector> initRowData(List<Graphic> circles) {
		Vector<Vector> rowData = new Vector<Vector>();
		for (Graphic circle : circles) {
			Vector<Object> row = new Vector<Object>();
			row.add(circle.getName());// 担保圈名称
			if (circle.getRegion() == null)
				row.add("未知地区");
			else
				row.add(circle.getRegion().getName());// 所属地区
			if (circle.getRiskClassify() == null)
				row.add("未知分类");
			else
				row.add(circle.getRiskClassify().getName());// 风险分类
			row.add(circle.vertexSet().size());// 涉及企业个数
			row.add(NumberToolkit.format(GraphicToolkit.getLoanBalance(circle)));// 贷款余额
			row.add(NumberToolkit.format(GraphicToolkit.getGuaranteedLoanBalance(circle)));// 被担保贷款余额
			row.add(NumberToolkit.format(GraphicToolkit.getBuLiangLoanBalance(circle)));// 不良贷款余额
			row.add(GraphicToolkit.getBuLiangLv(circle));// 不良率
			row.add(NumberToolkit.format(GraphicToolkit.getGuanZhuLoanBalance(circle)));// 关注类贷款余额
			row.add(NumberToolkit.format(GraphicToolkit.getYuQi90YiNeiLoanBalance(circle)));// 逾期90以内贷款余额
			row.add(NumberToolkit.format(GraphicToolkit.getOffBalance(circle)));// 表外业务余额
			row.add(circle);// 最后一列放Graphic对象本身，该列不会被显示
			rowData.add(row);
		}
		return rowData;
	}

	private Vector<String> initColumnNames() {
		Vector<String> columnNames = new Vector<String>();
		for (String name : COLUMN_NAMES)
			columnNames.add(name);
		return columnNames;
	}

	@Override
	public int getRowCount() {
		return rowData.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return rowData.get(rowIndex).get(columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex <= 2)
			return String.class;
		else
			return Double.class;
	}

}
