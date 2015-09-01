package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLToolkit {

	/**
	 * 将文件转换为Docmuent对象
	 * 
	 * @param file
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document parseFile(File file)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		doc = db.parse(file);
		return doc;
	}

	/**
	 * 将Document对象输出为文�?
	 * 
	 * @param doc
	 * @param file
	 * @return
	 * @throws TransformerException
	 */
	public static File outputFile(Document doc, File file)
			throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
	
		Transformer transformer = null;
		transformer = tFactory.newTransformer();
	
		DOMSource source = new DOMSource(doc);
	
		StreamResult result = new StreamResult(file);
	
		transformer.transform(source, result);
	
		return file;
	}

	/**
	 * 根据id属�?获取root节点及其子节点中符合条件的节�?
	 * 
	 * @param root
	 * @param id
	 * @return 未找到的话返回null
	 */
	public static Element getElementById(Element root, String id) {
		if (root == null || id == null)
			return null;
		else if (id.equals(root.getAttribute("id"))
				|| id.equals(root.getAttribute("ID"))
				|| id.equals(root.getAttribute("Id"))
				|| id.equals(root.getAttribute("iD")))
			return root;
		else {
			Element element = null;
			NodeList childs = root.getChildNodes();
			int length = childs.getLength();
			for (int i = 0; i < length; i++) {
				Node child = childs.item(i);
				if (child instanceof Element) {
					element = getElementById((Element) child, id);
					if (element != null)
						return element;
				}
			}
		}
		return null;
	}

	/**
	 * 根据TagName在root的一级子节点中查找符合条件的�?��节点
	 * 
	 * @param root
	 * @param tag
	 * @return
	 */
	public static List<Element> getChildsByTagName(Element root, String tag) {
		ArrayList<Element> list = new ArrayList<Element>();
		if (root == null || tag == null)
			return list;
		else {
			NodeList childs = root.getChildNodes();
			int length = childs.getLength();
			for (int i = 0; i < length; i++) {
				Node child = childs.item(i);
				if (child instanceof Element && tag.equals(child.getNodeName()))
					list.add((Element) child);
			}
		}
		return list;
	}

	public static void removeAllChilds(Element root) {
		NodeList list = root.getChildNodes();
		List<Node> childs = new ArrayList<Node>();
		for (int i = 0; i < list.getLength(); i++)
			childs.add(list.item(i));
		for (Node child : childs) {
			root.removeChild(child);
		}
	}
}
