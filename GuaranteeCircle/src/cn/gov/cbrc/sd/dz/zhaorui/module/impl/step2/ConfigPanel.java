package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl.PickAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.GCClassify;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1.DataSourcePanel;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3.Step3Module;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;

@SuppressWarnings("serial")
public class ConfigPanel extends JPanel {

	private GC GC = null;

	private Step2Module step2Module = null;

	private String instructionText = "程序会自动分析所导入企业的担保关系，并在内存中绘制一整张担保关系拓扑图。一般情况下，该图会包含若干张彼此独立的连通子图，其中规模较小的连通子图会有几十个，但往往也存在一个规模超大（内部节点数为几百甚至几千）的连通子图。对于这种超大连通子图，需要用一定的算法将其合理拆分为规模适中的小图，本页面即用来配置拆分算法。配置完成后，点击“应用”按钮即完成参数配置。";

	private JPanel algmConfigPanel;
	
	private GCClassifyConfigPanel gcClassifyConfigPanel;

	private RegionDistributAnalysisConfigPanel rdaConfigPanel;
	
	private JButton apply;

	private List<JRadioButton> algorithmChooseRadioButtons;

	private List<HugeCircleSplitAlgorithm> algms;
	private String successMessage= "应用并保存配置成功！";

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

		JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
		// 超大圈算法选择和配置面板
		JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(BorderFactory.createTitledBorder("超大圈拆分算法配置"));
		JPanel algorithmChoosePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));// 超大圈算法选择面板
		algorithmChoosePanel.add(new JLabel("超大圈拆分算法:"));
		List<JRadioButton> algorithmChooseRadioButtons = createAlgorithmChooseRadioButtons();
		for (JRadioButton button : algorithmChooseRadioButtons){
			algorithmChoosePanel.add(button);
		}
		p1.add(algorithmChoosePanel, BorderLayout.NORTH);
		algmConfigPanel = new JPanel();
		p1.add(algmConfigPanel, BorderLayout.CENTER);// 超大圈算法配置面板
		centerPanel.add(p1, BorderLayout.NORTH);

		// 担保圈分类标准配置面板
		JPanel p2 = new JPanel();
		p2.setBorder(BorderFactory.createTitledBorder("担保圈分类标准配置"));
		GCClassify gcClassify=new GCClassify();
		gcClassifyConfigPanel=new GCClassifyConfigPanel(gcClassify);
		p2.add(gcClassifyConfigPanel);
		centerPanel.add(p2, BorderLayout.CENTER);

		// “区域分布”分析模块配置面板
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p3.setBorder(BorderFactory.createTitledBorder("“区域分布”分析模块配置"));
		rdaConfigPanel=new RegionDistributAnalysisConfigPanel();
		p3.add(rdaConfigPanel);
		centerPanel.add(p3, BorderLayout.SOUTH);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		// 应用按钮
		apply = new JButton("应用");
		this.add(apply, BorderLayout.SOUTH);
	}
	Map<JRadioButton,HugeCircleSplitAlgorithm> map;
	private List<JRadioButton> createAlgorithmChooseRadioButtons() throws Exception {
		map=new HashMap<JRadioButton, HugeCircleSplitAlgorithm>();
		ButtonGroup algorithmChooseRadioButtonGroup = new ButtonGroup();
		algorithmChooseRadioButtons = new ArrayList<JRadioButton>();
		algms = Config.getHugeCircleSplitAlgorithms();
		for (HugeCircleSplitAlgorithm algm : algms) {
			JRadioButton button = new JRadioButton(algm.getAlgorithm_name());
			button.setEnabled(algm.isAlgorithm_enable());
			button.setSelected(algm.isAlgorithm_selected());
			algorithmChooseRadioButtonGroup.add(button);
			algorithmChooseRadioButtons.add(button);
			map.put(button,algm);
		}

		return algorithmChooseRadioButtons;
	}

	private void addListeners() {
		for (int i = 0; i < algorithmChooseRadioButtons.size(); i++) {
			JRadioButton button = algorithmChooseRadioButtons.get(i);
			button.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {						
						step2Module.setHcsAlgm(map.get(e.getSource()));
						JPanel p = step2Module.getHcsAlgm().getAlgorithmConfigPanel();
						algmConfigPanel.removeAll();
						algmConfigPanel.add(p);
					}
				}
			});
			if (i == 0) {
				button.doClick();
				algmConfigPanel.removeAll();			
				step2Module.setHcsAlgm(map.get(button));
				algmConfigPanel.add(step2Module.getHcsAlgm().getAlgorithmConfigPanel());
			}
		}
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < algorithmChooseRadioButtons.size(); i++) {
					JRadioButton button = algorithmChooseRadioButtons.get(i);
					if (button.isSelected()) {
						JPanel algmConfigPanel = algms.get(i).getAlgorithmConfigPanel();
						if (algmConfigPanel instanceof PickAlgorithmConfigPanel) {
							PickAlgorithmConfigPanel pacPanel = (PickAlgorithmConfigPanel) algmConfigPanel;
							PickAlgorithm palgm = pacPanel.getAlgorithm();

							palgm.setAlgorithm_selected(true);
							
							palgm.setCondition_number_value(pacPanel.getCondition_number_value());

							palgm.setGuaranteed_loan_balance_floor_selected(
									pacPanel.isGuaranteed_loan_balance_floor_selected());
							palgm.setGuaranteed_loan_balance_floor_unit(pacPanel.getGuaranteed_loan_balance_floor_unit());
							palgm.setGuaranteed_loan_balance_floor_value(
									pacPanel.getGuaranteed_loan_balance_floor_value());

							palgm.setGuarantor_floor_selected(pacPanel.isGuarantorFloorSelected());
							palgm.setGuarantor_floor_value(pacPanel.getGuarantorFloor());

							palgm.setLoan_balance_floor_selected(pacPanel.isLoanBalanceFloorSelected());
							palgm.setLoan_balance_floor_unit(pacPanel.getLoanBalanceFloorUnit());
							palgm.setLoan_balance_floor_value(pacPanel.getLoanBalanceFloor());

							palgm.setMutually_guaranteed_floor_selected(pacPanel.isMutuallyGuarantorFloorSelected());
							palgm.setMutually_guaranteed_floor_value(pacPanel.getMutuallyGuarantorFloor());

							palgm.setOne_hand_vertex_all(pacPanel.isOne_hand_vertex_all_selected());
							palgm.setOne_hand_vertex_in_only(pacPanel.isOne_hand_vertex_in_only_selected());
							palgm.setOne_hand_vertex_none(pacPanel.isOne_hand_vertex_none_selected());

							palgm.setTwo_hand_vertex_all(pacPanel.isTwo_hand_vertex_all_selected());
							palgm.setTwo_hand_vertex_in_only(pacPanel.isTwo_hand_vertex_in_only_selected());
							palgm.setTwo_hand_vertex_none(pacPanel.isTwo_hand_vertex_none_selected());

							palgm.setThree_hand_vertex_all(pacPanel.isThree_hand_vertex_all_selected());
							palgm.setThree_hand_vertex_in_only(pacPanel.isThree_hand_vertex_in_only_selected());
							palgm.setThree_hand_vertex_none(pacPanel.isThree_hand_vertex_none_selected());

							palgm.setPick_corecorp_loop(pacPanel.isPickCorecorpLoopSelected());
							palgm.setPick_mutually_guaranteed_corp(pacPanel.isPickMutuallyGuaranteedCorpSelected());
							palgm.setUnpick_corecorp_son(pacPanel.isUnpickCorecorpSonSelected());
							
							GCClassify gcClassify=gcClassifyConfigPanel.getGcClassify();
							gcClassify.setLoan_balance_floor(gcClassifyConfigPanel.getLoan_balance_floor());
							gcClassify.setLoan_balance_ceiling(gcClassifyConfigPanel.getLoan_balance_ceiling());

							try {
								palgm.updateConfigCache();
								gcClassify.updateConfigCache();
								rdaConfigPanel.updateConfigCache();
								boolean sucess=Config.saveDoc();
								
								if(sucess){
									JOptionPane.showMessageDialog(ConfigPanel.this,successMessage, "提示",
											JOptionPane.INFORMATION_MESSAGE);
									InfoPane.getInstance().info( successMessage);
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(ConfigPanel.this, "应用配置成功，但保存配置时出错，不影响下一步继续进行。", "警告",
										JOptionPane.WARNING_MESSAGE);
							}finally {
								Module.gotoStep(3);
							}
						}
					}
				}
			}
		});
	}

}
