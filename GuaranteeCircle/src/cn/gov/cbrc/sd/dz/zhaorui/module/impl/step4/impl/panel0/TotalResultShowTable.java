package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel0;

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

public class TotalResultShowTable extends JTable {

	private GraphicListShowTableModel model;

	public TotalResultShowTable(GraphicListShowTableModel model) {
		super(model);
		this.model = model;
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableToolkit.fitTableColumns(this);
	}

}

class GraphicListShowTableModel extends javax.swing.table.AbstractTableModel {

	private Vector<Vector> rowData;
	private Vector<String> columnNames;
	private String[] COLUMN_NAMES = { "担保圈名称", "所属地区", "风险分类", "涉及企业个数", "担保圈贷款余额", "担保圈被担保贷款余额","不良贷款余额", "不良率(%)", "企业名称",
			"贷款银行家数", "贷款余额","被担保贷款余额", "不良贷款余额", "不良率(%)" };

	public GraphicListShowTableModel(List<Graphic> circles) {
		super();
		columnNames = initColumnNames();
		rowData = initRowData(circles);
	}

	private Vector<Vector> initRowData(List<Graphic> circles) {
		Vector<Vector> rowData = new Vector<Vector>();
		for (Graphic circle : circles) {
			for (Corporation corp : circle.vertexSet()) {
				Vector<Object> row = new Vector<Object>();
				row.add(circle.getName());// 担保圈名称
				row.add(circle.getRegion()==null?"未知地区":circle.getRegion().getName());// 所属地区
				row.add(circle.getRiskClassify().getName());// 风险分类
				row.add(circle.vertexSet().size());// 涉及企业个数
				row.add(NumberToolkit.format(GraphicToolkit.getLoanBalance(circle)));// 贷款余额
				row.add(NumberToolkit.format(GraphicToolkit.getGuaranteedLoanBalance(circle)));// 被担保贷款余额
				row.add(NumberToolkit.format(GraphicToolkit.getBuLiangLoanBalance(circle)));// 不良贷款余额
				row.add(GraphicToolkit.getBuLiangLv(circle));// 不良率
				row.add(corp.getName());// 企业名称
				row.add(corp.getLoanBankCount());// 贷款银行家数
				row.add(NumberToolkit.format(corp.getLoanBalance()));// 贷款余额
				row.add(NumberToolkit.format(corp.getGuaranteedLoanBalance()));// 被担保贷款余额
				row.add(NumberToolkit.format(corp.getBuLiangLoanBalance()));// 不良贷款余额
				row.add(corp.getBuLiangLv());// 不良率
				row.add(corp);// 最后一列放Vertex对象本身，该列不会被显示
				rowData.add(row);
			}
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
		if (columnIndex <= 2||columnIndex==8)
			return String.class;
		else
			return Double.class;
	}

}
