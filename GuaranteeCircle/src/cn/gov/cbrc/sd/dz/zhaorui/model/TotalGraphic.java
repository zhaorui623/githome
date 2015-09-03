package cn.gov.cbrc.sd.dz.zhaorui.model;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class TotalGraphic {

	private static SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge> graphic;

	public static void init() {
		graphic = new SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}

	public static void addVertex(Corporation corp) {
		graphic.addVertex(corp);
		
	}

	//担保人->借款人
	public static void addEdge(Corporation sourceCorp, Corporation targetCorp) {
		graphic.addEdge(sourceCorp, targetCorp);
	}

	public static void printBasicInfo() {
		System.out.print("总图基本信息[");
		System.out.print("节点总数="+graphic.vertexSet().size()+"\t");
		System.out.print("边总数="+graphic.edgeSet().size()+"\t");
		System.out.print("]\n");
	}
}
