package cn.gov.cbrc.sd.dz.zhaorui.jgrapht.demo;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		String v1 = "1", v2 = "2", v3 = "3", v4 = "4", v5 = "5";
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);
		g.addVertex(v5);

		g.addEdge(v1, v2);
		g.addEdge(v2, v3);
		g.addEdge(v3, v2);
		g.addEdge(v4, v2);
		g.addEdge(v3, v5);
		g.addEdge(v4, v5);

		System.out.println("广度优先：");
		BreadthFirstIterator<String, DefaultWeightedEdge> bit = new BreadthFirstIterator<String, DefaultWeightedEdge>(
				g);
		bit.setCrossComponentTraversal(false);
		while (bit.hasNext())
			System.out.println(bit.next());
		System.out.println("遍历结束。");

		System.out.println(g.incomingEdgesOf(v2));
		System.out.println(g.outgoingEdgesOf(v2));
	}

}
