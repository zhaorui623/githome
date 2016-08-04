package cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;

import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;

public class FNAlgorithm extends HugeCircleSplitAlgorithm {

	public FNAlgorithm(String algorithm_name, Boolean algorithm_enable, Boolean algorithm_selected) throws Exception {

		super(algorithm_name, algorithm_enable, algorithm_selected);
	}

	public static void main(String[] args) throws Exception {
		for (int size = 338; size <= 338; size = size + 8) {
			long start = System.currentTimeMillis();

			Graphic g = new Graphic();
			Corporation v[] = new Corporation[size];
			for (int i = 0; i < v.length; i++) {
				v[i] = Corporation.createDefaultCorp("" + i, "" + i);
				g.addVertex(v[i]);
			}
			for (int i = 0; i < size / 8; i++) {
				g.addEdge(v[1 + i * 8], v[2 + i * 8]);
				g.addEdge(v[1 + i * 8], v[0 + i * 8]);
				g.addEdge(v[0 + i * 8], v[3 + i * 8]);
				g.addEdge(v[2 + i * 8], v[3 + i * 8]);
				g.addEdge(v[3 + i * 8], v[4 + i * 8]);
				g.addEdge(v[4 + i * 8], v[5 + i * 8]);
				g.addEdge(v[4 + i * 8], v[7 + i * 8]);
				g.addEdge(v[7 + i * 8], v[6 + i * 8]);
				g.addEdge(v[5 + i * 8], v[6 + i * 8]);
			}

			// g.toFile(new File("6个点"));
			new FNAlgorithm(null, true, true).splitHugeCircleImpl(g);
			System.out.println(
					"m+n=" + (g.vertexSet().size() + g.edgeSet().size()) + "耗时" + (System.currentTimeMillis() - start));
		}
	}

	public List<Graphic> splitHugeCircleImpl(Graphic g) throws Exception {
		List<Graphic> sonGraphics = new ArrayList<Graphic>();
		int i = 0;

		Set<Corporation> vs = g.vertexSet();
		int N = vs.size();
		// 找贷款余额占整个超大圈贷款余额90%的前N个节点
		// 排序
		List<Corporation> vs_ordered = GraphicToolkit.getMaxNLoanBalanceVertexs(g, N);
		double totalLoanBalance = GraphicToolkit.getLoanBalance(g);
		double tt = 0;
		for (; i < vs_ordered.size(); i++) {
			tt = tt + vs_ordered.get(i).getLoanBalance();
			if (tt / totalLoanBalance > 0.8)
				break;
		}
		i = (i == vs_ordered.size()) ? (i - 1) : i;
		// 前i个节点贷款余额占整个超大圈贷款余额90%
		InfoPane.getInstance().info("贷款余额前" + i + "名的企业贷款余额之和占该超大担保圈贷款余额的80%以上");
		// 取这些节点的N手圈
		List<Graphic> comunities = new LinkedList<Graphic>();
		for (int j = 0; j <= i; j++) {
			Graphic comunit = PickAlgorithm.pickCircleOf(g, vs_ordered.get(j));
			comunities.add(comunit);
		}

		// // 初始化，每个节点都是一个社区
		// List<Graphic> comunities = new LinkedList<Graphic>();
		// for (Corporation corp : g.vertexSet()) {
		// Graphic comunity = new Graphic();
		// comunity.addVertex(corp);
		// comunities.add(comunity);
		// }
		double bestQ = -2000000000;
		List<Graphic> bestComunities = null;
		long start = System.currentTimeMillis();
		// Set<DefaultWeightedEdge> edgeg.edgeSet()
		// 一直到合并为一个社区，循环结束
		while (comunities.size() != 1) {
			// 当前的Q
			double Q = getEQ(g, comunities);
			System.out.println("=============当前EQ=" + Q);
			System.out.println("=====" + comunities.size() + "===========================================");
			// System.out.println(System.currentTimeMillis() - start);
			// 合并后Q的增量
			double d_Q_Max = -2000000000;
			List<Graphic> bestComunitiesOfThisTurn = null;
			// 尝试两两社区合并，计算新Q
			for (i = 0; i < comunities.size() - 1; i++) {
				System.out.print("i=" + i + " ");
				for (int j = i + 1; j < comunities.size(); j++) {
//					System.out.print(j + " ");
					// System.out
					// .println("假如合并" + (comunities.get(i).vertexSet()) + "与" +
					// (comunities.get(j).vertexSet()));
					List<Graphic> tmp = new LinkedList<Graphic>();
					for (int k = 0; k < comunities.size(); k++) {
						if (k == i) {
							Graphic comunityI = comunities.get(i).clone();
							// Set<Corporation> ivs = comunityI.vertexSet();
							Graphic comunityJ = comunities.get(j).clone();
							// Set<Corporation> jvs = comunityJ.vertexSet();
							// for (Corporation v : comunityJ.vertexSet()) {
							// comunityI.addVertex(v);
							// }
							// for (Corporation v : comunityJ.vertexSet()) {
							// Set<DefaultWeightedEdge> vEdges = g.edgesOf(v);
							// for (DefaultWeightedEdge e : vEdges) {
							// Corporation s = comunityJ.getEdgeSource(e);
							// Corporation t = comunityJ.getEdgeTarget(e);
							// if (comunityI.getEdge(s, t) != null)
							// continue;
							// if (ivs.contains(s) && jvs.contains(t) ||
							// ivs.contains(t) && jvs.contains(s)
							// || jvs.contains(t) && jvs.contains(s))
							// comunityI.addEdge(s, t);
							// }
							// }
							comunityI.absorb(comunityJ);
							tmp.add(comunityI);
						} else if (k == j)
							continue;
						else
							tmp.add(comunities.get(k));
					}
					double Q_new = getEQ(g, tmp);
					// System.out.println("Q_new=" + Q_new);
					double d_Q = Q_new - Q;
					if (d_Q > d_Q_Max) {
						d_Q_Max = d_Q;
						bestComunitiesOfThisTurn = tmp;
					}
				}
			}
			System.out.println();
			comunities = bestComunitiesOfThisTurn;
			if (Q + d_Q_Max > bestQ) {
				bestQ = Q + d_Q_Max;
				bestComunities = bestComunitiesOfThisTurn;
			}
			// System.out.println(comunities.size() + "\t" + Q);
		}
		// double Q = getQ(g, bestComunities);
		// System.out.println("Q=\t" + Q);
		System.out.println("EQ=\t" + getEQ(g, bestComunities));
//		 System.out.println("bestComunities" + "\t" + bestComunities + "\t" +
//		 bestQ);
		 for (i = 0; i < bestComunities.size(); i++)
		 System.out.println("社区" + i + bestComunities.get(i).vertexSet());
		// for (Set<Corporation> vertexSet : bestComunities) {
		// Graphic son = new Graphic();
		// for (Corporation corp : vertexSet) {
		// son.addVertex(corp);
		// }
		// son.addEdgesFrom(g);
		// sonGraphics.add(son);
		// son.setName("FN担保圈-"+son.getHeaviestVertex().getName()+"圈");
		// }
		// for (Graphic son : sonGraphics)
		// son.toFile(new File("6个点"));
		// return sonGraphics;
		return bestComunities;
	}

	private String comunity2Str(Set<Corporation> set) {
		String str = "";
		for (Corporation corp : set) {
			str = str + "," + corp.getName();
		}
		return "[" + str + "]";
	}

	public static double getQ(Graphic g, List<Graphic> comunities) {
		// System.out.println("计算" + toStr(comunities) + "的模块度");
		double Q = 0;
		// 所有边
		double m = g.edgeSet().size();
		for (Graphic comunity : comunities) {
			double Ic = comunity.edgeSet().size();
			double Dc = getComunityAllVertexsDegreeSum(g, comunity);

			// System.out.println("Q=" + Q + "+" + "(" + Ic + "/" + m +
			// "-Math.pow(" + Dc + " / (2 * " + m + "))");
			Q = Q + (Ic / m - Math.pow(Dc / (2 * m), 2));
		}
		// System.out.println(comunities+"\t"+Q);
		return Q;
	}

	// 重叠社区的Q
	public static double getEQ(Graphic g, List<Graphic> comunities) {

		double Q = 0;
		// 所有边
		double m = g.edgeSet().size();

		// List<Corporation> vs = new ArrayList<Corporation>();
		// vs.addAll(g.vertexSet());
		// int size = vs.size();
		// 两两节点
		Map<Corporation, Corporation> visited = new HashMap<Corporation, Corporation>();
		for (Graphic c : comunities) {
			List<Corporation> vs = new ArrayList<Corporation>();
			vs.addAll(c.vertexSet());
			int size = vs.size();
			for (int i = 0; i < size - 1; i++) {
				for (int j = i + 1; j < size; j++) {
					Corporation v1 = vs.get(i);
					Corporation v2 = vs.get(j);
					if (v2.equals(visited.get(v1)) || v1.equals(visited.get(v2)))// 说明已经计算过了
						continue;
					else {//标记为已计算
						visited.put(v1, v2);
						visited.put(v2, v1);
					}

					double Aij = (g.getEdge(v1, v2) == null && g.getEdge(v2, v1) == null) ? 0 : 1;
					double ki = g.degreeOf(v1);
					double kj = g.degreeOf(v2);
					double Oi = getComunitCount(v1, comunities);
					double Oj = getComunitCount(v2, comunities);
					Q += (Aij - ki * kj / 2.0 / m) / (Oi * Oj);

				}
			}
		}
		Q = Q / 2.0 / m;

		return Q;
	}

	private static double getComunitCount(Corporation v, List<Graphic> comunities) {
		int count = 0;
		for (Graphic c : comunities) {
			if (c.vertexSet().contains(v))
				count++;
		}
		return count;
	}

	private static boolean isInSameComunit(Corporation v1, Corporation v2, List<Graphic> comunities) {
		for (Graphic c : comunities) {
			Set<Corporation> vs = c.vertexSet();
			if (vs.contains(v1) && vs.contains(v2))
				return true;
		}
		return false;
	}

	// 局部Q
	public static double getJQ(Graphic g, List<Graphic> comunities) {
		// System.out.println("计算" + toStr(comunities) + "的模块度");
		double Q = 0;
		// 所有边
		double m = g.edgeSet().size();
		for (Graphic comunity : comunities) {
			double Ic = comunity.edgeSet().size();
			Graphic comunity_ = comunity.clone();
			comunity_.addEdgesFrom(comunity);

			double Dc = getComunityAllVertexsDegreeSum(comunity_, comunity);

			// System.out.println("Q=" + Q + "+" + "(" + Ic + "/" + m +
			// "-Math.pow(" + Dc + " / (2 * " + m + "))");
			Q = Q + (Ic / m - Math.pow(Dc / (2 * m), 2));
		}
		// System.out.println(comunities+"\t"+Q);
		return Q;
	}

	private static String toStr(List<Graphic> comunities) {
		String str = "";
		for (Graphic c : comunities)
			str += c.vertexSet();
		return str + "";
	}

	private static int getComunityAllVertexsDegreeSum(Graphic g, Graphic comunity) {
		int sum = 0;
		for (Corporation corp : comunity.vertexSet()) {
			sum = sum + g.inDegreeOf(corp) + g.outDegreeOf(corp);
		}
		return sum;
	}

}
