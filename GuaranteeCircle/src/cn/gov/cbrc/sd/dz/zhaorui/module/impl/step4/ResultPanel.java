package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1.Step1Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3.Step3Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel0.Panel0;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.Panel1;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2.Panel2;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel3.Panel3;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel5.Panel5;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.ExcelToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.FileToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.NumberToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.TableToolkit;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@SuppressWarnings("serial")
public class ResultPanel extends JPanel {

	// public static int hugeCirclesCount;
	// public static int hugeCirclesVertexCount;
	// public static double hugeCirclesLoanBalance;
	public static List<Graphic> hugeCircles;
	private GC GC = null;
	private JTabbedPane tabbedPane;
	private Panel0 panel0;
	private Panel1 panel1;
	private Panel2 panel2;
	private Panel3 panel3;
	private Panel1 panel4;
	private Panel5 panel5;
	private JButton export;

	private List<Graphic> circles;

	private final String TOTALTABLE = "总表";
	private final String TABLE1 = "表1-担保圈清单";
	private final String TABLE2 = "表2-涉圈企业清单";
	private final String TABLE3 = "表3-“区域分布”分析结果";
	private final String TABLE4 = "表4-“重点客户”分析结果";
	private final String TABLE5 = "表5-“风险分类”分析结果";

	public ResultPanel(Step4Module step4Module) {
		super();
		this.GC = step4Module.getGC();
		this.setName(step4Module.getName());

		this.setLayout(new BorderLayout());

		export = new JButton("导出全部结果");
		this.add(export, BorderLayout.NORTH);

		tabbedPane = new JTabbedPane(); // 创建选项卡面板对象
		// 创建面板
		panel0 = new Panel0();
		panel1 = new Panel1();
		panel2 = new Panel2();
		panel3 = new Panel3();
		panel4 = new Panel1();
		panel5 = new Panel5();
		// panel1.setBackground(Color.yellow);
		// panel2.setBackground(Color.blue);
		// panel3.setBackground(Color.green);
		// panel4.setBackground(Color.red);
		// 将标签面板加入到选项卡面板对象上
		tabbedPane.addTab(TOTALTABLE, null, panel0, "Total panel");
		tabbedPane.addTab(TABLE1, null, panel1, "First panel");
		tabbedPane.addTab(TABLE2, null, panel2, "Second panel");
		tabbedPane.addTab(TABLE3, null, panel3, "Thrid panel");
		tabbedPane.addTab(TABLE4, null, panel4, "Fourth panel");
		tabbedPane.addTab(TABLE5, null, panel5, "Fifth panel");

		this.add(tabbedPane, BorderLayout.CENTER);
		this.setBackground(Color.white);

		addListeners();
	}

	public void refreshResult(List<Graphic> circles) throws Exception {
		this.circles = circles;
		if (circles != null) {
			InfoPane.getInstance().info("正在整理报表数据……");
			panel0.refreshResult(circles);
			((Step4Module) GC.getModule("4")).setProgress(1);
			panel1.refreshResult(circles);
			((Step4Module) GC.getModule("4")).setProgress(2);
			panel2.refreshResult(GraphicToolkit.getCorpsSet(circles));
			((Step4Module) GC.getModule("4")).setProgress(3);

			panel3.refreshResult(circles);
			((Step4Module) GC.getModule("4")).setProgress(4);

			List<Graphic> vipCircles = new ArrayList<Graphic>();
			for (Graphic circle : circles)
				if (circle.isVIPGraphic())
					vipCircles.add(circle);
			panel4.refreshResult(vipCircles);
			((Step4Module) GC.getModule("4")).setProgress(5);

			panel5.refreshResult(circles);
			((Step4Module) GC.getModule("4")).setProgress(6);

			// 根据模板生成2016年省局担保圈监测分析表
			generateJCFXTables();

			InfoPane.getInstance().info("报表数据整理完成");

		}
	}

	private void addListeners() {
		export.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					boolean result = doExport();
					if (result == true)
						JOptionPane.showMessageDialog(null, "导出成功", "导出结果", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	protected boolean doExport() throws Exception {
		if (this.circles == null || this.circles.size() == 0)
			return false;
		String reportDate = ((Step1Module) GC.getModule("1")).getReportDate();
		String dir = GC.getOutputDir().getAbsolutePath() + "\\报表\\";
		TableToolkit.exp2Excel(panel0.getTable(), new File(dir + TOTALTABLE + "-" + reportDate + ".xls"));
		TableToolkit.exp2Excel(panel1.getTable(), new File(dir + TABLE1 + "-" + reportDate + ".xls"));
		TableToolkit.exp2Excel(panel2.getTable(), new File(dir + TABLE2 + "-" + reportDate + ".xls"));
		TableToolkit.exp2Excel(panel3.getTable(), new File(dir + TABLE3 + "-" + reportDate + ".xls"));
		TableToolkit.exp2Excel(panel4.getTable(), new File(dir + TABLE4 + "-" + reportDate + ".xls"));
		TableToolkit.exp2Excel(panel5.getTable(), new File(dir + TABLE5 + "-" + reportDate + ".xls"));
		return true;
	}

	private void generateJCFXTables() throws Exception {

		generateJCFXTable1();
		generateJCFXTable2();
	}

	private void generateJCFXTable2() throws Exception {

		File templateFile = new File("2016年省局担保圈监测分析表-表样2.xls");
		File dir = new File(GC.getOutputDir().getAbsolutePath() + "\\报表\\");
		if (dir.exists() == false)
			dir.mkdirs();
		File t1File = new File(dir.getAbsolutePath() + "\\"
				+ templateFile.getName().replaceFirst(".xls", "-" + ((Step1Module) GC.getModule("1")).getReportDate())
				+ ".xls");

		WritableWorkbook book2 = null;
		try {
			book2 = Workbook.createWorkbook(t1File, Workbook.getWorkbook(templateFile));
		} catch (FileNotFoundException e) {
			InfoPane.getInstance().error("无法生成“2016年省局担保圈监测分析表-表样2”，因为未找到模板文件：" + templateFile.getAbsolutePath());
			InfoPane.getInstance().error(e.toString());
			e.printStackTrace();
			((Step4Module) GC.getModule("4")).setProgress(12);
			return;
		}
		WritableSheet sheet1 = book2.getSheet(0);
		String th[] = { "担保圈名称", "所属地区", "涉及企业个数", "贷款行家数", "担保关系条数", "担保圈贷款余额", "担保圈被担保贷款余额", "担保圈资产总额", "担保圈负债总额",
				"担保贷款/（资产总额-负债总额）", "担保圈互保贷款余额", "互保贷款余额/担保圈贷款余额", "核心客户贷款余额", "核心客户贷款占比", "最大三家客户贷款余额", "最大三家客户贷款占比",
				"不良贷款余额", "不良率(%)", "关注类贷款余额", "逾期90以内贷款余额", "表外业务余额" };
		for (int i = 0; i < th.length; i++)
			sheet1.addCell(new Label(i, 0, th[i]));
		int row = 1;
		for (Graphic circle : circles) {
			sheet1.addCell(new Label(0, row, circle.getName()));// 担保圈名称
			sheet1.addCell(new Label(1, row, circle.getRegion() == null ? "未知地区" : circle.getRegion().getName()));// 所属地区
			ExcelToolkit.addNumberCell(sheet1, 2, row, circle.vertexSet().size());// 涉及企业个数
			ExcelToolkit.addNumberCell(sheet1, 4, row, circle.edgeSet().size());// 担保关系条数
			double circleLoanBlance = GraphicToolkit.getLoanBalance(circle);
			ExcelToolkit.addNumberCell(sheet1, 5, row, circleLoanBlance);// 担保圈贷款余额
			ExcelToolkit.addNumberCell(sheet1, 6, row, GraphicToolkit.getGuaranteedLoanBalance(circle));// 担保圈被担保贷款余额
			ExcelToolkit.addNumberCell(sheet1, 7, row, GraphicToolkit.getZCZE(circle));// 担保圈资产总额
			ExcelToolkit.addNumberCell(sheet1, 8, row, GraphicToolkit.getFZZE(circle));// 担保圈负债总额
			// ExcelToolkit.addNumberCell(sheet1,8, row,
			// GraphicToolkit.getMutuallyLoanBalance(circle));//担保圈互保贷款余额
			double hxqyLoanBalance = GraphicToolkit.getHxqyLoanBalance(circle);
			ExcelToolkit.addNumberCell(sheet1, 12, row, hxqyLoanBalance);// 核心客户贷款余额
			ExcelToolkit.addNumberCell(sheet1, 13, row,
					circleLoanBlance == 0 ? 0 : (hxqyLoanBalance * 1.0 / circleLoanBlance * 100));// 核心客户贷款占比
			double max3LoanBalance = GraphicToolkit.getMaxNLoanBalanceVertexLoanBlance(circle, 3);
			ExcelToolkit.addNumberCell(sheet1, 14, row, max3LoanBalance);// 最大三家客户贷款余额
			ExcelToolkit.addNumberCell(sheet1, 15, row,
					circleLoanBlance == 0 ? 0 : (max3LoanBalance * 1.0 / circleLoanBlance * 100));// 最大三家客户贷款占比
			ExcelToolkit.addNumberCell(sheet1, 16, row, GraphicToolkit.getBuLiangLoanBalance(circle));// 不良贷款余额
			ExcelToolkit.addNumberCell(sheet1, 17, row, GraphicToolkit.getBuLiangLv(circle));// 不良率
			ExcelToolkit.addNumberCell(sheet1, 18, row, GraphicToolkit.getGuanZhuLoanBalance(circle));// 关注类贷款余额
			ExcelToolkit.addNumberCell(sheet1, 19, row, GraphicToolkit.getYuQi90YiNeiLoanBalance(circle));// 逾期90以内贷款余额
			ExcelToolkit.addNumberCell(sheet1, 20, row, GraphicToolkit.getOffBalance(circle));// 表外业务余额
			row++;
		}
		((Step4Module) GC.getModule("4")).setProgress(12);

		book2.write();
		book2.close();
	}

	private void generateJCFXTable1() throws Exception {

		// 2016年省局担保圈监测分析表-表样1
		File templateFile = new File("2016年省局担保圈监测分析表-表样1.xls");
		File dir = new File(GC.getOutputDir().getAbsolutePath() + "\\报表\\");
		if (dir.exists() == false)
			dir.mkdirs();
		File t1File = new File(dir.getAbsolutePath() + "\\"
				+ templateFile.getName().replaceFirst(".xls", "-" + ((Step1Module) GC.getModule("1")).getReportDate())
				+ ".xls");
		WritableWorkbook book1 = null;
		try {
			book1 = Workbook.createWorkbook(t1File, Workbook.getWorkbook(templateFile));
		} catch (FileNotFoundException e) {
			InfoPane.getInstance().error("无法生成“2016年省局担保圈监测分析表-表样1”，因为未找到模板文件：" + templateFile.getAbsolutePath());
			InfoPane.getInstance().error(e.toString());
			e.printStackTrace();
			((Step4Module) GC.getModule("4")).setProgress(11);
			return;
		}

		// 表1-山东银监局辖区担保圈时序分析表
		WritableSheet sheet1 = book1.getSheet(0);
		int rowCount = sheet1.getRows();
		for (int row = 0; row < rowCount; row++) {
			String reportDate = sheet1.getCell(0, row).getContents();
			if (reportDate.equals(((Step1Module) GC.getModule("1")).getReportDate())) {
				// 担保圈个数
				ExcelToolkit.addNumberCell(sheet1, 1, row, circles.size());
				// 担保圈客户数
				ExcelToolkit.addNumberCell(sheet1, 2, row, GraphicToolkit.getCorpsSet(circles).size());
				// 担保圈贷款余额
				ExcelToolkit.addNumberCell(sheet1, 3, row, GraphicToolkit.getLoanBalance(circles));
				// 不良担保圈个数
				ExcelToolkit.addNumberCell(sheet1, 5, row, GraphicToolkit.getBuliangCircles(circles).size());
				// 担保圈不良客户数
				ExcelToolkit.addNumberCell(sheet1, 6, row, GraphicToolkit.getBuliangCorpsSet(circles).size());
				// 不良担保圈涉贷余额
				ExcelToolkit.addNumberCell(sheet1, 7, row, GraphicToolkit.getBuliangCriclesLoanBalance(circles));
				// 担保圈不良贷款余额
				ExcelToolkit.addNumberCell(sheet1, 8, row, GraphicToolkit.getBuLiangLoanBalance(circles));
			}
		}
		((Step4Module) GC.getModule("4")).setProgress(7);
		// 表2-山东银监局辖区担保圈类型分析表
		WritableSheet sheet2 = book1.getSheet(1);
		rowCount = sheet2.getRows();
		for (int row = 0; row < rowCount; row++) {
			String reportDate = sheet2.getCell(0, row).getContents();
			if (reportDate.equals(((Step1Module) GC.getModule("1")).getReportDate())) {
				List<Graphic> duliCircles = GraphicToolkit.getDuliCircles(circles);
				List<Graphic> hxqyCircles = GraphicToolkit.getHxqyCircles(circles);
				// 超大担保圈个数
				ExcelToolkit.addNumberCell(sheet2, 1, row, hugeCircles.size());
				// 超大担保圈客户数
				ExcelToolkit.addNumberCell(sheet2, 2, row, GraphicToolkit.getCorpsSet(hugeCircles).size());
				// 超大担保圈贷款余额
				ExcelToolkit.addNumberCell(sheet2, 3, row, GraphicToolkit.getLoanBalance(hugeCircles));
				for(Graphic hg:hugeCircles)
				{
					InfoPane.getInstance().info("======================超大担保圈："+hg.getName()+"=========================");
					InfoPane.getInstance().info("客户数:"+hg.vertexSet().size());
					InfoPane.getInstance().info("贷款余额:"+GraphicToolkit.getLoanBalance(hg));
					InfoPane.getInstance().info("不良余额:"+GraphicToolkit.getBuLiangLoanBalance(hg));
					InfoPane.getInstance().info("不良客户数:"+GraphicToolkit.getBuliangCorpsSet(hg).size());
					InfoPane.getInstance().info("不良率:"+GraphicToolkit.getBuLiangLv(hg));
					InfoPane.getInstance().info("关注贷款余额:"+GraphicToolkit.getGuanZhuLoanBalance(hg));
					InfoPane.getInstance().info("逾期贷款余额:"+GraphicToolkit.getYuQiLoanBalance(hg));
					Set<Corporation> vs=hg.vertexSet();
					for(Corporation v:vs){
						InfoPane.getInstance().info(v.getOrgCode()+"\t"+v.getName()+"\t"+v.getRegion());						
					}
					InfoPane.getInstance().info("----------------------超大担保圈："+hg.getName()+"---------------------------");
				}
				// 独立担保圈个数
				ExcelToolkit.addNumberCell(sheet2, 5, row, duliCircles.size());
				// 独立担保圈客户数
				ExcelToolkit.addNumberCell(sheet2, 6, row, GraphicToolkit.getCorpsSet(duliCircles).size());
				// 独立担保圈贷款余额
				ExcelToolkit.addNumberCell(sheet2, 7, row, GraphicToolkit.getLoanBalance(duliCircles));
				// 核心企业担保圈个数
				ExcelToolkit.addNumberCell(sheet2, 9, row, hxqyCircles.size());
				// 核心企业担保圈客户数
				ExcelToolkit.addNumberCell(sheet2, 10, row, GraphicToolkit.getCorpsSet(hxqyCircles).size());
				// 核心企业担保圈贷款余额
				ExcelToolkit.addNumberCell(sheet2, 11, row, GraphicToolkit.getLoanBalance(hxqyCircles));
			}
		}
		((Step4Module) GC.getModule("4")).setProgress(8);

		// 表3-山东银监局辖区担保圈规模分析表
		WritableSheet sheet3 = book1.getSheet(2);
		String[] fuctions = { "getCirclesByLoanBalance", "getCirclesByVertexCount", "getCirclesByEdgeCount" };
		int rowStep = 5;
		double loanBalanceScale[][][] = {
				{ { 1000000, Double.MAX_VALUE }, { 300000, 1000000 }, { 100000, 300000 }, { 10000, 100000 },
						{ -1, 10000 } },
				{ { 100, Double.MAX_VALUE }, { 50, 100 }, { 20, 50 }, { 10, 20 }, { -1, 10 } },
				{ { 200, Double.MAX_VALUE }, { 100, 200 }, { 50, 100 }, { 20, 50 }, { -1, 20 } } };
		for (int j = 0; j < fuctions.length; j++) {
			for (int i = 0; i < loanBalanceScale[j].length; i++) {
				List<Graphic> rowCircles = (List<Graphic>) GraphicToolkit.class
						.getMethod(fuctions[j], List.class, Double.class, Double.class)
						.invoke(null, circles, loanBalanceScale[j][i][0], loanBalanceScale[j][i][1]);
				int row = i + 3 + rowStep * j;
				// 担保圈个数
				ExcelToolkit.addNumberCell(sheet3, 2, row, rowCircles.size());
				// 担保圈客户数
				ExcelToolkit.addNumberCell(sheet3, 3, row, GraphicToolkit.getCorpsSet(rowCircles).size());
				// 担保圈贷款余额
				ExcelToolkit.addNumberCell(sheet3, 4, row, GraphicToolkit.getLoanBalance(rowCircles));
				// 不良担保圈个数
				ExcelToolkit.addNumberCell(sheet3, 6, row, GraphicToolkit.getBuliangCircles(rowCircles).size());
				// 担保圈不良客户数
				ExcelToolkit.addNumberCell(sheet3, 7, row, GraphicToolkit.getBuliangCorpsSet(rowCircles).size());
				// 不良担保圈涉贷余额
				ExcelToolkit.addNumberCell(sheet3, 8, row, GraphicToolkit.getBuliangCriclesLoanBalance(rowCircles));
				// 担保圈不良贷款余额
				ExcelToolkit.addNumberCell(sheet3, 9, row, GraphicToolkit.getBuLiangLoanBalance(rowCircles));
			}
		}
		((Step4Module) GC.getModule("4")).setProgress(9);
		// 表4-山东银监局辖区担保圈分地区分析表
		WritableSheet sheet4 = book1.getSheet(3);
		rowCount = sheet4.getRows();
		for (int row = 3; row <= rowCount + 2; row++) {
			String regionName = sheet4.getCell(0, row).getContents();
			if (row == rowCount) {
				regionName = "青岛";
				sheet4.addCell(new Label(0, row, regionName));
			}
			if (row == rowCount + 1) {
				regionName = "省外";
				sheet4.addCell(new Label(0, row, regionName));
			}
			if (row == rowCount + 2) {
				regionName = "未知";
				sheet4.addCell(new Label(0, row, regionName));
			}
			List<Graphic> regionCircles = GraphicToolkit.getCirclesByRegionName(circles, regionName);
			// 担保圈个数
			ExcelToolkit.addNumberCell(sheet4, 1, row, regionCircles.size());
			// 担保圈客户数
			ExcelToolkit.addNumberCell(sheet4, 2, row, GraphicToolkit.getCorpsSet(regionCircles).size());
			// 担保圈贷款余额
			ExcelToolkit.addNumberCell(sheet4, 3, row, GraphicToolkit.getLoanBalance(regionCircles));
			// 不良担保圈个数
			ExcelToolkit.addNumberCell(sheet4, 5, row, GraphicToolkit.getBuliangCircles(regionCircles).size());
			// 担保圈不良客户数
			ExcelToolkit.addNumberCell(sheet4, 6, row, GraphicToolkit.getBuliangCorpsSet(regionCircles).size());
			// 不良担保圈涉贷余额
			ExcelToolkit.addNumberCell(sheet4, 7, row, GraphicToolkit.getBuliangCriclesLoanBalance(regionCircles));
			// 担保圈不良贷款余额
			ExcelToolkit.addNumberCell(sheet4, 8, row, GraphicToolkit.getBuLiangLoanBalance(regionCircles));
		}
		((Step4Module) GC.getModule("4")).setProgress(10);
		// 表5-山东银监局辖区担保圈分地区类型分析表
		WritableSheet sheet5 = book1.getSheet(4);
		rowCount = sheet5.getRows();
		for (int row = 4; row <= rowCount + 2; row++) {
			String regionName = sheet5.getCell(0, row).getContents();
			if (row == rowCount) {
				regionName = "青岛";
				sheet5.addCell(new Label(0, row, regionName));
			}
			if (row == rowCount + 1) {
				regionName = "省外";
				sheet5.addCell(new Label(0, row, regionName));
			}
			if (row == rowCount + 2) {
				regionName = "未知";
				sheet5.addCell(new Label(0, row, regionName));
			}
			List<Graphic> regionCircles = GraphicToolkit.getCirclesByRegionName(circles, regionName);
			List<Graphic> regionHugeCircles = GraphicToolkit.getCirclesByRegionName(hugeCircles, regionName);
			List<Graphic> duliCircles = GraphicToolkit.getDuliCircles(regionCircles);
			List<Graphic> hxqyCircles = GraphicToolkit.getHxqyCircles(regionCircles);
			// 超大担保圈个数
			ExcelToolkit.addNumberCell(sheet5, 1, row, regionHugeCircles.size());
			// 超大担保圈客户数
			ExcelToolkit.addNumberCell(sheet5, 2, row, GraphicToolkit.getCorpsSet(regionHugeCircles).size());
			// 超大担保圈贷款余额
			ExcelToolkit.addNumberCell(sheet5, 3, row, GraphicToolkit.getLoanBalance(regionHugeCircles));
			// 独立担保圈个数
			ExcelToolkit.addNumberCell(sheet5, 5, row, duliCircles.size());
			// 独立担保圈客户数
			ExcelToolkit.addNumberCell(sheet5, 6, row, GraphicToolkit.getCorpsSet(duliCircles).size());
			// 独立担保圈贷款余额
			ExcelToolkit.addNumberCell(sheet5, 7, row, GraphicToolkit.getLoanBalance(duliCircles));
			// 核心企业担保圈个数
			ExcelToolkit.addNumberCell(sheet5, 9, row, hxqyCircles.size());
			// 核心企业担保圈客户数
			ExcelToolkit.addNumberCell(sheet5, 10, row, GraphicToolkit.getCorpsSet(hxqyCircles).size());
			// 核心企业担保圈贷款余额
			ExcelToolkit.addNumberCell(sheet5, 11, row, GraphicToolkit.getLoanBalance(hxqyCircles));

		}
		((Step4Module) GC.getModule("4")).setProgress(11);

		book1.write();
		book1.close();
	}

}
