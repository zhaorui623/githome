package cn.gov.cbrc.sd.dz.zhaorui.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Risk;
import cn.gov.cbrc.sd.dz.zhaorui.model.GraphicClassify;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.XMLToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.model.Scale;

/**
 * 从风险分类维度分析
 * 
 * @author zr
 *
 */
public class GraphicClassifyAnalysis {

	private Map<String, GraphicClassify> classifyMap;

	private Scale scaleA, scaleB, scaleC;

	public GraphicClassifyAnalysis() throws Exception {
		super();
		this.classifyMap = new HashMap<String, GraphicClassify>();
		Document doc = Config.getDoc();
		Element scaleAElement = XMLToolkit.getElementById(doc, "28");
		Element scaleBElement = XMLToolkit.getElementById(doc, "29");
		Element scaleCElement = XMLToolkit.getElementById(doc, "30");
		String scaleALevel = scaleAElement.getAttribute(Config.LEVEL_ATTRIBUTE);
		String scaleBLevel = scaleBElement.getAttribute(Config.LEVEL_ATTRIBUTE);
		String scaleCLevel = scaleCElement.getAttribute(Config.LEVEL_ATTRIBUTE);
		int scaleBLoanBalancefloor = Integer.parseInt(scaleBElement.getAttribute(Config.LOAN_BALANCE_FLOOR_ATTRIBUTE))
				* 10000;
		int scaleBLoanBalanceceilling = Integer
				.parseInt(scaleBElement.getAttribute(Config.LOAN_BALANCE_CEILLING_ATTRIBUTE)) * 10000;
		scaleA = new Scale(scaleALevel, 0, scaleBLoanBalancefloor);
		scaleB = new Scale(scaleBLevel, scaleBLoanBalancefloor, scaleBLoanBalanceceilling);
		scaleC = new Scale(scaleCLevel, scaleBLoanBalanceceilling, Integer.MAX_VALUE);

	}

	public void analysisRiskClassify(Graphic graphic) {
		// 贷款余额
		double loanBalance = GraphicToolkit.getLoanBalance(graphic);
		// 不良贷款余额
		double buLiangLoanBalance = GraphicToolkit.getBuLiangLoanBalance(graphic);
		// 关注贷款余额
		double guanZhuLoanBalance = GraphicToolkit.getGuanZhuLoanBalance(graphic);
		// 逾期90天以内的贷款余额
		double yuQi90YiNeiLoanBalance = GraphicToolkit.getYuQi90YiNeiLoanBalance(graphic);
		// 逾期90天以上的贷款余额
		double yuQi90YiShangLoanBalance = GraphicToolkit.getYuQi90YiShangLoanBalance(graphic);

		// 确定规模级别
		Scale scale;
		if (loanBalance <= scaleA.getCeilling())
			scale = scaleA;
		else if (loanBalance > scaleB.getFloor() && loanBalance <= scaleB.getCeilling())
			scale = scaleB;
		else
			scale = scaleC;

//		// 确定风险级别(按逾期天数）
//		Risk risk;
//		if (yuQi90YiShangLoanBalance > 0)
//			risk = new Risk("3");
//		else if (yuQi90YiNeiLoanBalance > 0)
//			risk = new Risk("2");
//		else
//			risk = new Risk("1");
		
		// 确定风险级别(按五级分类）
		Risk risk;
		if (buLiangLoanBalance > 0)
			risk = new Risk("3");
		else if (guanZhuLoanBalance > 0)
			risk = new Risk("2");
		else
			risk = new Risk("1");

		// 最终确定分类
		GraphicClassify classify = new GraphicClassify(scale, risk);
		graphic.setRiskClassify(classify);
	}

}
