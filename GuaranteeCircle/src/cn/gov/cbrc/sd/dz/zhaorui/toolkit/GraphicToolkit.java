package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.util.NumberToTextConverter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.w3c.dom.Element;

import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;

public class GraphicToolkit {

	public static Set<Corporation> getVertexSet(List<Graphic> graphics) {
		Set<Corporation> set = null;
		if (graphics != null) {
			set = new HashSet<Corporation>();
			for (Graphic g : graphics) {
				set.addAll(g.vertexSet());
			}
		}
		return set;
	}

	public static boolean f = false;

	public static double getLoanBalance(List<Graphic> graphics) {
		double loanBalance = 0;
		// try {
		// File file = new
		// File("D:\\TDDOWNLOAD\\eclipse\\githome\\GuaranteeCircle\\20160625134525\\报表\\x.txt");
		// if (!file.exists()) {
		// file.createNewFile();
		// }
		// FileWriter fileWritter = new FileWriter(file, true);
		// BufferedWriter bufferWritter = new BufferedWriter(fileWritter);

		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.LOAN_BALANCE_COL);

				// if (f)
				// bufferWritter.write(String.valueOf(value) + "\n");
				loanBalance = loanBalance + value;
			}
			// bufferWritter.close();
		}
		// } catch (Exception e) {
		//
		// }
		return loanBalance;
	}

	public static double getGuaranteedLoanBalance(List<Graphic> graphics) {
		double guaranteedLoanBalance = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.GUARANTEED_LOAN_BALANCE_COL);
				guaranteedLoanBalance = guaranteedLoanBalance + value;
			}
		}
		return guaranteedLoanBalance;
	}

	public static double getGuanZhuLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.GUANZHU_LOAN_BALANCE_COL);
				result = result + value;
			}
		}
		return result;
	}

	public static double getBuLiangLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.BULIANG_LOAN_BALANCE_COL);
				result = result + value;
			}
		}
		return result;
	}

	public static double getYuQi90YiNeiLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value1 = corp.getDoubleValue(Corporation.YUQI_30DAY_YINEI_LOAN_BALANCE_COL);
				double value2 = corp.getDoubleValue(Corporation.YUQI_31DAY_90DAY_LOAN_BALANCE_COL);
				result = result + value1 + value2;
			}
		}
		return result;
	}

	public static double getOffBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value1 = corp.getDoubleValue(Corporation.OFF_BALANCE_CD_COL);
				double value2 = corp.getDoubleValue(Corporation.OFF_BALANCE_XYZ_COL);
				double value3 = corp.getDoubleValue(Corporation.OFF_BALANCE_BH_COL);
				double value4 = corp.getDoubleValue(Corporation.OFF_BALANCE_WTDK_COL);
				double value5 = corp.getDoubleValue(Corporation.OFF_BALANCE_WTTZ_COL);
				double value6 = corp.getDoubleValue(Corporation.OFF_BALANCE_CN_COL);
				double value7 = corp.getDoubleValue(Corporation.OFF_BALANCE_XYFXRZYH_COL);
				double value8 = corp.getDoubleValue(Corporation.OFF_BALANCE_JRYSP_COL);
				result = result + value1 + value2 + value3 + value4 + value5 + value6 + value7 + value8;
			}
		}
		return result;
	}

	public static double getLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getLoanBalance(list);
	}

	public static double getGuaranteedLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getGuaranteedLoanBalance(list);
	}

	public static double getGuanZhuLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getGuanZhuLoanBalance(list);
	}

	public static double getBuLiangLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getBuLiangLoanBalance(list);
	}

	public static double getYuQi90YiNeiLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getYuQi90YiNeiLoanBalance(list);
	}

	public static double getOffBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getOffBalance(list);
	}

	public static double getYuQi90YiShangLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getYuQi90YiShangLoanBalance(list);
	}

	public static double getYuQi90YiShangLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value1 = corp.getDoubleValue(Corporation.YUQI_91DAY_180DAY_LOAN_BALANCE_COL);
				double value2 = corp.getDoubleValue(Corporation.YUQI_181DAY_365DAY_LOAN_BALANCE_COL);
				double value3 = corp.getDoubleValue(Corporation.YUQI_1YEAR_3YEAR_LOAN_BALANCE_COL);
				double value4 = corp.getDoubleValue(Corporation.YUQI_3YEAR_YISHANG_LOAN_BALANCE_COL);
				result = result + value1 + value2 + value3 + value4;
			}
		}
		return result;
	}

	/**
	 * 从graphics中找出“核心企业集”包含corps的graphic，移除之
	 * 
	 * @param corp
	 * @param graphics
	 */
	public static void removeGraphicWhosCoreCorpsContains(List<Corporation> corps, List<Graphic> graphics) {
		if (graphics != null && corps != null && corps.size() > 0) {
			Iterator<Graphic> iterator = graphics.iterator();
			while (iterator.hasNext()) {
				Graphic g = iterator.next();
				Set<Corporation> coreCorpsOfG = g.getCoreCorps();
				if (coreCorpsOfG != null && coreCorpsOfG.containsAll(corps)) {
					iterator.remove();
				}
			}
		}
	}

	public static void removeGraphicWhosCoreCorpis(Corporation corp, List<Graphic> graphics) {
		List<Corporation> corps = new ArrayList<Corporation>();
		corps.add(corp);
		removeGraphicWhosCoreCorpsContains(corps, graphics);
	}

	public static Graphic getCircleWhosCoreCorpis(Corporation heaviestCorp, List<Graphic> graphics) {
		for (Graphic g : graphics) {
			Set<Corporation> coreCorps = g.getCoreCorps();
			if (coreCorps.contains(heaviestCorp))
				return g;
		}
		return null;
	}

	public static Set<Graphic> mergeCircles(SimpleDirectedWeightedGraph<Graphic, DefaultWeightedEdge> mergeTree) {
		boolean onlyAbsorbCorecorp = true;
		try {
			onlyAbsorbCorecorp = Boolean
					.getBoolean(XMLToolkit.getElementById(Config.getDoc(), "34").getAttribute("value"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<Graphic> leafs = new HashSet<Graphic>();

		while (true) {
			// 寻找非根叶子节点（即入度为0，出度不为0的节点）
			Graphic leaf = getLeaf(mergeTree);

			if (leaf != null) {// 如果树中还有非根叶子节点，则合并之
				DefaultWeightedEdge edge = mergeTree.edgesOf(leaf).iterator().next();
				Graphic parent = mergeTree.getEdgeTarget(edge);// 叶子节点的父节点
				parent.absorb(leaf, onlyAbsorbCorecorp);// 将叶子节点并入父节点
				leafs.add(leaf);// 记录一下
				mergeTree.removeVertex(leaf);// 将叶子节点移除
			} else// 否则，说明只剩根节点了，合并完成了，结束循环
				break;
		}

		return leafs;
	}

	private static Graphic getLeaf(SimpleDirectedWeightedGraph<Graphic, DefaultWeightedEdge> mergeTree) {
		Set<Graphic> vs = mergeTree.vertexSet();
		for (Graphic v : vs) {
			int inDegree = mergeTree.inDegreeOf(v);
			int outDegree = mergeTree.outDegreeOf(v);
			if (inDegree == 0 && outDegree != 0)
				return v;
		}
		return null;
	}

	public static Set<Corporation> getCorpsSet(List<Graphic> circles) {
		Set<Corporation> set = new HashSet<Corporation>();
		for (Graphic g : circles)
			set.addAll(g.vertexSet());
		return set;
	}

	public static double getBuLiangLv(Graphic circle) {
		List<Graphic> graphics = new ArrayList<Graphic>();
		graphics.add(circle);
		return getBuLiangLv(graphics);
	}

	public static double getBuLiangLv(List<Graphic> graphics) {
		double loanBalance = getLoanBalance(graphics);
		if (loanBalance == 0)
			return 0;
		double buliangLoanBalance = getBuLiangLoanBalance(graphics);

		return StringToolkit.formatDouble(buliangLoanBalance * 1.0 / loanBalance * 100.0);
	}

	public static String toDotCode(SimpleDirectedWeightedGraph<Graphic, DefaultWeightedEdge> graphic) {
		StringBuffer code = new StringBuffer();
		Set<DefaultWeightedEdge> edges = graphic.edgeSet();

		code.append("digraph show{node[fontname=\"宋体\"];\n");

		for (DefaultWeightedEdge e : edges)
			code.append("\"" + graphic.getEdgeSource(e).getName() + "\"->\"" + graphic.getEdgeTarget(e).getName()
					+ "\";\n");
		code.append("}");

		return code.toString();
	}

	public static File toFile(SimpleDirectedWeightedGraph<Graphic, DefaultWeightedEdge> graphic, File dir,
			String graphicName, String format) throws Exception {
		String s = toDotCode(graphic);
		dir = new File(dir.getAbsolutePath());
		if (dir.exists() == false)
			dir.mkdir();
		File file = new File(dir.getAbsolutePath() + "\\" + graphicName + ".dot");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		writer.append(s);
		writer.close();

		String cmd1 = "cmd /c cd " + System.getProperty("user.dir") + "/release/bin";
		String cmd2 = "cmd /c dot \"" + file.getAbsolutePath() + "\" -T" + format + " -o \""
				+ file.getAbsolutePath().replace(".dot", "." + format) + "\"";
		Runtime.getRuntime().exec(cmd1 + "&&" + cmd2).waitFor();

		return file;
	}

	public static List<Graphic> getBuliangCircles(List<Graphic> circles) {
		List<Graphic> buLiangCircles = new ArrayList<Graphic>();
		for (Graphic circle : circles) {
			if (getBuLiangLoanBalance(circle) > 0)
				buLiangCircles.add(circle);
		}
		return buLiangCircles;
	}

	public static Set<Corporation> getBuliangCorpsSet(List<Graphic> circles) {
		Set<Corporation> set = new HashSet<Corporation>();
		for (Graphic g : circles)
			set.addAll(getBuliangCorpsSet(g));
		return set;
	}

	public static Collection<? extends Corporation> getBuliangCorpsSet(Graphic circle) {
		Set<Corporation> set = new HashSet<Corporation>();
		for (Corporation corp : circle.vertexSet()) {
			if (corp.getBuLiangLoanBalance() > 0)
				set.add(corp);
		}
		return set;
	}

	public static double getBuliangCriclesLoanBalance(List<Graphic> circles) {

		List<Graphic> buliangCircles = getBuliangCircles(circles);
		return getLoanBalance(buliangCircles);

	}

	// 得到贷款余额位于(min,max]的担保圈清单
	public static List<Graphic> getCirclesByLoanBalance(List<Graphic> circles, Double min, Double max) {
		List<Graphic> cs = new ArrayList<Graphic>();
		for (Graphic circle : circles) {
			double loanbalance = getLoanBalance(circle);
			if (loanbalance > min && loanbalance <= max)
				cs.add(circle);
		}
		return cs;
	}

	// 得到客户个数位于(min,max]的担保圈清单
	public static List<Graphic> getCirclesByVertexCount(List<Graphic> circles, Double min, Double max) {
		List<Graphic> cs = new ArrayList<Graphic>();
		for (Graphic circle : circles) {
			double vertexCount = circle.vertexSet().size();
			if (vertexCount > min && vertexCount <= max)
				cs.add(circle);
		}
		return cs;
	}

	// 得到担保关系条数位于(min,max]的担保圈清单
	public static List<Graphic> getCirclesByEdgeCount(List<Graphic> circles, Double min, Double max) {
		List<Graphic> cs = new ArrayList<Graphic>();
		for (Graphic circle : circles) {
			double edgeCount = circle.edgeSet().size();
			if (edgeCount > min && edgeCount <= max)
				cs.add(circle);
		}
		return cs;
	}

	// 根据地区名称模糊查找担保圈
	public static List<Graphic> getCirclesByRegionName(List<Graphic> circles, String regionName) {
		List<Graphic> cs = new ArrayList<Graphic>();
		for (Graphic circle : circles) {
			Region circleRegion = circle.getRegion();
			String circleRegionName = circleRegion == null ? "未知地区" : circleRegion.getName();
			circleRegionName = circleRegionName == null ? "未知地区" : circleRegionName;
			if (circleRegionName.contains(regionName))
				cs.add(circle);
		}
		return cs;
	}

	// //取超大担保圈（实际此时只剩下超大独立担保圈了）
	// public static List<Graphic> getHugeCircles(List<Graphic> circles) {
	// List<Graphic> cs=new ArrayList<Graphic>();
	// for(Graphic circle:circles){
	// if(circle.getName().startsWith("超大"))
	// cs.add(circle);
	// }
	// return cs;
	// }
	// 取独立担保圈
	public static List<Graphic> getDuliCircles(List<Graphic> circles) {
		List<Graphic> cs = new ArrayList<Graphic>();
		for (Graphic circle : circles) {
			if (circle.getName().contains("独立担保圈"))
				cs.add(circle);
		}
		return cs;
	}

	// 取核心企业担保圈
	public static List<Graphic> getHxqyCircles(List<Graphic> circles) {
		List<Graphic> cs = new ArrayList<Graphic>();
		for (Graphic circle : circles) {
			if (circle.getName().contains("独立担保圈") == false)
				cs.add(circle);
		}
		return cs;
	}

	// 获取资产总额
	public static double getZCZE(List<Graphic> graphics) {

		double zcze = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.ZCZE_COL);

				zcze = zcze + value;
			}
		}
		return zcze;
	}

	public static double getZCZE(Graphic circle) {

		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getZCZE(list);
	}

	// 获取负债总额
	public static double getFZZE(List<Graphic> graphics) {

		double zcze = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.FZZE_COL);

				zcze = zcze + value;
			}
		}
		return zcze;
	}

	public static double getFZZE(Graphic circle) {

		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getFZZE(list);
	}

	public static double getHxqyLoanBalance(Graphic circle) {
		double loanbalance = 0;
		Set<Corporation> cores = circle.getCoreCorps();
		if (cores.size() == 0)
			return circle.getHeaviestVertex().getLoanBalance();
		else {
			for (Corporation core : cores) {
				loanbalance += core.getLoanBalance();
			}
			return loanbalance;
		}
	}

	// 获得最大N家贷款余额
	public static double getMaxNLoanBalanceVertexLoanBlance(Graphic circle, int N) {
		double loanbalance = 0;
		List<Corporation> maxVertexs = getMaxNLoanBalanceVertexs(circle, N);
		for (Corporation v : maxVertexs) {
			loanbalance += v.getLoanBalance();
		}
		return loanbalance;
	}

	// 获得最大N家企业
	public static List<Corporation> getMaxNLoanBalanceVertexs(Graphic circle, int N) {
		List<Corporation> maxVertexs = new ArrayList<Corporation>();
		List<Corporation> vertexs = new ArrayList<Corporation>();
		vertexs.addAll(circle.vertexSet());
		Collections.sort(vertexs, new Comparator<Corporation>() {
			public int compare(Corporation arg0, Corporation arg1) {
				double d0 = arg0.getLoanBalance();
				double d1 = arg1.getLoanBalance();

				if (d0 < d1)
					return 1; // Neither val is NaN, thisVal is smaller
				else if (d0 > d1)
					return -1;
				else
					return 0;
			}
		});
		int length = N < vertexs.size() ? N : vertexs.size();
		if (length == vertexs.size())
			return vertexs;
		else {
			for (int i = 0; i < length; i++)
				maxVertexs.add(vertexs.get(i));
			return maxVertexs;
		}
	}

	public static double getLoanBalance(Set<Corporation> vertexSet) {
		double result=0;
		for(Corporation v:vertexSet)
			result=result+=v.getLoanBalance();
		return result;
	}

	public static double getYuQiLoanBalance(Graphic circle) {

		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getYuQiLoanBalance(list);
	}

	private static double getYuQiLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.YUQI_LOAN_BALANCE_COL);
				result = result + value;
			}
		}
		return result;
	}

	// public static double getMutuallyLoanBalance(Graphic circle) {
	// Set<DefaultWeightedEdge> mutuallyEdgeSet= circle.getMutuallyEdges();
	//
	// return 0;
	// }

}
