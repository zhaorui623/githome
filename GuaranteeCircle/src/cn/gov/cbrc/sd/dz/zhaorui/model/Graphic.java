package cn.gov.cbrc.sd.dz.zhaorui.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class Graphic {

	private SimpleDirectedWeightedGraph<Corporation, DefaultWeightedEdge> g;
	private String name;

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
		System.out.print("总图基本信息[");
		System.out.print("节点总数=" + g.vertexSet().size() + "\t");
		System.out.print("边总数=" + g.edgeSet().size());
		System.out.print("]\n");
	}

	public String toDotCode() {
		StringBuffer code = new StringBuffer();
		code.append("digraph show{node[fontname=\"宋体\"];\n");
		// if(g.name.equals("独立")==false)
		// code.append(g.name.substring(0,
		// g.name.length()-1)+"[style=\"filled\",fillcolor=\"#f08080\"]\n");
		for (DefaultWeightedEdge e : this.edgeSet())
			code.append(this.getEdgeSource(e).getName() + "->" + this.getEdgeTarget(e).getName() + ";\n");
		code.append("}");

		return code.toString();
	}

	public File toFile(File dir)
			throws Exception {
		String s = this.toDotCode();
		dir = new File(dir.getAbsolutePath() );
		if (dir.exists() == false)
			dir.mkdir();
		File file = new File(dir.getAbsolutePath() + "\\" + this.getName() + ".dot");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		writer.append(s);
		writer.close();

		String cmd1 = "cmd /c cd " + System.getProperty("user.dir") + "/release/bin";
		String cmd2 = "cmd /c dot " + file.getAbsolutePath() + " -Tsvg -o "
				+ file.getAbsolutePath().replace(".dot", ".svg");
		String cmd3 = cmd2.replaceAll("svg", "png");
		Runtime.getRuntime().exec(cmd1 + "&&" /* + cmd2 + "&&" */ + cmd3).waitFor();;

		return file;
	}

	public void setNameSuffix(String name) {
		this.name=name+this.toString().replaceAll(this.getClass().getName(), "");
	}
	public String getName(){
		return this.name;
	}

	/**
	 * 得到某节点的互保节点个数
	 * @param corp
	 */
	public int  mutuallyDegreeOf(Corporation v) {
		int result= mutuallyVertexsOf(v).size();
//		System.out.println(v.getName()+".互保企业个数="+result);
		return result;
	}

	/**
	 * 得到某节点的互保节点集合
	 * @return
	 */
	public Set<Corporation> mutuallyVertexsOf(Corporation v) {
		Set<Corporation> mutuallyVertexsSet=new HashSet<Corporation>();
		//得到所有指向该节点的节点
		Set<Corporation> inVertexsSet=incomingVertexsOf(v);
		//得到所有该节点 指向的节点
		Set<Corporation> outVertexsSet=outgoingVertexsOf(v);
		//取两个集合的交集
		mutuallyVertexsSet.addAll(inVertexsSet);
		mutuallyVertexsSet.retainAll(outVertexsSet);
		return mutuallyVertexsSet;
	}

	public Set<Corporation> outgoingVertexsOf(Corporation v) {
		Set<Corporation> outVertexsSet=new HashSet<Corporation>();
		Set<DefaultWeightedEdge>  outEdges=this.outgoingEdgesOf(v);
		for(DefaultWeightedEdge edge:outEdges)
			outVertexsSet.add(this.getEdgeTarget(edge));
		return outVertexsSet;
	}

	public Set<Corporation> incomingVertexsOf(Corporation v) {
		Set<Corporation> inVertexsSet=new HashSet<Corporation>();
		Set<DefaultWeightedEdge>  inEdges=this.incomingEdgesOf(v);
		for(DefaultWeightedEdge edge:inEdges)
			inVertexsSet.add(this.getEdgeSource(edge));
		return inVertexsSet;
	}
}
