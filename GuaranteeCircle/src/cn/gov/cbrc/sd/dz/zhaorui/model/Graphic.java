package cn.gov.cbrc.sd.dz.zhaorui.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3.Procedure;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3.Step3Module;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GCLogger;

public class Graphic {

	private SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge> g;
	private Map<String, Corporation> corpIndexing;// 公司索引，方便查找
	private String name;
	private Region region;
	private GraphicClassify riskClassify;
	private boolean VIPTag;
	private Set<Corporation> coreCorps = new HashSet<Corporation>();
	private Set<Corporation> secondCoreCorps = new HashSet<Corporation>();// 合并其他圈得到的核心节点放到这里面

	public Graphic() {
		g = new SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}

	public Graphic(SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge> g) {
		this.g = g;
	}

	public SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge> g() {
		return g;
	}

	public DefaultWeightedEdge addEdge(Corporation sourceVertex, Corporation targetVertex) {
		return g.addEdge(sourceVertex, targetVertex);
	}

	public boolean addVertex(Corporation v) {
		return g.addVertex(v);
	}

	public boolean containsEdge(DefaultWeightedEdge e) {
		return g.containsEdge(e);
	}

	public boolean containsEdge(Corporation sourceVertex, Corporation targetVertex) {
		return g.containsEdge(sourceVertex, targetVertex);
	}

	public boolean containsVertex(Corporation v) {
		return g.containsVertex(v);
	}

	public int degreeOf(Corporation vertex) {
		return g.degreeOf(vertex);
	}

	public Set<DefaultWeightedEdge> edgeSet() {
		return g.edgeSet();
	}

	public Set<DefaultWeightedEdge> edgesOf(Corporation vertex) {
		return g.edgesOf(vertex);
	}

	public DefaultWeightedEdge getEdge(Corporation sourceVertex, Corporation targetVertex) {
		return g.getEdge(sourceVertex, targetVertex);
	}

	public Corporation getEdgeSource(DefaultWeightedEdge e) {
		return g.getEdgeSource(e);
	}

	public Corporation getEdgeTarget(DefaultWeightedEdge e) {
		return g.getEdgeTarget(e);
	}

	public double getEdgeWeight(DefaultWeightedEdge e) {
		return g.getEdgeWeight(e);
	}

	public Set<DefaultWeightedEdge> incomingEdgesOf(Corporation vertex) {
		return g.incomingEdgesOf(vertex);
	}

	public int inDegreeOf(Corporation vertex) {
		return g.inDegreeOf(vertex);
	}

	public int outDegreeOf(Corporation vertex) {
		return g.outDegreeOf(vertex);
	}

	public Set<DefaultWeightedEdge> outgoingEdgesOf(Corporation vertex) {
		return g.outgoingEdgesOf(vertex);
	}

	public boolean removeAllEdges(Collection<DefaultWeightedEdge> edges) {
		return g.removeAllEdges(edges);
	}

	public boolean removeAllVertices(Collection<Corporation> vertices) {
		return g.removeAllVertices(vertices);
	}

	public boolean removeEdge(DefaultWeightedEdge e) {
		return g.removeEdge(e);
	}

	public DefaultWeightedEdge removeEdge(Corporation sourceVertex, Corporation targetVertex) {
		return g.removeEdge(sourceVertex, targetVertex);
	}

	public boolean removeVertex(Corporation v) {
		return g.removeVertex(v);
	}

	public void setEdgeWeight(DefaultWeightedEdge e, double weight) {
		g.setEdgeWeight(e, weight);
	}

	public Set<Corporation> vertexSet() {
		return g.vertexSet();
	}

	public void printBasicInfo() {
		InfoPane.getInstance().info("总图基本信息[节点总数=" + g.vertexSet().size() + "\t边总数=" + g.edgeSet().size() + "]");
	}

	public String toDotCode() {
		StringBuffer code = new StringBuffer();
		Set<DefaultWeightedEdge> edges = this.edgeSet();
		Set<Corporation> vertexs = this.vertexSet();
		code.append("digraph show{node[fontname=\"宋体\"];\n");
		// if(g.name.equals("独立")==false)
		// code.append(g.name.substring(0,
		// g.name.length()-1)+"[style=\"filled\",fillcolor=\"#f08080\"]\n");

		for (Corporation v : vertexs) {
			if (this.coreCorps.contains(v)) // 将该图的核心企业背景设置为红色
				code.append(v.getName() + "[peripheries=\"2\",style=\"filled\",fillcolor=\"#FF0033\"]\n");
			else if (v.isCore() == true) // 将其他核心企业背景设置为粉色
				code.append(v.getName() + "[style=\"filled\",fillcolor=\"#FF99FF\"]\n");

		}
		for (DefaultWeightedEdge e : edges)
			code.append(this.getEdgeSource(e).getName() + "->" + this.getEdgeTarget(e).getName() + ";\n");
		code.append("}");

		return code.toString();
	}

	public File toFile(File dir) throws Exception {
		String s = this.toDotCode();
		dir = new File(dir.getAbsolutePath());
		if (dir.exists() == false)
			dir.mkdir();
		File file = new File(dir.getAbsolutePath() + "\\" + this.getName() + ".dot");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		writer.append(s);
		writer.close();

		String cmd1 = "cmd /c cd " + System.getProperty("user.dir") + "/release/bin";
		String cmd2 = "cmd /c dot \"" + file.getAbsolutePath() + "\" -Tsvg -o \""
				+ file.getAbsolutePath().replace(".dot", ".svg") + "\"";
		String cmd3 = cmd2.replaceAll("svg", "png");
		Runtime.getRuntime().exec(cmd1 + "&&" /* + cmd2 + "&&" */ + cmd3).waitFor();
		;

		return file;
	}

	public void setNameSuffix(String name) {
		this.name = name + this.toString().replaceAll(this.getClass().getName(), "");
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 得到某节点的互保节点个数
	 * 
	 * @param corp
	 */
	public int mutuallyDegreeOf(Corporation v) {
		int result = mutuallyVertexsOf(v).size();
		// System.out.println(v.getName()+".互保企业个数="+result);
		return result;
	}

	/**
	 * 得到某节点的互保节点集合
	 * 
	 * @return
	 */
	public Set<Corporation> mutuallyVertexsOf(Corporation v) {
		Set<Corporation> mutuallyVertexsSet = new HashSet<Corporation>();
		// 得到所有指向该节点的节点
		Set<Corporation> inVertexsSet = incomingVertexsOf(v);
		// 得到所有该节点 指向的节点
		Set<Corporation> outVertexsSet = outgoingVertexsOf(v);
		// 取两个集合的交集
		mutuallyVertexsSet.addAll(inVertexsSet);
		mutuallyVertexsSet.retainAll(outVertexsSet);
		return mutuallyVertexsSet;
	}

	public Set<Corporation> outgoingVertexsOf(Corporation v) {
		Set<Corporation> outVertexsSet = new HashSet<Corporation>();
		Set<DefaultWeightedEdge> outEdges = this.outgoingEdgesOf(v);
		for (DefaultWeightedEdge edge : outEdges)
			outVertexsSet.add(this.getEdgeTarget(edge));
		return outVertexsSet;
	}

	public Set<Corporation> incomingVertexsOf(Corporation v) {
		Set<Corporation> inVertexsSet = new HashSet<Corporation>();
		Set<DefaultWeightedEdge> inEdges = this.incomingEdgesOf(v);
		for (DefaultWeightedEdge edge : inEdges)
			inVertexsSet.add(this.getEdgeSource(edge));
		return inVertexsSet;
	}

	/**
	 * 得到某节点所在的所有环（只取环路径长度≤loopLengthCeilling的环）
	 * 
	 * @param core
	 * @param loopLengthCeilling
	 * @return
	 */
	public Set<Loop> loopsOf(Corporation v, int loopLengthCeilling) {
		// Set<Loop> loops = loopsOf(v);
		// Iterator<Loop> loopsIterator = loops.iterator();
		// while (loopsIterator.hasNext()) {
		// Loop loop = loopsIterator.next();
		// if (loop.getLength() > loopLengthCeilling)
		// loopsIterator.remove();
		// }
		// return loops;

		// System.out.println("开始查找" + v.getName() + "所在的环");
		Set<Loop> loops = new HashSet<Loop>();
		List<Corporation> currentPath = new LinkedList<Corporation>();
		List<DefaultWeightedEdge> edgesOfCurrentPath = new LinkedList<DefaultWeightedEdge>();
		// 将当前节点设置为根节点
		Corporation currentVertex = v;
		// 将当前节点加入当前路径
		currentPath.add(currentVertex);
		dfs(currentVertex, currentPath, edgesOfCurrentPath, v, loops, loopLengthCeilling);
		return loops;
	}

	/**
	 * 得到某节点所在的所有环
	 * 
	 * @param core
	 * @return
	 */
	public Set<Loop> loopsOf(Corporation v) {
		return loopsOf(v, Integer.MAX_VALUE);
	}

	private void dfs(Corporation currentVertex, List<Corporation> currentPath,
			List<DefaultWeightedEdge> edgesOfCurrentPath, Corporation startVertex, Set<Loop> loops,
			int loopLengthCeilling) {
		// 取当前节点的所有子节点
		Set<Corporation> outVs = this.outgoingVertexsOf(currentVertex);
		for (Corporation outV : outVs) {// 对每一个子节点
			if (edgesOfCurrentPath.contains(this.getEdge(currentVertex, outV))
					|| edgesOfCurrentPath.size() >= loopLengthCeilling)
				continue;
			currentPath.add(outV);// 将子节点加入当前路径
			edgesOfCurrentPath.add(this.getEdge(currentVertex, outV));
			if (outV.equals(startVertex)) {// 如果该子节点与起始节点相同，说明找到了一个回路
				Loop loop = new Loop(edgesOfCurrentPath);// 新建一个回路对象
				for (Corporation c : currentPath)// 将当前路径上的所有节点加入到回路中
					loop.addVertex(c);
				loop.addEdgesFrom(this);// 将所有边加入回路
				loops.add(loop);
				// System.out.println("找到一个环" + loop);
				// 移除最后一个节点
				currentPath.remove(currentPath.size() - 1);
				edgesOfCurrentPath.remove(edgesOfCurrentPath.size() - 1);
				continue;
			}
			// 继续深度遍历
			dfs(outV, currentPath, edgesOfCurrentPath, startVertex, loops, loopLengthCeilling);
			// 移除最后一个节点
			currentPath.remove(currentPath.size() - 1);
			edgesOfCurrentPath.remove(edgesOfCurrentPath.size() - 1);
		}

	}

	public static void main(String[] args) {
		Graphic g = new Graphic();
		Corporation v0 = Corporation.createDefaultCorp("0");
		Corporation v1 = Corporation.createDefaultCorp("1");
		Corporation v2 = Corporation.createDefaultCorp("2");
		Corporation v3 = Corporation.createDefaultCorp("3");
		Corporation v4 = Corporation.createDefaultCorp("4");

		g.addVertex(v0);
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);

		g.addEdge(v0, v1);
		g.addEdge(v1, v0);
		g.addEdge(v4, v0);
		g.addEdge(v1, v4);
		g.addEdge(v2, v1);
		g.addEdge(v1, v2);
		g.addEdge(v2, v3);
		g.addEdge(v3, v2);

		g.printBasicInfo();

		g.loopsOf(v0, 7);
	}

	/**
	 * 尝试把本图的所有节点的所有边（Graphic中的边）加入本图中
	 * 
	 * @param graphic
	 *            节点原来所属图
	 */
	public void addEdgesFrom(Graphic graphic) {
		Set<Corporation> vs = this.vertexSet();
		for (Corporation v : vs) {
			if (graphic.vertexSet().contains(v)) {// 如果这个节点是本图和graphic的共同节点
				Set<DefaultWeightedEdge> edges = graphic.edgesOf(v);// 则取graphic中该节点的所有边
				for (DefaultWeightedEdge edge : edges) {// 尝试将每条边接入到本图中
					try {
						this.addEdge(graphic.getEdgeSource(edge), graphic.getEdgeTarget(edge));
					} catch (IllegalArgumentException e) {
						// 如果该边加不进来，说明对端节点未被拉取，所以自然就不用加
					}

				}
			}
		}
	}

	public Graphic clone() {
		Graphic graphicClone = new Graphic();
		if (this.getName() != null)
			graphicClone.setName(new String(this.getName()));
		Set<Corporation> vertexSet = this.vertexSet();
		for (Corporation vertex : vertexSet) {
			Corporation vertexClone = vertex.clone();
			graphicClone.addVertex(vertexClone);
		}
		Set<DefaultWeightedEdge> edgeSet = this.edgeSet();
		double i = 0, f = edgeSet.size();
		Procedure p=((Step3Module) (GC.getGalileo().getModule("3"))).procedure;
		for (DefaultWeightedEdge edge : edgeSet) {
			Corporation corpSource = this.getEdgeSource(edge);
			Corporation corpTarget = this.getEdgeTarget(edge);
			Corporation corpSourceClone = graphicClone.getVertexByName(corpSource.getName());
			Corporation corpTargetClone = graphicClone.getVertexByName(corpTarget.getName());
			graphicClone.addEdge(corpSourceClone, corpTargetClone);
			i++;
			p.setPercent((int) (i / f / 2 * 100));
		}

		return graphicClone;
	}

	public Corporation getVertexByName(String name) {
		if (corpIndexing == null) {
			corpIndexing = new HashMap<String, Corporation>();
			Set<Corporation> vertexs = this.vertexSet();
			for (Corporation v : vertexs)
				corpIndexing.put(v.getName(), v);

		}
		return corpIndexing.get(name);
	}

	public void setRegion(Region region) {
		this.region = region;

	}

	public Region getRegion() {

		return this.region;
	}

	public void setRiskClassify(GraphicClassify riskClassify) {
		this.riskClassify = riskClassify;
	}

	public GraphicClassify getRiskClassify() {
		return this.riskClassify;
	}

	public boolean containsVertexWithName(String corpName) {
		return false;
	}

	public void setVIPTag(boolean isVIPGrphic) {
		this.VIPTag = isVIPGrphic;
	}

	public boolean isVIPGraphic() {
		return this.VIPTag;
	}

	public void addCoreCorps(Corporation corp) {
		if (coreCorps == null)
			coreCorps = new HashSet<Corporation>();
		if (corp != null)
			coreCorps.add(corp);

	}

	public Set<Corporation> getCoreCorps() {
		return coreCorps;
	}

	public Corporation getHeaviestVertex(boolean core) {
		Set<Corporation> vertexs = this.vertexSet();
		double maxWeight = -1.0;
		Corporation heviestVertex = null;
		for (Corporation vertex : vertexs) {
			if (core == true && vertex.isCore() == false)
				continue;
			double weight = vertex.getWeight();
			if (weight > maxWeight) {
				maxWeight = weight;
				heviestVertex = vertex;
			}
		}
		return heviestVertex;
	}

	public Corporation getHeaviestCoreVertex() {

		return getHeaviestVertex(true);
	}

	/**
	 * 将整个graphic合并进本图中
	 * 
	 * @param graphic
	 */
	public void absorb(Graphic graphic) {
		absorb(graphic, false);
	}

	/**
	 * 将graphic合并进本图中，如果onlyCoreCorp为true，则只并入graphic的核心节点及其相关边
	 * 
	 * @param graphic
	 * @param onlyCoreCorp
	 */
	public void absorb(Graphic graphic, boolean onlyCoreCorp) {
		Set<Corporation> coreCorps = new HashSet<Corporation>();
		coreCorps.addAll(graphic.getCoreCorps());
		coreCorps.addAll(graphic.getSecondCoreCorps());
		Set<Corporation> vs = onlyCoreCorp ? coreCorps : graphic.vertexSet();
		for (Corporation v : vs) {
			this.addVertex(v);
			this.addSecondCoreCorps(v);
		}
		this.addEdgesFrom(graphic);

	}

	private Collection<Corporation> getSecondCoreCorps() {

		return this.secondCoreCorps;
	}

	private void addSecondCoreCorps(Corporation v) {
		this.secondCoreCorps.add(v);
	}

	public Corporation getHeaviestVertex() {

		return getHeaviestVertex(false);
	}
}
