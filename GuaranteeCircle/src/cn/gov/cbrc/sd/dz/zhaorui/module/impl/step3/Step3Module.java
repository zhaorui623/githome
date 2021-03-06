package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JMenuItem;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.GraphicClassifyAnalysis;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.RegionDistributAnalysis;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl.PickAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.VIPCustomerGroup;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1.Step1Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2.Step2Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.ResultPanel;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;

public class Step3Module extends Module {

	ProcessPanel pPanel;

	private HugeCircleSplitAlgorithm hcsAlgm;

	private Graphic totalGraphic;

	private boolean sucessMark;

	public Step3Module(String id, GC gc, String name, String iconName) {
		super(id, gc, name, iconName);
	}

	@Override
	protected List<Component> getTabs() {
		if (tabs == null) {
			tabs = new ArrayList<Component>();
			// "第三步：识别过程"选项卡
			pPanel = new ProcessPanel(this);
			tabs.add(pPanel);
		}
		return tabs;
	}

	@Override
	protected void initMenuItems() {
		menuitems.add(new JMenuItem(this.getName()));
	}

	public GC getGC() {
		return this.gc;
	}

	public Procedure procedure = new Procedure();

	/**
	 * 第1步：识别独立连通子图
	 * 
	 * @throws Exception
	 */
	private List<Graphic> graphics;

	@SuppressWarnings("static-access")
	public void procedure1() throws Exception {
		this.hcsAlgm = ((Step2Module) Module.getModule("2")).getHcsAlgm();
		this.totalGraphic = ((Step1Module) Module.getModule("1")).getTotalGraphic().clone();
		// 得到独立连通子图们
		graphics = hcsAlgm.split(totalGraphic);
		InfoPane.getInstance().info("共识别出连通子图" + graphics.size() + "个");
		// 统计一下复杂连通子图的个数，展示一下
		int count = 0;
		for (Graphic g : graphics) {
			int vertexCount = g.vertexSet().size();
			int edgeCount = g.edgeSet().size();
			if (edgeCount * 1.0 / vertexCount > 1.5 || edgeCount + vertexCount > 10)
				count++;
			else
				continue;
		}
		InfoPane.getInstance().info("其中复杂连通子图（担保圈）" + count + "个");
	}

	/**
	 * 第2步：对超大圈进行拆分
	 * 
	 * @throws Exception
	 */
	public void procedure2() throws Exception {
		List<Graphic> gs = new ArrayList<Graphic>();

		// //结果展示用，计数器清零
		// ResultPanel.hugeCirclesCount=0;
		// ResultPanel.hugeCirclesVertexCount=0;
		// ResultPanel.hugeCirclesLoanBalance=0;
		ResultPanel.hugeCircles = new ArrayList<Graphic>();

		for (Graphic graphic : graphics) {
			// if(graphic.getName().equals("独立担保圈78-茌平县冯屯镇东来冷藏加工厂圈")){
			List<Graphic> sonGrapihcs = hcsAlgm.splitHugeCircle(graphic);
			gs.addAll(sonGrapihcs);
		}
		// }
		graphics = gs;
	}

	/**
	 * 第5步：过滤掉非圈非链、小圈小链
	 * 
	 * @throws Exception
	 */
	public void procedure5() throws Exception {
		int size1 = graphics.size();
		Iterator<Graphic> iterator = graphics.iterator();
		while (iterator.hasNext()) {
			Graphic g = iterator.next();
			if (g.isVIPGraphic() == true) // 重点风险客户担保圈不能被过滤掉，用户就想看这个，过滤掉会捉急的
				continue;
			int vertexCount = g.vertexSet().size();
			int edgeCount = g.edgeSet().size();
			// 如果平均每个节点超过1.5条边 或者 点+边总数超过10的话，则保留，否则丢弃
			if (!(edgeCount * 1.0 / vertexCount > 1.5 || edgeCount + vertexCount > 10)) {
				iterator.remove();
				continue;
			}
			// 如果独立担保圈内所有企业均达不到核心企业标准，则丢弃该圈
			// if (g.getName().contains("独立担保圈")) {
			// ((PickAlgorithm)hcsAlgm).markCoreCorp(g);
			// if (g.getCoreCorps().size() == 0) {
			// iterator.remove();
			// continue;
			// }
			// }

		}
		int size2 = graphics.size();
		InfoPane.getInstance().info("共过滤掉" + (size1 - size2) + "个非圈非链、小圈小链，最终剩余担保圈" + size2 + "个");
	}

	/**
	 * 第8步：生成每个担保圈的拓扑图
	 * 
	 * @throws Exception
	 */
	public void procedure8() throws Exception {
		InfoPane.getInstance().info("为" + graphics.size() + "个担保圈生成图像文件……");
		final BlockingQueue<Graphic> queue = new LinkedBlockingQueue<Graphic>(graphics.size());
		for (Graphic g : graphics)
			queue.put(g);
		new Thread() {
			public void run() {
				while (queue.isEmpty() == false) {
					getProcedure().setPercent((int) ((1 - queue.size() * 1.0 / graphics.size()) * 100));
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		while (queue.isEmpty() == false) {
			Graphic g = queue.take();
			g.toFile(new File(GC.getOutputDir().getAbsolutePath() + "\\担保圈图\\"), "png", false);
		}
	}

	/**
	 * 第9步：生成每个超大担保圈的拓扑图
	 * 
	 * @throws Exception
	 */
	public void procedure9() throws Exception {
		if(ResultPanel.hugeCircles.size()==0)
			return;
		InfoPane.getInstance().info("为" + ResultPanel.hugeCircles.size() + "个超大担保圈生成图像文件……");
		final BlockingQueue<Graphic> queue = new LinkedBlockingQueue<Graphic>(ResultPanel.hugeCircles.size());
		for (Graphic g : ResultPanel.hugeCircles)
			queue.put(g);
		new Thread() {
			public void run() {
				while (queue.isEmpty() == false) {
					getProcedure().setPercent((int) ((1 - queue.size() * 1.0 / ResultPanel.hugeCircles.size()) * 100));
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		while (queue.isEmpty() == false) {
			Graphic g = queue.take();
			g.toFile(new File(GC.getOutputDir().getAbsolutePath() + "\\超大担保圈图\\"), "svg", true);
		}
	}

	/**
	 * 第6步：从地区分布维度分析
	 * 
	 * @throws Exception
	 */
	public void procedure6() throws Exception {
		RegionDistributAnalysis rda = new RegionDistributAnalysis();
		for (Graphic g : graphics) {
			rda.analysisRegion(g);
		}
		if (ResultPanel.hugeCircles == null) {
			ResultPanel.hugeCircles = new ArrayList<Graphic>();
		}
		for (Graphic g : ResultPanel.hugeCircles) {
			rda.analysisRegion(g);
		}

		// //只保留德州地区
		// Iterator<Graphic> iter=graphics.iterator();
		// while(iter.hasNext()){
		// Graphic g=iter.next();
		// if(g.getRegion().getName().contains("德州")==false)
		// iter.remove();
		// }
	}

	/**
	 * 第3步：从重点客户维度分析
	 * 
	 * @throws Exception
	 */
	public void procedure3() throws Exception {
		if (graphics == null)
			graphics = new ArrayList<Graphic>();

		boolean skip = ((Step1Module) Module.getModule("1")).isSkipVIPCustomerAnlys();
		if (skip == true) {
			InfoPane.getInstance().info("跳过第3步：从重点客户维度分析");
			return;
		}

		Graphic totalG = ((Step1Module) Module.getModule("1")).getTotalGraphic().clone();
		List<VIPCustomerGroup> list = ((Step1Module) Module.getModule("1")).getVIPCustomerGroupList();

		for (int i = 0; i < list.size(); i++) {
			procedure.setPercent((int) (i * 1.0 / list.size() * 100));
			// System.out.println(procedure.getPercent());
			VIPCustomerGroup group = list.get(i);
			Graphic graphic = new Graphic();// 重点客户担保圈
			graphic.setName("重点风险企业担保圈" + (i + 1) + "-" + group.getName() + "圈");
			List<Corporation> corps = group.getCorps();// 重点客户组内的客户清单
			if (corps == null) {
				InfoPane.getInstance().info("未发现重点风险企业" + group.getName() + "圈内企业与其他企业存在担保关系，该圈将不被生成");
				continue;
			}
			Set<Corporation> vertexSet = new HashSet<Corporation>();
			for (Corporation corp : corps) {// 拉取客户清单中每个客户的相关企业，全部加到vertexSet里
				corp.setCore(true);
				Graphic g = ((PickAlgorithm) hcsAlgm).pickCircleOf(totalG, corp);
				graphic.addCoreCorps(corp);
				vertexSet.addAll(g.vertexSet());
				GraphicToolkit.removeGraphicWhosCoreCorpis(corp, graphics);
			}
			for (Corporation vertex : vertexSet)
				graphic.addVertex(vertex);
			graphic.addEdgesFrom(totalG);
			graphic.setVIPTag(true);
			graphics.add(graphic);
		}
	}

	/**
	 * 第7步：从风险分类维度分析
	 * 
	 * @throws Exception
	 */
	public void procedure7() throws Exception {
		GraphicClassifyAnalysis rda = new GraphicClassifyAnalysis();
		for (Graphic g : graphics) {
			rda.analysisRiskClassify(g);
		}
	}

	/**
	 * 第4步：高关联度担保圈合并
	 * 
	 * @throws Exception
	 */
	public void procedure4() throws Exception {
		// if (0 == 0)
		// return;
		final SimpleDirectedWeightedGraph<Graphic, DefaultWeightedEdge> mergeTree = new SimpleDirectedWeightedGraph(
				DefaultWeightedEdge.class);

		int count = 0;
		Iterator<Graphic> iterator = graphics.iterator();
		while (iterator.hasNext()) {
			Graphic g = iterator.next();
			if (g.isVIPGraphic()) // 只处理自动生成的担保圈，根据重点风险客户形成的担保圈不处理
				continue;
			Corporation heaviestCorp = g.getHeaviestCoreVertex();// 取图中最重的节点,并且该节点是核心节点(广义的核心节点，不一定是该图的核心节点)
			if (heaviestCorp == null) // 说明该图中没有核心节点（可能是独立担保圈），就不处理了
				continue;
			if (g.getCoreCorps().contains(heaviestCorp)) // 如果最重的节点本身就是该图的核心企业，就不用处理了
				continue;
			else {// 否则，寻找以最重节点为核心企业的担保圈，并准备把当前图合并进该担保圈中
				Graphic graphic = GraphicToolkit.getCircleWhosCoreCorpis(heaviestCorp, graphics);
				if (graphic != null) {
					mergeTree.addVertex(graphic);
					mergeTree.addVertex(g);
					mergeTree.addEdge(g, graphic);
					count++;
				}
			}
		}
		// Thread t=new Thread() {
		// public void run() {
		// try {
		// System.out.println("生成合并路线图");
		GraphicToolkit.toFile(mergeTree, new File(GC.getOutputDir().getAbsolutePath() + "\\担保圈图\\"), "合并路线图", "png");
		// System.out.println("生成合并路线图成功");
		// } catch (Exception e) {
		// InfoPane.getInstance().warn("担保圈合并路线图生成失败");
		// }
		// }
		// };
		// t.start();
		// 合并担保圈，并将被合并的担保圈从graphics中移除
		Set<Graphic> graphicToRemove = GraphicToolkit.mergeCircles(mergeTree);
		graphics.removeAll(graphicToRemove);

		InfoPane.getInstance().info("合并了" + count + "个担保圈");
	}

	public Procedure getProcedure() {
		return procedure;
	}

	public ProcessPanel getpPanel() {

		return pPanel;
	}

	public List<Graphic> getResultGraphics() {
		return graphics;
	}

	public void clearProcedueStatusMark() {
		pPanel.clearProcedueStatusMark();
	}

	public void clearSucessMark() {
		this.setSucessMark(false);
	}

	public boolean isSucess() {
		return this.sucessMark;
	}

	public void setSucessMark(boolean b) {
		this.sucessMark = b;
	}

	public void resetData() {
		this.graphics=null;
		this.hcsAlgm = ((Step2Module) Module.getModule("2")).getHcsAlgm();
		ResultPanel.hugeCircles=null;
	}
}
