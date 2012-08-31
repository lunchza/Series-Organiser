package sorganiser.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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
		
		//add label and button to north panel
		northPanel.add(filesLabel);
		northPanel.add(browseButton);
		
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
}
