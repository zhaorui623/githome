package cn.gov.cbrc.sd.dz.zhaorui.resource;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;

public class Config {

	public static final String CONFIG_FILE_NAME = "config.xml";

	public static final String SPLIT_ALGORITHM_TAG = "algorithm";

	private static Document doc;
	private static List<HugeCircleSplitAlgorithm> hugeCircleSplitAlgorithmList;

	public static Document getDoc() {
		if (doc == null)
			doc = ResourceManager.getXMLDocument(Config.CONFIG_FILE_NAME);
		return doc;
	}
	/**
	 * 根据配置文件，生成“超大圈拆分算法对象”们
	 * @return
	 * @throws Exception
	 */
	public static List<HugeCircleSplitAlgorithm> getHugeCircleSplitAlgorithms() throws Exception {
		if (hugeCircleSplitAlgorithmList == null){
			hugeCircleSplitAlgorithmList = new ArrayList<HugeCircleSplitAlgorithm>();
			NodeList algorithmElements = Config.getDoc().getElementsByTagName(Config.SPLIT_ALGORITHM_TAG);
			for (int i = 0; i < algorithmElements.getLength(); i++) {
				Element algorithmElement = (Element) algorithmElements.item(i);
				String algorithmName = algorithmElement.getAttribute("name");
				boolean enable = Boolean.parseBoolean(algorithmElement.getAttribute("enable"));
				boolean selected = Boolean.parseBoolean(algorithmElement.getAttribute("selected"));
				String algorithmClassStr = algorithmElement.getAttribute("class");
				String configPanelClassStr = algorithmElement.getAttribute("component");
				Class classes[] = { String.class, Boolean.class, Boolean.class };
				Object args[] = { algorithmName, enable, selected };
				HugeCircleSplitAlgorithm hcsAlgm;
				if ("".equals(algorithmClassStr))
					algorithmClassStr = "cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm";
				hcsAlgm = (HugeCircleSplitAlgorithm) Class.forName(algorithmClassStr).getConstructor(classes).newInstance(args);
				if("".equals(configPanelClassStr))
					configPanelClassStr="javax.swing.JPanel";
				JPanel algorithmConfigPanel= (JPanel)Class.forName(configPanelClassStr).newInstance();
				hcsAlgm.setAlgorithmConfigPanel(algorithmConfigPanel);
				hugeCircleSplitAlgorithmList.add(hcsAlgm);
			}
		}
		return hugeCircleSplitAlgorithmList;
	}
}
