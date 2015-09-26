package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;

public class GraphicShowTable extends JTable {

	private GraphicShowTableModel model;

	public GraphicShowTable(GraphicShowTableModel model) {
		super(model);
		this.model = model;
	}

}

class GraphicShowTableModel extends javax.swing.table.AbstractTableModel {

	private Vector<Vector> rowData;
	private Vector<String> columnNames;
	private String[] COLUMN_NAMES = { "担保圈名称","所属地区", "涉及企业个数",
			"担保圈贷款余额" ,"关注类贷款余额","不良贷款余额","逾期90以内贷款余额","表外业务余额"  };

	public GraphicShowTableModel(List<Graphic> circles) {
		super();
		columnNames = initColumnNames();
		rowData = initRowData(circles);
	}

	private Vector<Vector> initRowData(List<Graphic> circles) {
		Vector<Vector> rowData = new Vector<Vector>();
		for (Graphic circle : circles) {
			Vector<Object> row = new Vector<Object>();
			row.add(circle.getName());// 担保圈名称
			row.add(circle.getRegion().getName());
			row.add(circle.vertexSet().size());// 涉及企业个数
			row.add(Math.round(GraphicToolkit.getLoanBalance(circle)));// 贷款余额
			row.add(Math.round(GraphicToolkit.getGuanZhuLoanBalance(circle)));// 关注类贷款余额
			row.add(Math.round(GraphicToolkit.getBuLiangLoanBalance(circle)));// 不良贷款余额
			row.add(Math.round(GraphicToolkit.getYuQi90YiNeiLoanBalance(circle)));// 逾期90以内贷款余额
			row.add(Math.round(GraphicToolkit.getOffBalance(circle)));// 表外业务余额
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
		if (columnIndex <=1)
			return String.class;
		else
			return Double.class;
	}

}
