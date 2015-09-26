package cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jgraph.graph.Edge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.w3c.dom.Document;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Loop;
import cn.gov.cbrc.sd.dz.zhaorui.model.Unit;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.XMLToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3.Step3Module;;

public class PickAlgorithm extends HugeCircleSplitAlgorithm {
	private int condition_number_value;
	private int guarantor_floor_value;
	private boolean guarantor_floor_selected;
	private int mutually_guaranteed_floor_value;
	private boolean mutually_guaranteed_floor_selected;
	private int guaranteed_loan_balance_floor_value;
	private Unit guaranteed_loan_balance_floor_unit;
	private boolean guaranteed_loan_balance_floor_selected;
	private int loan_balance_floor_value;
	private Unit loan_balance_floor_unit;
	private boolean loan_balance_floor_selected;
	private boolean one_hand_vertex_all;
	private boolean one_hand_vertex_in_only;
	private boolean one_hand_vertex_none;
	private boolean two_hand_vertex_all;
	private boolean two_hand_vertex_in_only;
	private boolean two_hand_vertex_none;
	private boolean three_hand_vertex_all;
	private boolean three_hand_vertex_in_only;
	private boolean three_hand_vertex_none;
	private boolean unpick_corecorp_son;
	private boolean pick_mutually_guaranteed_corp;
	private boolean pick_corecorp_loop;

	public PickAlgorithm(String algorithm_name, Boolean algorithm_enable, Boolean algorithm_selected) throws Exception {

		super(algorithm_name, algorithm_enable, algorithm_selected);

		Document doc = Config.getDoc();

		condition_number_value = Integer.parseInt(XMLToolkit.getElementById(doc, "32").getAttribute("value"));

		guarantor_floor_value = Integer.parseInt(XMLToolkit.getElementById(doc, "6").getAttribute("value"));
		guarantor_floor_selected = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "6").getAttribute("selected"));

		mutually_guaranteed_floor_value = Integer.parseInt(XMLToolkit.getElementById(doc, "7").getAttribute("value"));
		mutually_guaranteed_floor_selected = Boolean
				.parseBoolean(XMLToolkit.getElementById(doc, "7").getAttribute("selected"));

		guaranteed_loan_balance_floor_value = Integer
				.parseInt(XMLToolkit.getElementById(doc, "8").getAttribute("value"));
		guaranteed_loan_balance_floor_unit = Unit.valueOf(XMLToolkit.getElementById(doc, "8").getAttribute("unit"));
		guaranteed_loan_balance_floor_selected = Boolean
				.parseBoolean(XMLToolkit.getElementById(doc, "8").getAttribute("selected"));

		loan_balance_floor_value = Integer.parseInt(XMLToolkit.getElementById(doc, "9").getAttribute("value"));
		loan_balance_floor_unit = Unit.valueOf(XMLToolkit.getElementById(doc, "9").getAttribute("unit"));
		loan_balance_floor_selected = Boolean
				.parseBoolean(XMLToolkit.getElementById(doc, "9").getAttribute("selected"));

		one_hand_vertex_all = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "12").getAttribute("selected"));
		one_hand_vertex_in_only = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "13").getAttribute("selected"));
		one_hand_vertex_none = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "14").getAttribute("selected"));

		two_hand_vertex_all = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "16").getAttribute("selected"));
		two_hand_vertex_in_only = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "17").getAttribute("selected"));
		two_hand_vertex_none = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "18").getAttribute("selected"));

		three_hand_vertex_all = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "20").getAttribute("selected"));
		three_hand_vertex_in_only = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "21").getAttribute("selected"));
		three_hand_vertex_none = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "22").getAttribute("selected"));

		unpick_corecorp_son = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "23").getAttribute("selected"));
		pick_mutually_guaranteed_corp = Boolean
				.parseBoolean(XMLToolkit.getElementById(doc, "24").getAttribute("selected"));
		pick_corecorp_loop = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "25").getAttribute("selected"));
	}

	public int getGuarantor_floor_value() {
		return guarantor_floor_value;
	}

	public void setGuarantor_floor_value(int guarantor_floor_value) {
		this.guarantor_floor_value = guarantor_floor_value;
	}

	public boolean isGuarantor_floor_selected() {
		return guarantor_floor_selected;
	}

	public void setGuarantor_floor_selected(boolean guarantor_floor_selected) {
		this.guarantor_floor_selected = guarantor_floor_selected;
	}

	public int getMutually_guaranteed_floor_value() {
		return mutually_guaranteed_floor_value;
	}

	public void setMutually_guaranteed_floor_value(int mutually_guaranteed_floor_value) {
		this.mutually_guaranteed_floor_value = mutually_guaranteed_floor_value;
	}

	public boolean isMutually_guaranteed_floor_selected() {
		return mutually_guaranteed_floor_selected;
	}

	public void setMutually_guaranteed_floor_selected(boolean mutually_guaranteed_floor_selected) {
		this.mutually_guaranteed_floor_selected = mutually_guaranteed_floor_selected;
	}

	public int getGuaranteed_loan_balance_floor_value() {
		return guaranteed_loan_balance_floor_value;
	}

	public void setGuaranteed_loan_balance_floor_value(int guaranteed_loan_balance_floor_value) {
		this.guaranteed_loan_balance_floor_value = guaranteed_loan_balance_floor_value;
	}

	public Unit getGuaranteed_loan_balance_floor_unit() {
		return guaranteed_loan_balance_floor_unit;
	}

	public void setGuaranteed_loan_balance_floor_unit(Unit guaranteed_loan_balance_floor_unit) {
		this.guaranteed_loan_balance_floor_unit = guaranteed_loan_balance_floor_unit;
	}

	public boolean isGuaranteed_loan_balance_floor_selected() {
		return guaranteed_loan_balance_floor_selected;
	}

	public void setGuaranteed_loan_balance_floor_selected(boolean guaranteed_loan_balance_floor_selected) {
		this.guaranteed_loan_balance_floor_selected = guaranteed_loan_balance_floor_selected;
	}

	public int getLoan_balance_floor_value() {
		return loan_balance_floor_value;
	}

	public void setLoan_balance_floor_value(int loan_balance_floor_value) {
		this.loan_balance_floor_value = loan_balance_floor_value;
	}

	public Unit getLoan_balance_floor_unit() {
		return loan_balance_floor_unit;
	}

	public void setLoan_balance_floor_unit(Unit loan_balance_floor_unit) {
		this.loan_balance_floor_unit = loan_balance_floor_unit;
	}

	public boolean isLoan_balance_floor_selected() {
		return loan_balance_floor_selected;
	}

	public void setLoan_balance_floor_selected(boolean loan_balance_floor_selected) {
		this.loan_balance_floor_selected = loan_balance_floor_selected;
	}

	public boolean isOne_hand_vertex_all() {
		return one_hand_vertex_all;
	}

	public void setOne_hand_vertex_all(boolean one_hand_vertex_all) {
		this.one_hand_vertex_all = one_hand_vertex_all;
	}

	public boolean isOne_hand_vertex_in_only() {
		return one_hand_vertex_in_only;
	}

	public void setOne_hand_vertex_in_only(boolean one_hand_vertex_in_only) {
		this.one_hand_vertex_in_only = one_hand_vertex_in_only;
	}

	public boolean isOne_hand_vertex_none() {
		return one_hand_vertex_none;
	}

	public void setOne_hand_vertex_none(boolean one_hand_vertex_none) {
		this.one_hand_vertex_none = one_hand_vertex_none;
	}

	public boolean isTwo_hand_vertex_all() {
		return two_hand_vertex_all;
	}

	public void setTwo_hand_vertex_all(boolean two_hand_vertex_all) {
		this.two_hand_vertex_all = two_hand_vertex_all;
	}

	public boolean isTwo_hand_vertex_in_only() {
		return two_hand_vertex_in_only;
	}

	public void setTwo_hand_vertex_in_only(boolean two_hand_vertex_in_only) {
		this.two_hand_vertex_in_only = two_hand_vertex_in_only;
	}

	public boolean isTwo_hand_vertex_none() {
		return two_hand_vertex_none;
	}

	public void setTwo_hand_vertex_none(boolean two_hand_vertex_none) {
		this.two_hand_vertex_none = two_hand_vertex_none;
	}

	public boolean isThree_hand_vertex_all() {
		return three_hand_vertex_all;
	}

	public void setThree_hand_vertex_all(boolean three_hand_vertex_all) {
		this.three_hand_vertex_all = three_hand_vertex_all;
	}

	public boolean isThree_hand_vertex_in_only() {
		return three_hand_vertex_in_only;
	}

	public void setThree_hand_vertex_in_only(boolean three_hand_vertex_in_only) {
		this.three_hand_vertex_in_only = three_hand_vertex_in_only;
	}

	public boolean isThree_hand_vertex_none() {
		return three_hand_vertex_none;
	}

	public void setThree_hand_vertex_none(boolean three_hand_vertex_none) {
		this.three_hand_vertex_none = three_hand_vertex_none;
	}

	public boolean isUnpick_corecorp_son() {
		return unpick_corecorp_son;
	}

	public void setUnpick_corecorp_son(boolean unpick_corecorp_son) {
		this.unpick_corecorp_son = unpick_corecorp_son;
	}

	public boolean isPick_mutually_guaranteed_corp() {
		return pick_mutually_guaranteed_corp;
	}

	public void setPick_mutually_guaranteed_corp(boolean pick_mutually_guaranteed_corp) {
		this.pick_mutually_guaranteed_corp = pick_mutually_guaranteed_corp;
	}

	public boolean isPick_corecorp_loop() {
		return pick_corecorp_loop;
	}

	public void setPick_corecorp_loop(boolean pick_corecorp_loop) {
		this.pick_corecorp_loop = pick_corecorp_loop;
	}

	public int getCondition_number_value() {
		return condition_number_value;
	}

	public void setCondition_number_value(int condition_number_value) {
		this.condition_number_value = condition_number_value;
	}

	public boolean updateConfigCache() throws Exception {

		Document doc = Config.getDoc();

		XMLToolkit.getElementById(doc, "32").setAttribute("value", String.valueOf(condition_number_value));

		XMLToolkit.getElementById(doc, "6").setAttribute("value", String.valueOf(guarantor_floor_value));
		XMLToolkit.getElementById(doc, "6").setAttribute("selected", String.valueOf(guarantor_floor_selected));

		XMLToolkit.getElementById(doc, "7").setAttribute("value", String.valueOf(mutually_guaranteed_floor_value));
		XMLToolkit.getElementById(doc, "7").setAttribute("selected",
				String.valueOf(mutually_guaranteed_floor_selected));

		XMLToolkit.getElementById(doc, "8").setAttribute("value", String.valueOf(guaranteed_loan_balance_floor_value));
		XMLToolkit.getElementById(doc, "8").setAttribute("unit", String.valueOf(guaranteed_loan_balance_floor_unit));
		XMLToolkit.getElementById(doc, "8").setAttribute("selected",
				String.valueOf(guaranteed_loan_balance_floor_selected));

		XMLToolkit.getElementById(doc, "9").setAttribute("value", String.valueOf(loan_balance_floor_value));
		XMLToolkit.getElementById(doc, "9").setAttribute("unit", String.valueOf(loan_balance_floor_unit));
		XMLToolkit.getElementById(doc, "9").setAttribute("selected", String.valueOf(loan_balance_floor_selected));

		XMLToolkit.getElementById(doc, "12").setAttribute("selected", String.valueOf(one_hand_vertex_all));
		XMLToolkit.getElementById(doc, "13").setAttribute("selected", String.valueOf(one_hand_vertex_in_only));
		XMLToolkit.getElementById(doc, "14").setAttribute("selected", String.valueOf(one_hand_vertex_none));

		XMLToolkit.getElementById(doc, "16").setAttribute("selected", String.valueOf(two_hand_vertex_all));
		XMLToolkit.getElementById(doc, "17").setAttribute("selected", String.valueOf(two_hand_vertex_in_only));
		XMLToolkit.getElementById(doc, "18").setAttribute("selected", String.valueOf(two_hand_vertex_none));

		XMLToolkit.getElementById(doc, "20").setAttribute("selected", String.valueOf(three_hand_vertex_all));
		XMLToolkit.getElementById(doc, "21").setAttribute("selected", String.valueOf(three_hand_vertex_in_only));
		XMLToolkit.getElementById(doc, "22").setAttribute("selected", String.valueOf(three_hand_vertex_none));

		XMLToolkit.getElementById(doc, "23").setAttribute("selected", String.valueOf(unpick_corecorp_son));
		XMLToolkit.getElementById(doc, "24").setAttribute("selected", String.valueOf(pick_mutually_guaranteed_corp));
		XMLToolkit.getElementById(doc, "25").setAttribute("selected", String.valueOf(pick_corecorp_loop));

		return true;
	}

	/**
	 * 对超大圈拆分的具体工作在这里进行
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@Override
	protected List<Graphic> splitHugeCircleImpl(Graphic graphic) throws Exception {
		List<Graphic> gs = new ArrayList<Graphic>();
		// 对核心企业节点进行标记
		markCoreCorp(graphic);
		// 获取所有核心企业
		Set<Corporation> coreCorps = getCoreCorps(graphic);
		// 对每个核心企业，拉取其担保圈
		int i = 0;
		for (Corporation corp : coreCorps) {
			i++;
			Graphic g = pickCircleOf(graphic, corp);
			((Step3Module) (GC.getGalileo().getModule("3"))).getProcedure()
					.setPercent((int) (i * 1.0 / coreCorps.size() * 100));
			gs.add(g);
		}
		return gs;
	}

	/**
	 * 在超大圈graphic中拉取以core为核心节点的担保圈
	 * 
	 * @param graphic
	 *            超大圈
	 * @param corp
	 * @return
	 * @throws Exception
	 */
	private Graphic pickCircleOf(Graphic graphic, Corporation core) throws Exception {
		// 用来存放待会拉出来的担保圈图
		Graphic g = new Graphic();
//		g.setNameSuffix(core.getName());
		g.setName(core.getName()+"圈");
		// 先把核心节点放到g里
		g.addVertex(core);
		// 拉取第一层节点
		Set<Corporation> corpsOfLevel1 = pickNextLevel(graphic, g, g.vertexSet(), "One");
		// 拉取第二层节点
		Set<Corporation> corpsOfLevel2 = pickNextLevel(graphic, g, corpsOfLevel1, "Two");
		// 拉取第三层节点
		Set<Corporation> corpsOfLevel3 = pickNextLevel(graphic, g, corpsOfLevel2, "Three");
		// 如果“互保企业必取之”的条件被选中的话，则拉取核心企业及其N手互保企业的所有互保企业
		if (this.isPick_mutually_guaranteed_corp()) {
			pickMutuallyGuaranteedCorps(graphic, g, core);
		}
		// 如果“环上企业必取之”的条件被选中的话，则拉取核心企业所在环上的所有企业
		if (this.isPick_corecorp_loop()) {
			pickCorecorpLoop(graphic, g, core);
		}

		// 尝试将g中企业的每一条边加入g中
		g.addEdgesFrom(graphic);

		return g;
	}

	/**
	 * 拉取核心企业所在环上的所有企业（只取环路径长度≤loopLengthCeilling的环）
	 * 
	 * @param graphic
	 * @param g
	 * @param core
	 */
	private void pickCorecorpLoop(Graphic graphic, Graphic g, Corporation core) {
		int loopLengthCeilling = 4;
		Set<Loop> loops = graphic.loopsOf(core, loopLengthCeilling);
		for (Graphic loop : loops) {
			Set<Corporation> vs = loop.vertexSet();
			for (Corporation v : vs) {
				g.addVertex(v);
			}
		}
	}

	/**
	 * 拉取corps的下一层圈，通常corps是某层圈的所有企业集合
	 * 
	 * @param graphic
	 *            超大圈
	 * @param g
	 *            核心企业担保圈
	 * @param levelToPick
	 *            待拉取层次
	 * @param core
	 *            核心企业
	 * @return
	 * @throws Exception
	 */
	private Set<Corporation> pickNextLevel(Graphic graphic, Graphic g, Set<Corporation> corps, String levelToPick)
			throws Exception {
		int levelTopick = levelToPick.equals("One") ? 1 : (levelToPick.equals("Two") ? 2 : 3);
		Set<Corporation> corpsOfLevelX = new HashSet<Corporation>();
		// 如果是“全不取”被选中
		if ((Boolean) getClass().getMethod("is" + levelToPick + "_hand_vertex_none").invoke(this))
			return corpsOfLevelX;// 直接返回空集
		else {
			for (Corporation corp : corps) {// 对于每一个节点
				// 如果用户选择“不取核心节点的后手的话”，就不取核心节点的后手
				if (this.isUnpick_corecorp_son() == true && levelTopick > 1 && corp.isCore() == true)
					continue;
				// 获取与该点接触的所有边
				Set<DefaultWeightedEdge> edges = graphic.edgesOf(corp);
				for (DefaultWeightedEdge edge : edges) {// 对每一条边
					Corporation corpS = graphic.getEdgeSource(edge);// 边的头顶点
					Corporation corpT = graphic.getEdgeTarget(edge);// 边的尾顶点
					Corporation corp2 = corpS.equals(corp) == false ? corpS : corpT;// core的对端顶点
					// 如果是“全部”被选中
					if ((Boolean) getClass().getMethod("is" + levelToPick + "_hand_vertex_all").invoke(this)) {
						if (g.containsVertex(corp2) == false) {
							// 将每条边的另一端的那个节点加到g中
							g.addVertex(corp2);
							corpsOfLevelX.add(corp2);
						}
					}
					// 如果是“只取为核心企业提供担保的企业”
					if ((Boolean) getClass().getMethod("is" + levelToPick + "_hand_vertex_in_only").invoke(this)) {
						if (corpT == corp) {
							if (g.containsVertex(corpS) == false) {
								// 将core作为尾顶点时的对端顶点加入g中
								g.addVertex(corpS);
								corpsOfLevelX.add(corpS);
							}
						}
					}
				}
			}
			return corpsOfLevelX;
		}
	}

	/**
	 * 拉取核心企业及其N手互保企业的所有互保企业
	 * 
	 * @param graphic
	 *            超大圈
	 * @param g
	 *            核心企业担保圈
	 * @param core
	 *            核心企业
	 */
	private void pickMutuallyGuaranteedCorps(Graphic graphic, Graphic g, Corporation core) {
		Queue<Corporation> queue = new LinkedList<Corporation>();
		// 将核心节点加入队列中
		addToQueue(queue, core);
		core.setLevel(0);
		// 将核心节点加入g中
		g.addVertex(core);
		while (queue.isEmpty() == false) {// 当队列不为空，就一直做
			Corporation corp = queue.poll();// 从队列中取出一个企业
			int corpLevel = corp.getLevel();
			if (corpLevel >= 3) // 如果层级超出设定值，则不取它的后手
				continue;
			Set<Corporation> mutuallyVertexs = graphic.mutuallyVertexsOf(corp);// 取该企业的所有互保企业
			for (Corporation mutuallyVertex : mutuallyVertexs) {
				if (g.containsVertex(mutuallyVertex) == false) {// 如果互保企业尚未被加入到g中的话
					// 将互保企业加入g中
					g.addVertex(mutuallyVertex);
					if (mutuallyVertex.isCore() == false) {// 将非核心互保企业加入队列中
						addToQueue(queue, mutuallyVertex);
						mutuallyVertex.setLevel(corpLevel + 1);
					}
				}
			}
		}

	}

	private void markCoreCorp(Graphic graphic) throws Exception {
		Iterator<Corporation> iterator = graphic.vertexSet().iterator();
		while (iterator.hasNext()) {
			Corporation corp = iterator.next();
			int conditionMeetCount = 0;// 实际满足的条件个数
			if (this.isGuarantor_floor_selected()) {// 若条件1被启用的话
				// 如果企业被[guarantor-floor.value](含)家以上企业担保，则可以认定为核心企业
				if (graphic.inDegreeOf(corp) >= this.getGuarantor_floor_value()) {
					conditionMeetCount++;
				}
			}
			if (this.isMutually_guaranteed_floor_selected()) {// 若条件2被启用的话
				// 如果企业与[mutually-guaranteed-floor.value](含)家以上企业存在互保关系，则可以认定为核心企业
				if (graphic.mutuallyDegreeOf(corp) >= this.getMutually_guaranteed_floor_value()) {
					conditionMeetCount++;
				}
			}
			// 条件3：如果“企业的被担保贷款余额>=[guaranteed-loan-balance-floor.value]”,则认定为核心企业
			// TODO 条件3暂未启用
			if (this.isLoan_balance_floor_selected()) {// 若条件4被启用的话
				// 如果“企业的贷款余额>=[loan-balance-floor.value]”，则可以认定为 核心企业
				if (corp.getDoubleValue(Corporation.LOAN_BALANCE_COL) >= this.getLoan_balance_floor_value()
						* this.getLoan_balance_floor_unit().getMultiple()) {
					conditionMeetCount++;
				}
			}
			// 如果实际满足的条件个数超过用户定义的下限的话，则认定为核心企业
			if (conditionMeetCount >= this.getCondition_number_value())
				corp.setCore(true);
			else
				corp.setCore(false);
		}

		System.out.println("核心企业数为：" + getCoreCorpCount(graphic));
	}

	/**
	 * 在调用此方法前要确保调用过markCoreCorp()方法。
	 * 
	 * @param graphic
	 * @return
	 */
	public int getCoreCorpCount(Graphic graphic) {
		return getCoreCorps(graphic).size();
	}

	/**
	 * 在调用此方法前要确保调用过markCoreCorp()方法。
	 * 
	 * @param graphic
	 * @return
	 */
	public Set<Corporation> getCoreCorps(Graphic graphic) {
		Set<Corporation> result = new HashSet<Corporation>();
		Iterator<Corporation> iterator = graphic.vertexSet().iterator();
		while (iterator.hasNext()) {
			Corporation corp = iterator.next();
			if (corp.isCore())
				result.add(corp);
		}
		return result;
	}

}
