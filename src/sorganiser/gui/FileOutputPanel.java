package sorganiser.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sorganiser.data.VideoFile;

/**
 * This Panel houses the set of components on the right side of the panel window
 * @author Peter Pretorius
 *
 */
public class FileOutputPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Northern panel houses the label and button
	private JPanel northPanel;

	//"Files after renaming" label
	private JLabel filesLabel;
		
	//Convert button
	private JButton convertButton;
	
	//List of filenames after converting to format
	private JList<VideoFile> filesList;
	
	public FileOutputPanel()
	{
		//establish layout
		setLayout(new BorderLayout());
		
		//initialise components
		init();
	}

	/**
	 * Initialise components of this panel
	 */
	private void init() {
		
		//construct panels
		northPanel = new JPanel(new java.awt.FlowLayout());
		
		//establish files label
		filesLabel = new JLabel("Files after renaming:");
		
		//instantiate convert button
		convertButton = new JButton("Convert");
		
		//instantiate file list
		filesList = new JList<VideoFile>();
		filesList.setCellRenderer(new CustomCellRenderer());
		
		//add all components to this frame
		northPanel.add(filesLabel);
		northPanel.add(convertButton);
		add(northPanel, BorderLayout.NORTH);
		add(new JScrollPane(filesList), BorderLayout.CENTER);
	}
	
	/**
	 * Assigns an ActionListener to the components of this Panel
	 * @param handler
	 */
	public void registerActionListener(ActionListener handler)
	{
		convertButton.addActionListener(handler);
	}

	/**
	 * Changes the contents of the fileOutputList to the contents of the specified array
	 * @param newContents
	 */
	public void setListContents(VideoFile[] newContents)
	{
		filesList.setListData(newContents);
	}
	
	/**
	 * Changes the contents of the fileOutputList to the contents of the specified ArrayList
	 * @param newContents
	 */
	public void setListContents(ArrayList<VideoFile> newContents)
	{
		//convert to array
		VideoFile[] fileListArray = new VideoFile[newContents.size()];
		for (int i = 0; i < newContents.size(); i++)
			fileListArray[i] = newContents.get(i);
		
		setListContents(fileListArray);
	}
	
	public JButton getConvertButton() {
		return convertButton;
	}
	
	public JList<VideoFile> getOutputFileList()
	{
		return filesList;
	}
}
