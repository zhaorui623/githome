package cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;

import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;

public class SeedMergeAlgorithm extends HugeCircleSplitAlgorithm {

	public SeedMergeAlgorithm(String algorithm_name, Boolean algorithm_enable, Boolean algorithm_selected)
			throws Exception {

		super(algorithm_name, algorithm_enable, algorithm_selected);
	}

	public static void main(String[] args) throws Exception {
		for (int size = 8; size <= 8; size = size + 8) {
			long start = System.currentTimeMillis();

			Graphic g = new Graphic();
			Corporation v[] = new Corporation[size];
			for (int i = 0; i < v.length; i++) {
				v[i] = Corporation.createDefaultCorp("" + i, "" + i);
				v[i].getDatas().put(Corporation.LOAN_BALANCE_COL, i * 1.0);
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
			List<Graphic> sons = new SeedMergeAlgorithm(null, true, true).splitHugeCircleImpl(g);
			for (Graphic son : sons) {
				System.out.println(son.vertexSet());
			}
			System.out.println(
					"m+n=" + (g.vertexSet().size() + g.edgeSet().size()) + "耗时" + (System.currentTimeMillis() - start));
		}
	}

	public List<Graphic> splitHugeCircleImpl(Graphic g) throws Exception {
		List<Graphic> sonGraphics = new ArrayList<Graphic>();
for(double bbb=0.7;bbb<=0.7;bbb=bbb+0.05){
	sonGraphics.clear();
		Set<Corporation> vs = g.vertexSet();
		int N = vs.size();
		// 找贷款余额占整个超大圈贷款余额80%的前N个节点
		// 排序
		List<Corporation> vs_ordered = GraphicToolkit.getMaxNLoanBalanceVertexs(g, N);
		double totalLoanBalance = GraphicToolkit.getLoanBalance(g);
		int i = 0;
		double t = 0;
		for (; i < vs_ordered.size(); i++) {
			t = t + vs_ordered.get(i).getLoanBalance();
			if (t / totalLoanBalance > 0.8)
				break;
		}
		i = (i == vs_ordered.size()) ? (i - 1) : i;
		// 前i个节点贷款余额占整个超大圈贷款余额90%
//		InfoPane.getInstance().info("贷款余额前" + i + "名的企业贷款余额之和占该超大担保圈贷款余额的80%以上");
		// 取这些节点的N手圈
		List<Graphic> comunits = new LinkedList<Graphic>();
		for (int j = 0; j <i; j++) {
			Graphic comunit = PickAlgorithm.pickCircleOf(g, vs_ordered.get(j));
			comunits.add(comunit);
		}
		int size = comunits.size();
		while (true) {
			double maxsimilarity = -1.0;
			Graphic maxg1 = null, maxg2 = null;
			int maxg2Index = -1;
			// 两两计算社区相似度，合并相似度超过bbb的社区中最相似的两个
			for (int m = 0; m < comunits.size(); m++) {
				for (int n = m + 1; n < comunits.size(); n++) {
					Graphic gm = comunits.get(m);
					Graphic gn = comunits.get(n);
					double similarity = similarity(gm, gn);
					// if(similarity-0>0.0001)
					// System.out.println(similarity);
					if (similarity > bbb && similarity >= maxsimilarity) {//先合并小的
						maxsimilarity = similarity;
						maxg1 = gm;
						maxg2 = gn;
						maxg2Index = n;
					}
				}
			}

			if (maxg1 != null) {
				maxg1.absorb(maxg2);
				comunits.remove(maxg2Index);
			}

//			double q=FNAlgorithm.getEQ(g,comunits);
//			InfoPane.getInstance().info("社区数量："+comunits.size()+"模块度为EQ="+q);
			int newsize = comunits.size();
			if (size == newsize)
				break;
			else
				size = newsize;
		}
		for (Graphic c : comunits) {
//			Corporation h = c.getMaxDegreeVertex();
//			c.setName(h.getName() + "圈");
//			c.getCoreCorps().clear();
//			c.addCoreCorps(h);
			sonGraphics.add(c);
		}

		double q=FNAlgorithm.getEQ(g,sonGraphics);
		InfoPane.getInstance().info("阈值为"+bbb+"\t子担保圈个数为"+sonGraphics.size()+"\t模块度为EQ="+q);
}
		return sonGraphics;
	}

	private double similarity(Graphic g1, Graphic g2) {
		Set<Corporation> v1 = g1.vertexSet();
		Set<Corporation> v2 = g2.vertexSet();
//		Set<Corporation> vsum=new HashSet<Corporation>();
//		vsum.addAll(v1);
//		vsum.addAll(v2);		
//		int sum = vsum.size();
//		if (sum == 0)
//			return 1;
		int i = 0;
		for (Corporation v : v1) {
			if (v2.contains(v))
				i++;
		}
//		double similarity = i * 1.0 / sum;
		double similarity=Math.max(i*1.0/v1.size(), i*1.0/v2.size());
		return similarity;
	}

}
