package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
		double loanBalance=0;
		if(graphics!=null){
			Set<Corporation> corps=getVertexSet(graphics);
			for(Corporation corp:corps){
				double value=corp.getDoubleValue(Corporation.LOAN_BALANCE_COL);
				loanBalance=loanBalance+value;
			}
		}
		return loanBalance;
	}

	public static double getGuanZhuLoanBalance(List<Graphic> graphics) {
		double result=0;
		if(graphics!=null){
			Set<Corporation> corps=getVertexSet(graphics);
			for(Corporation corp:corps){
				double value=corp.getDoubleValue(Corporation.GUANZHU_LOAN_BALANCE_COL);
				result=result+value;
			}
		}
		return result;
	}

	public static double getBuLiangLoanBalance(List<Graphic> graphics) {
		double result=0;
		if(graphics!=null){
			Set<Corporation> corps=getVertexSet(graphics);
			for(Corporation corp:corps){
				double value=corp.getDoubleValue(Corporation.BULIANG_LOAN_BALANCE_COL);
				result=result+value;
			}
		}
		return result;
	}

	public static double getYuQi90YiNeiLoanBalance(List<Graphic> graphics) {
		double result=0;
		if(graphics!=null){
			Set<Corporation> corps=getVertexSet(graphics);
			for(Corporation corp:corps){
				double value1=corp.getDoubleValue(Corporation.YUQI30YINEI_LOAN_BALANCE_COL);
				double value2=corp.getDoubleValue(Corporation.YUQI31_90_LOAN_BALANCE_COL);
				result=result+value1+value2;
			}
		}
		return result;
	}

	public static double getOffBalance(List<Graphic> graphics) {
		double result=0;
		if(graphics!=null){
			Set<Corporation> corps=getVertexSet(graphics);
			for(Corporation corp:corps){
				double value1=corp.getDoubleValue(Corporation.OFF_BALANCE_CD_COL);
				double value2=corp.getDoubleValue(Corporation.OFF_BALANCE_XYZ_COL);
				double value3=corp.getDoubleValue(Corporation.OFF_BALANCE_BH_COL);
				double value4=corp.getDoubleValue(Corporation.OFF_BALANCE_WTDK_COL);
				double value5=corp.getDoubleValue(Corporation.OFF_BALANCE_WTTZ_COL);
				double value6=corp.getDoubleValue(Corporation.OFF_BALANCE_CN_COL);
				double value7=corp.getDoubleValue(Corporation.OFF_BALANCE_XYFXRZYH_COL);
				double value8=corp.getDoubleValue(Corporation.OFF_BALANCE_JRYSP_COL);
				result=result+value1+value2+value3+value4+value5+value6+value7+value8;
			}
		}
		return result;
	}

	public static double getLoanBalance(Graphic circle) {
		List<Graphic> list=new ArrayList<Graphic>();
		list.add(circle);
		return getLoanBalance(list);
	}

	public static double getGuanZhuLoanBalance(Graphic circle) {
		List<Graphic> list=new ArrayList<Graphic>();
		list.add(circle);
		return getGuanZhuLoanBalance(list);
	}

	public static double getBuLiangLoanBalance(Graphic circle) {
		List<Graphic> list=new ArrayList<Graphic>();
		list.add(circle);
		return getBuLiangLoanBalance(list);
	}

	public static double getYuQi90YiNeiLoanBalance(Graphic circle) {
		List<Graphic> list=new ArrayList<Graphic>();
		list.add(circle);
		return getYuQi90YiNeiLoanBalance(list);
	}

	public static double getOffBalance(Graphic circle) {
		List<Graphic> list=new ArrayList<Graphic>();
		list.add(circle);
		return getOffBalance(list);
	}

}
