package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2.Step2Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1.Step1Module;

public class Step3Module extends Module {

	ProcessPanel pPanel;

	private HugeCircleSplitAlgorithm hcsAlgm;

	private Graphic totalGraphic;

	public static final int PROCEDURE_COUNT = 7;

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

	private Procedure procedure = new Procedure();

	/**
	 * 第一步：识别独立连通子图
	 * 
	 * @throws Exception
	 */
	List<Graphic> graphics;

	@SuppressWarnings("static-access")
	public void procedure1() throws Exception {
		this.hcsAlgm = ((Step2Module) Module.getModule("2")).getHcsAlgm();
		this.totalGraphic = ((Step1Module) Module.getModule("1")).getTotalGraphic();
		// 得到独立连通子图们
		graphics = hcsAlgm.split(totalGraphic);
		InfoPane.getInstance().info("共识别出连通子图" + graphics.size() + "个");
	}

	public void procedure2() throws Exception {
		InfoPane.getInstance().info("执行procedure2！");
	}

	/**
	 * 过滤掉非圈非链、小圈小链
	 * 
	 * @throws Exception
	 */
	public void procedure3() throws Exception {
		int size1=graphics.size();
		Iterator<Graphic> iterator = graphics.iterator();
		while (iterator.hasNext()) {
			Graphic g = iterator.next();
			int vertexCount = g.vertexSet().size();
			int edgeCount = g.edgeSet().size();
			//如果平均每个节点超过1.5条边  或者  点+边总数超过10的话，则保留
			if (edgeCount * 1.0 / vertexCount > 1.5||edgeCount+vertexCount>10)
				;
			else//否则，丢弃
				iterator.remove();
		}		
		int size2=graphics.size();
		InfoPane.getInstance().info("共过滤掉" + (size1-size2) + "个非圈非链、小圈小链，最终剩余担保圈"+size2+"个");
	}

	public void procedure4() throws Exception {
		Iterator<Graphic> iterator = graphics.iterator();
		while (iterator.hasNext()) {
			Graphic g = iterator.next();
			g.toFile(new File(System.getProperty("user.dir")+"\\担保圈图\\"));
		}
	}

	public void procedure5() throws Exception {
		InfoPane.getInstance().info("执行procedure5！");

	}

	public void procedure6() throws Exception {
		InfoPane.getInstance().info("执行procedure6！");

	}

	public void procedure7() throws Exception {
		InfoPane.getInstance().info("执行procedure7！");
	}

	public Procedure getProcedure() {
		return procedure;
	}

	public ProcessPanel getpPanel() {

		return pPanel;
	}

}
