package cn.gov.cbrc.sd.dz.zhaorui.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.JPanel;

import org.jgrapht.graph.DefaultWeightedEdge;

import cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl.PickAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2.PickAlgorithmConfigPanel;

public abstract class HugeCircleSplitAlgorithm {

	protected static int huge_circle_vertex_floor;

	protected String algorithm_name;

	protected boolean algorithm_enable;

	protected boolean algorithm_selected;

	protected JPanel algorithmConfigPanel;

	protected boolean isAlgorithmConfigPanelInited = false;

	public HugeCircleSplitAlgorithm(){
		super();
	}
	
	public HugeCircleSplitAlgorithm(String algorithm_name, Boolean algorithm_enable, Boolean algorithm_selected) {
		super();
		this.algorithm_name = algorithm_name;
		this.algorithm_enable = algorithm_enable;
		this.algorithm_selected = algorithm_selected;
	}

	public String getAlgorithm_name() {
		return algorithm_name;
	}

	public void setAlgorithm_name(String algorithm_name) {
		this.algorithm_name = algorithm_name;
	}

	public boolean isAlgorithm_enable() {
		return algorithm_enable;
	}

	public void setAlgorithm_enable(boolean algorithm_enable) {
		this.algorithm_enable = algorithm_enable;
	}

	public boolean isAlgorithm_selected() {
		return algorithm_selected;
	}

	public void setAlgorithm_selected(boolean algorithm_selected) {
		this.algorithm_selected = algorithm_selected;
	}

	public static int getHuge_circle_vertex_floor() {
		return huge_circle_vertex_floor;
	}

	public static void setHuge_circle_vertex_floor(int huge_circle_vertex_floor) {
		HugeCircleSplitAlgorithm.huge_circle_vertex_floor = huge_circle_vertex_floor;
	}

	public JPanel getAlgorithmConfigPanel() {
		if (isAlgorithmConfigPanelInited == false) {
			if (algorithmConfigPanel instanceof PickAlgorithmConfigPanel) {
				((PickAlgorithmConfigPanel) algorithmConfigPanel).init();
			}
			isAlgorithmConfigPanelInited = true;
		}
		return algorithmConfigPanel;
	}

	public void setAlgorithmConfigPanel(JPanel algorithmConfigPanel) {
		this.algorithmConfigPanel = algorithmConfigPanel;
		if (algorithmConfigPanel instanceof PickAlgorithmConfigPanel)
			((PickAlgorithmConfigPanel) algorithmConfigPanel).setAlgorithm((PickAlgorithm) this);
	}

	/**
	 * 用广度优先搜索的方法，识别传入参数graphic的所有连通子图，放到一个List里返回
	 * 
	 * @return
	 */
	public static List<Graphic> split(Graphic graphic) {
		// 用来存放待会拆出来的所有子图
		List<Graphic> result = new ArrayList<Graphic>();
		// 当graphic里尚有节点时，就一直做这个循环，这是为了拆出所有连通子图
		int i=0;
		while (graphic.vertexSet().isEmpty() == false) {
			System.out.println("尚有"+graphic.vertexSet().size()+"个节点");
			// 用来存放待会拆出来的一个子图
			Graphic g = new Graphic();
			// 随便取一个节点,加到一个队列里，并同时加到子图g中
			Queue<Corporation> queue = new LinkedList<Corporation>();
			Corporation corpRandom = graphic.vertexSet().iterator().next();
			addToQueue(queue, corpRandom);
			g.addVertex(corpRandom);
			// 当队列不为空时，就一直做这个循环，这是为了得到一个连通子图
			while (queue.isEmpty() == false) {
				// 从队列中弹出一个节点
				Corporation corp = queue.poll();
				// 如果图中已经不存在这个节点了，说明压入队列后在处理其它节点时发现它已经满足移除出总图的条件了，就把它移除了，所以此处就没有再处理的必要了
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
			g.setName("独立担保圈"+(++i)+"-"+g.getHeaviestVertex().getName()+"圈");
			System.out.println(g.getName()+"[节点数"+g.vertexSet().size()+"]");
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

	/**
	 * 超大圈拆分算法
	 * 
	 * @param graphic
	 *            超大圈
	 * @return 如果graphic是超大圈，则返回拆分后的子图集合；如果graphic不是超大圈，则返回的集合中只包含graphic自己
	 * @throws Exception 
	 */
	public List<Graphic> splitHugeCircle(Graphic graphic) throws Exception {
		List<Graphic> gs = new ArrayList<Graphic>();
		if (isHugeCircle(graphic)) {// 如果确实是超大圈，则拆分之
			List<Graphic> sonGrpahicsOfHugeCircle=splitHugeCircleImpl(graphic);
			InfoPane.getInstance().info("发现超大圈，它最终被拆分成了"+sonGrpahicsOfHugeCircle.size()+"个子圈");
			gs.addAll(sonGrpahicsOfHugeCircle);
			
		} else {// 如果不是超大圈，则直接加入到结果集合中
			gs.add(graphic);
		}
		return gs;
	}

	/**具体拆分算法，此方法需要调用具体实现类的方法
	 * 	
	 * @param graphic
	 * @throws Exception 
	 */
	protected abstract List<Graphic> splitHugeCircleImpl(Graphic graphic) throws Exception;

	/**
	 * 判断一个圈是不是超大圈
	 * 
	 * @param graphic
	 * @return
	 */
	private boolean isHugeCircle(Graphic graphic) {
		//如果圈内节点数超过了[超大圈节点数下限]，则说明可以认定为超大圈
		if (graphic.vertexSet().size() >= this.getHuge_circle_vertex_floor())
			return true;
		else
			return false;
	}
}
