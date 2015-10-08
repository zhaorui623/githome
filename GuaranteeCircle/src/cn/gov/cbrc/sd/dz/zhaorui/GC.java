package cn.gov.cbrc.sd.dz.zhaorui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DragSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import cn.gov.cbrc.sd.dz.zhaorui.component.GalileoMenuBar;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.component.NavigatorBar;
import cn.gov.cbrc.sd.dz.zhaorui.component.WorkPane;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GCLogger;

/**
 * The main class of the editor
 * 
 * @author zr
 */
public class GC {

	/**
	 * the parent container
	 */
	private static JFrame parentContainer = null;
	int WIDTH = 1024, HEIGHT = 700;

	/**
	 * the desktop component
	 */
	private JPanel desktop;

	/**
	 * 左侧导航
	 */
	private NavigatorBar navigator;

	/**
	 * 菜单
	 */
	private GalileoMenuBar menubar;

	/**
	 * 右部主工作区
	 */
	private WorkPane workPane;

	/**
	 * the class that gives the resources
	 */
	private ResourceManager resource;

	/**
	 * the drag source
	 */
	private DragSource dragSource = DragSource.getDefaultDragSource();

	/**
	 * whether the JVM will be exited when the user requires to exit from the
	 * editor
	 */
	private boolean canExitFromJVM = false;

	/**
	 * the font
	 */
	public static final Font theFont = new Font("theFont", Font.ROMAN_BASELINE, 20);

	/**
	 * the small font
	 */
	public static final Font smallFont = new Font("smallFont", Font.ROMAN_BASELINE, 12);

	/**
	 * the set of the runnables that should be run when the editor is exiting
	 */
	private HashSet<Runnable> disposeRunnables = new HashSet<Runnable>();

	/**
	 * the decimal format and the format used when displaying numbers
	 */
	private static DecimalFormat format, displayFormat;

	/**
	 * the svg editor
	 */
	private static GC gc = null;

	public static ImageIcon icon;

	private static File outputDir;

	/**
	 * creating the bundle
	 */
	static {

		// sets the format object that will be used to convert a double value
		// into a string
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("############.################", symbols);
		displayFormat = new DecimalFormat("##########.#", symbols);
	}

	private static Logger logger = GCLogger.getInstance(GC.class);

	/**
	 * The constructor of the class
	 * 
	 * @param args
	 */
	public GC(String[] args) {// 设置界面风格为window经典风格
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		gc = this;
	}

	/**
	 * initializing the editor
	 * 
	 * @param parent
	 *            the parent container for the application
	 * @param showSplash
	 *            whether the splash screen should be shown or not
	 * @param displayFrame
	 *            whether or not to show the frame
	 * @param exitFromJVM
	 *            whether the JVM will be exited when the user requires to exit
	 *            from the editor
	 * @param disposeRunnable
	 *            the runnable that should be run when exiting the editor
	 * @throws JFlashInvalidFlashException
	 * @throws JFlashLibraryLoadFailedException
	 * @throws FileNotFoundException
	 */
	public void init(JFrame parent, boolean showLoginDialog, boolean showSplash, final boolean displayFrame,
			boolean exitFromJVM, Runnable disposeRunnable) {

		parentContainer = parent;
		this.canExitFromJVM = exitFromJVM;

		if (disposeRunnable != null)
			this.disposeRunnables.add(disposeRunnable);

		// setting the values for the tooltip manager
		ToolTipManager.sharedInstance().setInitialDelay(100);
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		ToolTipManager.sharedInstance().setReshowDelay(100);

		// setting the frame icon
		icon = ResourceManager.getIcon("galileo.png", false);

		// the window containing the splash screen
		JWindow window = null;

		// 设置界面风格为window经典风格
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		// creating the resource manager
		resource = new ResourceManager(this);

		if (desktop == null) {
			// the desktop is not multi-windowed
			desktop = new JPanel();
			desktop.setBackground(Color.white);
			desktop.setLayout(new BorderLayout());
			// desktop.setBackground(new Color(51, 96, 145));
			// desktop.setBackground(new Color(128,128,128));
		}

		// 菜单
		menubar = new GalileoMenuBar();
		parent.setJMenuBar(menubar);

		// 左侧导航
		navigator = new NavigatorBar();
		desktop.add(navigator, "West");

		// 右部主工作区
		workPane = new WorkPane();
		desktop.add(workPane, "Center");

		// 下部日志区
		desktop.add(InfoPane.getInstance(), "South");

		Module.createModules(this);

		if (icon != null && icon.getImage() != null)
			parentContainer.setIconImage(icon.getImage());

		// handling the frame content
		parentContainer.getContentPane().setLayout(new BorderLayout());
		parentContainer.getContentPane().add(desktop, BorderLayout.CENTER);

		// computing the bounds of the main frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(parentContainer.getGraphicsConfiguration());
		final Rectangle frameBounds = new Rectangle((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2,
				WIDTH, HEIGHT);

		// sets the bounds of the main frame
		parentContainer.setBounds(frameBounds);
		desktop.setBounds(frameBounds);

		// displays the main frame
		if (displayFrame) {
			parentContainer.setVisible(true);
		}

	}

	/**
	 * @return the main frame
	 */
	public static Container getParent() {
		return parentContainer;
	}

	/**
	 * @return the navigator bar
	 */
	public NavigatorBar getNavigatorBar() {
		return navigator;
	}

	/**
	 * @return the work pane
	 */
	public WorkPane getWorkPane() {
		return workPane;
	}

	/**
	 * @return Returns the dragSource.
	 */
	public DragSource getDragSource() {
		return dragSource;
	}

	/**
	 * @return the component into which all the panels containing the
	 *         JScrollPanes will be placed
	 */
	public JComponent getDesktop() {
		return desktop;
	}

	/**
	 * @return an object of the class managing the resources
	 */
	public ResourceManager getResource() {
		return resource;
	}

	/**
	 * @return Returns the format.
	 */
	public static DecimalFormat getFormat() {
		return format;
	}

	/**
	 * @return the format used when numbers have to be displayed
	 */
	public static DecimalFormat getDisplayFormat() {
		return displayFormat;
	}

	/**
	 * exits the editor
	 */
	public void exit() {

		// saving the current state of the editor
		gc.getResource().saveEditorsCurrentState();

		boolean canExitEditor = canExitFromJVM;

		if (canExitEditor) {

			// running the dispose runnables
			for (Runnable runnable : disposeRunnables) {

				runnable.run();
			}

			System.exit(0);
		}

	}

	/**
	 * @return the galileo instance
	 */
	public static GC getGalileo() {

		return gc;
	}

	private String currentModuleId;

	public void setCurrentModule(String id) {
		currentModuleId = id;
	}

	public String getCurrentModule() {
		return currentModuleId;
	}

	public Module getModule(String moduleName) {
		return Module.getModule(moduleName);
	}

	public GalileoMenuBar getMenuBar() {
		return menubar;
	}

	public static void setOutputDir(File dir) {
		if (dir != null) {
			if (dir.exists() == false)
				dir.mkdir();
			outputDir = dir;
		}
	}

	public static File getOutputDir() {
		return outputDir;
	}
}
