package cn.gov.cbrc.sd.dz.zhaorui.resource;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.prefs.Preferences;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;

import cn.gov.cbrc.sd.dz.zhaorui.GC;

/**
 * @author zr
 * 
 *         The class managing the resources
 */
public class ResourceManager {

	/**
	 * the current directory for the system
	 */
	private File currentDirectory = null;

	/**
	 * the hashtable associating a an icon's name to an icon
	 */
	private static final Hashtable<String, ImageIcon> icons = new Hashtable<String, ImageIcon>();

	/**
	 * the hashtable associating a an icon's name to a gray icon
	 */
	private static final Hashtable<String, ImageIcon> grayIcons = new Hashtable<String, ImageIcon>();

	/**
	 * the map associating the name of a xml document to this document
	 */
	private final static Hashtable<String, Document> cachedXMLDocuments = new Hashtable<String, Document>();

	/**
	 * the map associating the name of a xls document to this document
	 */
	private final static Hashtable<String, HSSFWorkbook> cachedXLSWorkbooks = new Hashtable<String, HSSFWorkbook>();

	/**
	 * a user preference node
	 */
	private Preferences preferences = null;

	/**
	 * the list of the runnables that will be run when the program is exited
	 */
	private final LinkedList<Runnable> exitRunnables = new LinkedList<Runnable>();

	/**
	 * the galileo
	 */
	private GC galileo = null;

	/**
	 * the constructor of the class
	 * 
	 * @param galileo
	 */
	public ResourceManager(GC galileo) {

		this.galileo = galileo;

		// getting the preference node
		preferences = Preferences.userNodeForPackage(galileo.getClass());

		if (preferences != null) {

			String[] keys = null;

			try {
				keys = preferences.keys();
			} catch (Exception ex) {
			}

			if (keys != null) {

				// filling the list of the recent files
				String val = "";

				for (int i = 0; i < keys.length; i++) {

					val = preferences.get(keys[i], null);

					if (val != null && !val.equals("")) {

						// recentFiles.add(val);
					}
				}
			}
		}
	}

	/**
	 * @return the editor
	 */
	public GC getGalileoMain() {
		return galileo;
	}

	/**
	 * computes the path of a resource given its name
	 * 
	 * @param resource
	 *            the name of a resource
	 * @return the full path of a resource
	 */
	public static String getPath(String resource) {

		String path = "";

		try {
			path = ResourceManager.class.getResource(resource).toExternalForm();
		} catch (Exception ex) {
			path = "";
		}

		return path;
	}

	/**
	 * gives an ImageIcon object given the name of it as it is witten in the
	 * GalileoMainIcons.properties file
	 * 
	 * @param name
	 *            the name of an icon
	 * @param isGrayIcon
	 *            true if the icon should be used for a disabled widget
	 * @return an image icon
	 */
	public static ImageIcon getIcon(String name, boolean isGrayIcon) {
		ImageIcon icon = null;
		if (name != null && !name.equals("")) {
			if (icons.containsKey(name)) {
				if (isGrayIcon) {
					icon = grayIcons.get(name);
				} else {
					icon = icons.get(name);
				}
			} else {
				if (name != null && !name.equals("")) {
					try {
						icon = new ImageIcon(new URL(getPath("icons/" + name)));
					} catch (Exception ex) {
						icon = null;
					}
					if (icon != null) {
						icons.put(name, icon);
						Image image = icon.getImage();
						ImageIcon grayIcon = new ImageIcon(GrayFilter
								.createDisabledImage(image));
						grayIcons.put(name, grayIcon);
						if (isGrayIcon) {
							icon = grayIcon;
						}
					}
				}
			}
		}
		return icon;
	}

	/**
	 * @return the current directory
	 */
	public File getCurrentDirectory() {
		return currentDirectory;
	}

	/**
	 * sets he current directory
	 * 
	 * @param directory
	 *            a file representing a directory
	 */
	public void setCurrentDirectory(File directory) {
		currentDirectory = directory;
	}

	/**
	 * create a document from tthe given file in the resource files
	 * 
	 * @param name
	 *            the name of the xml file
	 * @return the document
	 */
	public static Document getXMLDocument(String name) {

		Document doc = null;

		if (name != null && !name.equals("")) {

			if (cachedXMLDocuments.containsKey(name)) {

				doc = cachedXMLDocuments.get(name);

			} else {

				DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory
						.newInstance();

				String path = "";

				try {
					// parses the XML file
					DocumentBuilder docBuild = docBuildFactory
							.newDocumentBuilder();
					path = getPath("xml/".concat(name));
					doc = docBuild.parse(path);
				} catch (Exception ex) {
					doc = null;
				}

				if (doc != null) {

					cachedXMLDocuments.put(name, doc);
				}
			}
		}

		return doc;
	}

	/**
	 * create a document from tthe given file in the resource files
	 * 
	 * @param name
	 *            the name of the xls file
	 * @return the document
	 */
	public static HSSFWorkbook getXLSWorkbook(String name) {

		HSSFWorkbook workbook = null;

		if (name != null && !name.equals("")) {

			if (cachedXLSWorkbooks.containsKey(name)) {

				workbook = cachedXLSWorkbooks.get(name);

			} else {
				String path = "";
				try {
					// parses the XLS file
					path = getPath2("xls/".concat(name));
					workbook = new HSSFWorkbook(new FileInputStream(path));
				} catch (Exception ex) {
					ex.printStackTrace();
					workbook = null;
				}

				if (workbook != null) {

					cachedXLSWorkbooks.put(name, workbook);
				}
			}
		}

		return workbook;
	}

	public static HSSFWorkbook getXLSWorkbook(File file) {
		HSSFWorkbook workbook = null;
		if (file != null && file.exists()) {
			try {
				// parses the XLS file
				workbook = new HSSFWorkbook(new FileInputStream(file));
			} catch (Exception ex) {
				ex.printStackTrace();
				workbook = null;
			}
		}
		return workbook;
	}

	public static String getPath2(String resource) {
		String path = ResourceManager.class.getCanonicalName().replace(
				"ResourceManager", "").replaceAll("\\.", "/");
		path = System.getProperty("user.dir").concat("/bin/" + path).concat(
				resource);
		return path;
	}

	/**
	 * adds a runnable to the list of the runnables that will be run when the
	 * editor is exited
	 * 
	 * @param runnable
	 *            a runnable
	 */
	public void addExitRunnable(Runnable runnable) {

		synchronized (this) {
			exitRunnables.add(runnable);
		}
	}

	/**
	 * saves the editor's current state before it is exited
	 */
	public void saveEditorsCurrentState() {

		// runs the list of the runnables
		LinkedList<Runnable> exitRun = new LinkedList<Runnable>(exitRunnables);

		for (Runnable runnable : exitRun) {

			if (runnable != null) {

				runnable.run();
			}
		}
	}
}
