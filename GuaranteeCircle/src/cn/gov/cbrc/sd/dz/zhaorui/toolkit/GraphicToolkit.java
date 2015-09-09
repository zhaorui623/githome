package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;

public class GraphicToolkit {

	/**
	 * 用广度优先搜索的方法，识别传入参数graphic的所有连通子图，放到一个List里返回
	 * 
	 * @return
	 */
	public static List<SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge>> split(
			SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge> graphic) {
		// 用来存放待会拆出来的所有子图
		List<SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge>> result = new ArrayList<SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge>>();
		// 当graphic里尚有节点时，就一直做这个循环，这是为了拆出所有连通子图
		while (graphic.vertexSet().isEmpty() == false) {
			// 用来存放待会拆出来的一个子图
			SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge>(
					DefaultWeightedEdge.class);
			// 随便取一个节点,加到一个队列里，并同时加到子图g中
			Queue<Corporation> queue = new LinkedList<Corporation>();
			Corporation corpRandom = graphic.vertexSet().iterator().next();
			addToQueue(queue, corpRandom);
			g.addVertex(corpRandom);
			// 当队列不为空时，就一直做这个循环，这是为了得到一个连通子图
			while (queue.isEmpty() == false) {
				// 从队列中弹出一个节点
				Corporation corp = queue.poll();
				// 如果图中已经不存在这个节点了，压入队列后在处理其它节点时发现它已经满足移除出总图的条件了，就把它移除了，所以此处就没有再处理的必要了
				if (graphic.containsVertex(corp) == false)
					continue;
				// 获取与该节点接触的所有边
				Set<DefaultWeightedEdge> edges = graphic.edgesOf(corp);
				for (DefaultWeightedEdge edge : edges) {
					Corporation corpS = graphic.getEdgeSource(edge);
					Corporation corpT = graphic.getEdgeTarget(edge);
					Corporation corp2 = corpS.equals(corp) == false ? corpS : corpT;
					// 将每条边的另一端的那个节点加到子图g中
					g.addVertex(corp2);
					// 如果另端节点还与其他节点相连，则压入队列，否则直接从总图中移除
					if (graphic.edgesOf(corp2).size() > 1)
						addToQueue(queue, corp2);
					else
						graphic.removeVertex(corp2);
					// 将每条边加入到子图中
					g.addEdge(corpS, corpT);
					// 从总图中移除每条边
					graphic.removeEdge(edge);
				}
				// 从总图中移除 刚刚被弹出的 这个节点
				graphic.removeVertex(corp);
			}
			result.add(g);
		}

		return result;
	}

	public static boolean addToQueue(Queue q, Object obj) {
		if (q.contains(obj) == false)
			return q.add(obj);
		else
			return true;
	}
}
