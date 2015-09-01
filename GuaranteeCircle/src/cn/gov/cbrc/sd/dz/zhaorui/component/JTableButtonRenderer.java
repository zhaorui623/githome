package cn.gov.cbrc.sd.dz.zhaorui.component;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class JTableButtonRenderer implements TableCellRenderer { 
  private TableCellRenderer __defaultRenderer; 

  public JTableButtonRenderer(TableCellRenderer renderer) { 
    __defaultRenderer = renderer; 
  } 

  public Component getTableCellRendererComponent(JTable table, Object value, 
boolean isSelected, 
boolean hasFocus, 
int row, int column) 
  { 
    if(value instanceof Component) 
      return (Component)value; 
    return __defaultRenderer.getTableCellRendererComponent( 
  table, value, isSelected, hasFocus, row, column); 
  } 
} 

/** 
* 
* @author Daniel F. Savarese 
*/ 
class JTableButtonMouseListener implements MouseListener { 
  private JTable __table; 

  private void __forwardEventToButton(MouseEvent e) { 
    TableColumnModel columnModel = __table.getColumnModel(); 
    int column = columnModel.getColumnIndexAtX(e.getX()); 
    int row    = e.getY() / __table.getRowHeight(); 
    Object value; 
    JButton button; 
    MouseEvent buttonEvent; 

    if(row >= __table.getRowCount() || row < 0 || 
      column >= __table.getColumnCount() || column < 0) 
      return; 

    value = __table.getValueAt(row, column); 

    if(!(value instanceof JButton)) 
      return; 

    button = (JButton)value; 

    buttonEvent = 
      (MouseEvent)SwingUtilities.convertMouseEvent(__table, e, button); 
    button.dispatchEvent(buttonEvent); 
    // This is necessary so that when a button is pressed and released 
    // it gets rendered properly.  Otherwise, the button may still appear 
    // pressed down when it has been released. 
    __table.repaint(); 
  } 

  public JTableButtonMouseListener(JTable table) { 
    __table = table; 
  } 

  public void mouseClicked(MouseEvent e) { 
    __forwardEventToButton(e); 
  } 

  public void mouseEntered(MouseEvent e) { 
    __forwardEventToButton(e); 
  } 

  public void mouseExited(MouseEvent e) { 
    __forwardEventToButton(e); 
  } 

  public void mousePressed(MouseEvent e) { 
    __forwardEventToButton(e); 
  } 

  public void mouseReleased(MouseEvent e) { 
    __forwardEventToButton(e); 
  } 
} 