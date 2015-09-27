package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.exception.ErrorDialog;
import cn.gov.cbrc.sd.dz.zhaorui.exception.TimerErrorDialog;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.*;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.Panel1;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2.Panel2;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel4.Panel4;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.InvisibleNode;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.InvisibleTreeModel;

@SuppressWarnings("serial")
public class ResultPanel extends JPanel {

	private GC GC = null;
	private JTabbedPane tabbedPane;
	private JPanel panel3;
	private Panel1 panel1;
	private Panel2 panel2;
	private Panel4 panel4;

	public ResultPanel(Step4Module step4Module) {
		super();
		this.GC = step4Module.getGC();
		this.setName(step4Module.getName());

		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane(); // 创建选项卡面板对象
		// 创建面板
		panel1 = new Panel1();
		panel2 = new Panel2();
		panel3 = new JPanel();
		panel4 = new Panel4();
		// panel1.setBackground(Color.yellow);
		// panel2.setBackground(Color.blue);
		// panel3.setBackground(Color.green);
		// panel4.setBackground(Color.red);
		// 将标签面板加入到选项卡面板对象上
		tabbedPane.addTab("表1-担保圈总体识别结果", null, panel1, "First panel");
		tabbedPane.addTab("表2-“区域分布”识别结果 ", null, panel2, "Second panel");
		tabbedPane.addTab("表3-“重点客户”识别结果", null, panel3, "Third panel");
		tabbedPane.addTab("表4-“风险分类”识别结果", null, panel4, "Fourth panel");

		this.add(tabbedPane, BorderLayout.CENTER);
		this.setBackground(Color.white);
	}

	public void refreshResult(List<Graphic> circles) {
		if (circles != null) {
			panel1.refreshResult(circles);
			panel2.refreshResult(circles);
			panel4.refreshResult(circles);
		}
	}

}
