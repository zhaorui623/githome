package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel4;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.model.GraphicClassify;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;

public class GrpahicClassifyShowTable extends JTable {

	private GraphicClassifyShowTableModel model;

	public GrpahicClassifyShowTable(GraphicClassifyShowTableModel model) {
		super(model);
		this.model = model;
	}

}

class GraphicClassifyShowTableModel extends javax.swing.table.AbstractTableModel {

	private Vector<Vector> rowData;
	private Vector<String> columnNames;
	private String[] COLUMN_NAMES = { "类别名称", "担保圈个数", "涉及企业个数",
			"担保圈贷款余额" ,"关注类贷款余额","不良贷款余额",/*"逾期90以内贷款余额","逾期90以上贷款余额",*/"表外业务余额"  };

	public GraphicClassifyShowTableModel(Map<GraphicClassify, List<Graphic>> classifyGraphicsMap) {
		super();
		columnNames = initColumnNames();
		rowData = initRowData(classifyGraphicsMap);
	}

	private Vector<Vector> initRowData(Map<GraphicClassify, List<Graphic>> classifyGraphicsMap) {
		Vector<Vector> rowData = new Vector<Vector>();
		Set<GraphicClassify> riskClassifySet = classifyGraphicsMap.keySet();
		for (GraphicClassify riskClassify : riskClassifySet) {
			Vector<Object> row = new Vector<Object>();
			List<Graphic> graphics = classifyGraphicsMap.get(riskClassify);
			row.add(riskClassify.getName());// 类别名称
			row.add(graphics.size());// 担保圈个数
			row.add(GraphicToolkit.getVertexSet(graphics).size());// 涉及企业个数
			row.add(Math.round(GraphicToolkit.getLoanBalance(graphics)));// 贷款余额
			row.add(Math.round(GraphicToolkit.getGuanZhuLoanBalance(graphics)));// 关注类贷款余额
			row.add(Math.round(GraphicToolkit.getBuLiangLoanBalance(graphics)));// 不良贷款余额
//			row.add(Math.round(GraphicToolkit.getYuQi90YiNeiLoanBalance(graphics)));// 逾期90以内贷款余额
//			row.add(Math.round(GraphicToolkit.getYuQi90YiShangLoanBalance(graphics)));// 逾期90以上贷款余额
			row.add(Math.round(GraphicToolkit.getOffBalance(graphics)));// 表外业务余额
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
		if (columnIndex == 0)
			return String.class;
		else
			return Double.class;
	}

}
