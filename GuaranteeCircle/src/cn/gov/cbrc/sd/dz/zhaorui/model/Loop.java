package cn.gov.cbrc.sd.dz.zhaorui.model;

import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Loop extends Graphic {

	private int length;
	private List<DefaultWeightedEdge> loopPath;

	public Loop(int length) {
		this.length = length;
	}

	public Loop(List<DefaultWeightedEdge> loopPath) {
		this.length = loopPath.size();
		this.loopPath = loopPath;
	}

	private void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(getName());
		if (loopPath != null) {
			sb.append("环路径：");
			DefaultWeightedEdge e = null;
			for (int i = 0; i < loopPath.size(); i++) {
				e = loopPath.get(i);
				sb.append(getEdgeSource(e).getName());
				sb.append("->");
			}
			sb.append(getEdgeTarget(e).getName());
		}
		sb.append("]");
		return sb.toString();
	}
}
