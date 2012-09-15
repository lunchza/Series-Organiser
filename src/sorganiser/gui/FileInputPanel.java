package sorganiser.gui;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import sorganiser.data.VideoFile;

/**
 * This Panel houses the set of components on the left side of the panel window
 * @author Peter Pretorius
 *
 */
public class FileInputPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Northern panel houses the label and button
	private JPanel northPanel;
	
	//"Files in folder" label
	private JLabel filesLabel;
	
	//Browse button
	private JButton browseButton;
	
	//Popup menu
	private ContextMenu contextMenu;
	
	//List of filenames of files in the current browsed-to directory
	private JList<VideoFile> filesList;
	
	public FileInputPanel()
	{
		//establish layout
		setLayout(new java.awt.BorderLayout());
		
		//initialise components
		init();
	}

	private void init() {
		
		//construct panels
		northPanel = new JPanel(new java.awt.FlowLayout());
		
		//establish files label
		filesLabel = new JLabel("Files in directory:");
		
		//instantiate browse button
		browseButton = new JButton("Browse...");
		
		//instantiate file list
		filesList = new JList<VideoFile>();
		filesList.setPreferredSize(new java.awt.Dimension(300, getHeight()));
		filesList.setCellRenderer(new CustomCellRenderer());
		//register context menu
		filesList.addMouseListener(new ListPopupListener());
		
		//add label and button to north panel
		northPanel.add(filesLabel);
		northPanel.add(browseButton);
		
		//init context menu
		contextMenu = new ContextMenu();
		
		add(northPanel, java.awt.BorderLayout.NORTH);
		add(new JScrollPane(filesList), java.awt.BorderLayout.CENTER);
	}
	
	/**
	 * Allows access to the file input list
	 * @return
	 
	public JList<String> getInputFilesList()
	{
		return filesList;
	}*/
	
	/**
	 * Changes the contents of the fileInputList
	 * @param newContents
	 */
	public void setListContents(VideoFile[] newContents)
	{
		filesList.setListData(newContents);
	}
	
	/**
	 * Retrieves the video file at the specified index
	 * @return
	 */
	public VideoFile getFileAt(int index)
	{
		return filesList.getModel().getElementAt(index);
	}
	
	/**
	 * Checks if the file at index has been excluded
	 * @param index
	 * @return
	 */
	public boolean isExcluded(int index)
	{
		return getFileAt(index).isExcluded();
	}
	
	/**
	 * Assigns an ActionListener to the components of this Panel
	 * @param listener
	 */
	public void registerActionListener(ActionListener listener)
	{
		browseButton.addActionListener(listener);
	}

	public JButton getBrowseButton()
	{
		return browseButton;
	}	
	
	/**
	 * This class will handle right-clicks on the JList displaying the appropriate context menu
	 * @author Peter Pretorius
	 *
	 */
	private class ListPopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			if (filesList.getModel().getSize() != 0) //Don't show context menu for empty files list
				check(e);
		}

		private void check(MouseEvent e) {
			 if (e.getButton() == MouseEvent.BUTTON3) { //if the event shows the menu
				 int clickedIndex = filesList.locationToIndex(e.getPoint());
			        filesList.setSelectedIndex(clickedIndex); //select the item
			        contextMenu.setClickedIndex(clickedIndex);
			        contextMenu.setExcluded(filesList.getSelectedValue().isExcluded());
			        contextMenu.show(filesList, e.getX(), e.getY()); //and show the menu
			    }
		}
	}
}
