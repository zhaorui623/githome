package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.exception.ErrorDialog;
import cn.gov.cbrc.sd.dz.zhaorui.exception.TimerErrorDialog;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.InvisibleNode;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.InvisibleTreeModel;

@SuppressWarnings("serial")
public class DataSourcePanel extends JSplitPane {

	private GC GC = null;
	

	private static InfoPane infopane;

	public DataSourcePanel(Step1Module step1Module) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		this.GC = step1Module.getGC();
		this.setName(step1Module.getName());
	}
	

}
