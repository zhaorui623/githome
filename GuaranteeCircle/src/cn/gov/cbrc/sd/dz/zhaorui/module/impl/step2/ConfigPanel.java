package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;

@SuppressWarnings("serial")
public class ConfigPanel extends JPanel {

	private GC GC = null;

	private Step2Module step2Module = null;

	private String instructionText = "程序会自动分析所导入企业的担保关系，并在内存中绘制一整张担保关系拓扑图。一般情况下，该图会包含若干张彼此独立的连通子图，其中规模较小的连通子图会有几十个，但往往也存在一个规模超大（内部节点数为几百甚至几千）的连通子图。对于这种超大连通子图，需要用一定的算法将其合理拆分为规模适中的小图，本页面即用来配置拆分算法。配置完成后，点击“应用”按钮即完成参数配置。";

	private JPanel algmConfigPanel;

	private List<JRadioButton> algorithmChooseRadioButtons;

	private List<HugeCircleSplitAlgorithm> algms;

	public ConfigPanel(Step2Module step2Module) throws Exception {

		this.step2Module = step2Module;
		this.GC = step2Module.getGC();
		this.setName(step2Module.getName());
		this.setLayout(new BorderLayout(5, 5));
		addComponents();

		addListeners();
	}

	private void addComponents() throws Exception {
		// 说明面板
		JTextArea instruction = new JTextArea();
		instruction.setEditable(false);
		instruction.setLineWrap(true);
		instruction.setText(instructionText);
		this.add(instruction, BorderLayout.NORTH);

		// 超大圈算法选择和配置面板
		JPanel p1 = new JPanel(new BorderLayout(5, 5));
		p1.setBorder(BorderFactory.createTitledBorder("超大圈拆分算法配置"));
		JPanel algorithmChoosePanel = new JPanel();// 超大圈算法选择面板
		algorithmChoosePanel.add(new JLabel("超大圈拆分算法:"));
		List<JRadioButton> algorithmChooseRadioButtons = createAlgorithmChooseRadioButtons();
		for (JRadioButton button : algorithmChooseRadioButtons)
			algorithmChoosePanel.add(button);
		p1.add(algorithmChoosePanel, BorderLayout.NORTH);
		algmConfigPanel = new JPanel();// 超大圈算法配置面板
		p1.add(algmConfigPanel, BorderLayout.CENTER);

		this.add(p1, BorderLayout.CENTER);

		// 担保圈分类标准配置面板

	}

	private List<JRadioButton> createAlgorithmChooseRadioButtons() throws Exception {
		ButtonGroup algorithmChooseRadioButtonGroup = new ButtonGroup();
		algorithmChooseRadioButtons = new ArrayList<JRadioButton>();
		algms = Config.getHugeCircleSplitAlgorithms();
		for (HugeCircleSplitAlgorithm algm : algms) {
			JRadioButton button = new JRadioButton(algm.getAlgorithm_name());
			button.setEnabled(algm.isAlgorithm_enable());
			button.setSelected(algm.isAlgorithm_selected());
			algorithmChooseRadioButtonGroup.add(button);
			algorithmChooseRadioButtons.add(button);
		}

		return algorithmChooseRadioButtons;
	}

	private void addListeners() {
		for (int i = 0; i < algorithmChooseRadioButtons.size(); i++) {
			JRadioButton button = algorithmChooseRadioButtons.get(i);
			step2Module.setHcsAlgm(algms.get(i));
			button.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						JPanel p = step2Module.getHcsAlgm().getAlgorithmConfigPanel();
						algmConfigPanel.removeAll();
						algmConfigPanel.add(p);
					}
				}
			});
			if (i == 0){
				button.doClick();
				algmConfigPanel.removeAll();
				algmConfigPanel.add(step2Module.getHcsAlgm().getAlgorithmConfigPanel());
			}
		}
	}

}
