package cn.gov.cbrc.sd.dz.zhaorui.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.model.RegionLevel;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.XMLToolkit;

public class Config {

	public static final File CONFIG_FILE = new File(System.getProperty("user.dir") + "\\" + "config.xml");

	public static final String SPLIT_ALGORITHM_TAG = "algorithm";
	public static final String REGION_TAG = "region";
	public static final String REGION_LEVEL_TAG = "region-level";
	public static final String LOAN_BALANCE_FLOOR_ATTRIBUTE = "loan-balance-floor";
	public static final String LOAN_BALANCE_CEILLING_ATTRIBUTE = "loan-balance-ceiling";
	public static final String LEVEL_ATTRIBUTE = "level";

	private static Document doc;

	private static List<HugeCircleSplitAlgorithm> hugeCircleSplitAlgorithmList;

	public static Document getDoc() throws Exception {
		if (doc == null) {
			DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuild = docBuildFactory.newDocumentBuilder();
			doc = docBuild.parse(CONFIG_FILE);
		}
		return doc;
	}

	public static boolean saveDoc() throws Exception {
		TransformerFactory tfac = TransformerFactory.newInstance();
		Transformer tra = tfac.newTransformer();
		DOMSource doms = new DOMSource(doc);
		FileOutputStream outstream = new FileOutputStream(CONFIG_FILE);
		StreamResult sr = new StreamResult(outstream);
		tra.transform(doms, sr);
		return true;
	}

	/**
	 * 根据配置文件，生成“超大圈拆分算法对象”们
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<HugeCircleSplitAlgorithm> getHugeCircleSplitAlgorithms() throws Exception {
		if (hugeCircleSplitAlgorithmList == null) {
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
					algorithmClassStr = "cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl.DefaultAlgorithm";
				hcsAlgm = (HugeCircleSplitAlgorithm) Class.forName(algorithmClassStr).getConstructor(classes)
						.newInstance(args);
				if ("".equals(configPanelClassStr))
					configPanelClassStr = "javax.swing.JPanel";
				JPanel algorithmConfigPanel = (JPanel) Class.forName(configPanelClassStr).newInstance();
				hcsAlgm.setAlgorithmConfigPanel(algorithmConfigPanel);
				hcsAlgm.setHuge_circle_vertex_floor(
						Integer.parseInt(XMLToolkit.getElementById(doc, "2").getAttribute("value")));
				hugeCircleSplitAlgorithmList.add(hcsAlgm);
			}
		}
		return hugeCircleSplitAlgorithmList;
	}

	private static Map<String, Region> regionsMap;

	public static Map<String, Region> getRegionsMap() throws Exception {
		if (regionsMap == null) {
			regionsMap = new HashMap<String, Region>();
			Document doc = Config.getDoc();
			NodeList regionElements = doc.getElementsByTagName(Config.REGION_TAG);
			for (int i = 0; i < regionElements.getLength(); i++) {
				Element regionElement = (Element) regionElements.item(i);
				String name = regionElement.getAttribute("name");
				String codeStr = regionElement.getAttribute("code");
				String[] codes = codeStr.split(";");
				Region region = new Region(name, codes);
				for (String code : codes)
					regionsMap.put(code, region);
			}
		}
		return regionsMap;
	}

	public static Region getRegionByCode(String code) throws Exception {
		Region region;
		if (code == null || "null".equals(code))
			return new Region("未知地区");
		region = getRegionsMap().get(code);
		if (region == null)
			region = new Region("省外地区", code);
		return region;
	}

	public static RegionLevel getRegionLevel() throws Exception {

		Document doc = Config.getDoc();
		Element regionLevelElement =(Element) doc.getElementsByTagName(Config.REGION_LEVEL_TAG).item(0);
		String regionLevel=regionLevelElement.getAttribute("value");
		return RegionLevel.valueOf(regionLevel);
		
	}
}
