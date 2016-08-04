package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2;

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

public class CorporationListShowTable extends JTable {

	private CorporationListShowTableModel model;

	public CorporationListShowTable(CorporationListShowTableModel model) {
		super(model);
		this.model = model;
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableToolkit.fitTableColumns(this);
	}

}

class CorporationListShowTableModel extends javax.swing.table.AbstractTableModel {

	private Vector<Vector> rowData;
	private Vector<String> columnNames;
	private String[] COLUMN_NAMES = { "企业名称", "组织机构代码", "所属地区","权重","贷款银行家数", "贷款余额","被担保贷款余额"/*,"对外提供担保金额"*/,"对外提供担保贷款对应的贷款余额", "不良贷款余额", "不良贷款率（%）", "正常类贷款余额",
			"关注类贷款余额", "次级类贷款余额", "可疑类贷款余额", "损失类贷款余额", "逾期贷款", "逾期30天以内", "逾期31_90天", "逾期91_180天", "逾期181_365天",
			"逾期1年_3年", "逾期3年以上", "银行持有企业债券", "银行持有企业股权", "表外业务余额" ,"资产总额","负债总额"};

	public CorporationListShowTableModel(Set<Corporation> corporations) throws Exception {
		super();
		columnNames = initColumnNames();
		rowData = initRowData(corporations);
	}

	private Vector<Vector> initRowData(Set<Corporation> corporations) throws Exception {
		Vector<Vector> rowData = new Vector<Vector>();
		for (Corporation corp : corporations) {
			Vector<Object> row = new Vector<Object>();
			row.add(corp.getName());// 企业名称
			row.add(corp.getOrgCode());// 企业组织机构代码
			row.add(corp.getRegion().getName());// 所属地区
			row.add(NumberToolkit.format(corp.getWeight()));// 权重
			row.add(corp.getLoanBankCount());// 贷款银行家数
			row.add(NumberToolkit.format(corp.getLoanBalance()));// 贷款余额
			row.add(NumberToolkit.format(corp.getGuaranteedLoanBalance()));// 被担保贷款余额
//			row.add(NumberToolkit.format(corp.getOutGuarantValue()));// 对外提供担保金额
			row.add(NumberToolkit.format(corp.getOutGuaranteedLoanBalance()));// 对外提供担保贷款对应的贷款余额
			row.add(NumberToolkit.format(corp.getBuLiangLoanBalance()));// 不良贷款余额
			row.add(corp.getBuLiangLv());// 不良率
			row.add(NumberToolkit.format(corp.getZhengChangLoanBalance()));// 正常类贷款余额
			row.add(NumberToolkit.format(corp.getGuanZhuLoanBalance()));// 关注类贷款余额
			row.add(NumberToolkit.format(corp.getCiJiLoanBalance()));// 次级类贷款余额
			row.add(NumberToolkit.format(corp.getKeYiLoanBalance()));// 可疑类贷款余额
			row.add(NumberToolkit.format(corp.getSunShiLoanBalance()));// 损失类贷款余额
			row.add(NumberToolkit.format(corp.getYuQiLoanBalance()));// 逾期贷款余额
			row.add(NumberToolkit.format(corp.getYuQi30YiNeiLoanBalance()));// 逾期30以内贷款余额
			row.add(NumberToolkit.format(corp.getYuQi31_90YiNeiLoanBalance()));// 逾期31_90天贷款余额
			row.add(NumberToolkit.format(corp.getYuQi91_180YiNeiLoanBalance()));// 逾期91_180天贷款余额
			row.add(NumberToolkit.format(corp.getYuQi181_365YiNeiLoanBalance()));// 逾期181_365天贷款余额
			row.add(NumberToolkit.format(corp.getYuQi1Year_3YearYiNeiLoanBalance()));// 逾期1年_3年贷款余额
			row.add(NumberToolkit.format(corp.getYuQi3YearYiShangLoanBalance()));// 逾期3年以上贷款余额
			row.add(NumberToolkit.format(corp.getZhaiQuanBalance()));// 银行持有企业债券
			row.add(NumberToolkit.format(corp.getGuQuanBalance()));// 银行持有企业股权
			row.add(NumberToolkit.format(corp.getOffBalance()));// 表外业务余额
			row.add(NumberToolkit.format(corp.getZCZE()));// 资产总额
			row.add(NumberToolkit.format(corp.getFZZE()));// 负债总额
			row.add(corp);// 最后一列用来放Corporation对象本身，该列不会被展示
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
