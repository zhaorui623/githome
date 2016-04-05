package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Document;

import javax.swing.SpinnerNumberModel;

import cn.gov.cbrc.sd.dz.zhaorui.component.NumberSpinner;
import cn.gov.cbrc.sd.dz.zhaorui.model.GCClassify;
import cn.gov.cbrc.sd.dz.zhaorui.model.RegionLevel;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.XMLToolkit;

public class RegionDistributAnalysisConfigPanel extends JPanel {

	private JComboBox regionLevelChooser;

	public RegionDistributAnalysisConfigPanel() throws Exception {
		super(new FlowLayout(FlowLayout.LEFT));
		
		this.add(new JLabel("进行“区域分布”分析时细化到："));
		
		RegionLevel regionLevels[]=RegionLevel.values();		
		regionLevelChooser=new JComboBox(regionLevels);

		RegionLevel level=Config.getRegionLevel();
		regionLevelChooser.setSelectedItem(level);
		
		this.add(regionLevelChooser);

		addListeners();
	}

	private void addListeners() {
		
	}

	public boolean updateConfigCache() throws Exception {
		Document doc=Config.getDoc();
		XMLToolkit.getElementById(doc, "33").setAttribute("value",String.valueOf(regionLevelChooser.getSelectedItem()));
		return true;
	}
}
