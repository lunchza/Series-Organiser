package sorganiser.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * The right-click menu for the input files list. This class delegates recorded Actions to the main class
 * @author Peter Pretorius
 *
 */
public class ContextMenu extends JPopupMenu implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenuItem inspect, exclude;
	
	//records which element on the filesList was clicked
	private int clickedIndex;
	
	public ContextMenu()
	{
		inspect = new JMenuItem("Inspect file");
		inspect.addActionListener(this);
		exclude = new JMenuItem("Exclude file");
		exclude.addActionListener(this);
		add(inspect);
		add(exclude);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == inspect)
			SOFrame.getInstance().inspect(clickedIndex);
		
		else if (e.getSource() == exclude)
			SOFrame.getInstance().toggleExclude(clickedIndex);
	}
	
	/**
	 * 
	 */
	public void setExcluded(boolean b)
	{
		if (b) //file already excluded
			exclude.setText("Include file");
		else
			exclude.setText("Exclude File");
	}
	
	public void setClickedIndex(int i)
	{
		clickedIndex = i;
	}
}
