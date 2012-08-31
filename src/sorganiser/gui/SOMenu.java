package sorganiser.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Menu bar for Series Organiser
 * @author Peter Pretorius
 *
 */
public class SOMenu extends JMenuBar {
	
	private JMenu fileMenu, optionsMenu, helpMenu;
	
	private JMenuItem exitMenuItem, aboutMenuItem;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SOMenu()
	{
		//establish file menu structure
		fileMenu = new JMenu("File");
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		
		//establish options menu structure
		optionsMenu = new JMenu("Options");
		
		//establish help menu structure
		helpMenu = new JMenu("Help");
		aboutMenuItem = new JMenuItem("About");
		helpMenu.add(aboutMenuItem);
		
		//construct menu
		add(fileMenu);
		add(optionsMenu);
		add(helpMenu);
		
	}
	
	/**
	 * Adds an ActionListener to the components of this menu
	 * @param listener
	 */
	public void setActionListener(ActionListener listener)
	{
		exitMenuItem.addActionListener(listener);
		aboutMenuItem.addActionListener(listener);
	}

}
