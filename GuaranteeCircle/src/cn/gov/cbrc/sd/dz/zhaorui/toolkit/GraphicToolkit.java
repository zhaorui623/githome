package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;

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

	public static double getLoanBalance(List<Graphic> graphics) {
		double loanBalance = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.LOAN_BALANCE_COL);
				loanBalance = loanBalance + value;
			}
		}
		return loanBalance;
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
		Set<Graphic> leafs=new HashSet<Graphic>();
		
		while(true){
			//寻找非根叶子节点（即入度为0，出度不为0的节点）
			Graphic leaf=getLeaf(mergeTree);
			
			if(leaf!=null){//如果树中还有非根叶子节点，则合并之
				DefaultWeightedEdge edge=mergeTree.edgesOf(leaf).iterator().next();
				Graphic parent=mergeTree.getEdgeTarget(edge);//叶子节点的父节点
				parent.absorb(leaf,true);//将叶子节点并入父节点(只保留叶子节点的核心企业）
				leafs.add(leaf);//记录一下
				mergeTree.removeVertex(leaf);//将叶子节点移除
			}else//否则，说明只剩根节点了，合并完成了，结束循环
				break;			
		}
		
		return leafs;		
	}

	private static Graphic getLeaf(SimpleDirectedWeightedGraph<Graphic, DefaultWeightedEdge> mergeTree) {
		Set<Graphic> vs=mergeTree.vertexSet();
		for(Graphic v:vs){
			int inDegree=mergeTree.inDegreeOf(v);
			int outDegree=mergeTree.outDegreeOf(v);
			if(inDegree==0&&outDegree!=0)
				return v;
		}
		return null;
	}

}
