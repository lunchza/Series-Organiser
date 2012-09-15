package sorganiser.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sorganiser.data.FileHandler;
import sorganiser.data.VideoFile;
import sorganiser.main.SOException;
import sorganiser.renamer.RenamePolicy;
import sorganiser.renamer.Renamer;

/**
 * The main program window
 * @author Peter Pretorius
 *
 */
public class SOFrame extends JFrame {
	//singleton
	private static SOFrame soFrame = new SOFrame();
	
	//main program panel
	private JPanel panel;
	
	//File input panel is on the left side of the frame
	private FileInputPanel fileInputPanel;
	
	//Center panel houses the components in the center
	private CenterPanel centerPanel;
	
	//File output panel is on the right side of the frame
	private FileOutputPanel fileOutputPanel;
	
	//layout for this frame
	private BorderLayout layout;
	
	//menu bar
	private SOMenu menuBar;
	
	//status bar
	private JStatusBar statusBar;
	
	//The list of VideoFile objects that are represented by the input file list
	private ArrayList<VideoFile> inputFiles;
	
	//The list of VideoFile objects that are represented by the output file list
	private ArrayList<VideoFile> outputFiles;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SOFrame()
	{
		super("Series Organiser v2.0");
		
		//for now, program quits on frame close
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Frame is non-resizable
		setResizable(false);
		
		//frame starts centered
		setLocationRelativeTo(null);
		
		init();
		
		// register event handler with the interface
		registerEventHandler();
	}
	
	/**
	 * Initialises all of the subcomponents of this frame
	 */
	private void init()
	{
		//instantiate the BorderLayout used by this menu. There is a gap of 50 pixels between
		//layout areas
		layout = new BorderLayout();
		layout.setHgap(50);
		
		//instantiate panel
		panel = new JPanel(layout);
		
		//instantiate menu
		menuBar = new SOMenu();
		
		//add menu to frame
		setJMenuBar(menuBar);
		
		//instantiate status bar
		statusBar = new JStatusBar(JStatusBar.LEFT_ORIENTATION);
		statusBar.setStatus("Program running");
		
		//instantiate file input panel
		fileInputPanel = new FileInputPanel();
		
		//add file input panel to panel
		panel.add(fileInputPanel, java.awt.BorderLayout.WEST);
		
		//instantiate center panel
		centerPanel = new CenterPanel();
		
		//add center panel to panel
		panel.add(centerPanel, java.awt.BorderLayout.CENTER);
		
		//instantiate file output panel
		fileOutputPanel = new FileOutputPanel();
		
		//add file output panel to panel
		panel.add(fileOutputPanel, java.awt.BorderLayout.EAST);
		
		//add statusbar to panel
		panel.add(statusBar, java.awt.BorderLayout.SOUTH);
		
		//add panel to frame
		setContentPane(panel);
	}
	
	/**
	 * Sets up listeners for all subcomponents of this frame
	 * @param handler
	 */
	public void registerEventHandler()
	{
		ClickHandler handler = new ClickHandler();
		//Register ClickHandler for both side panels
		fileInputPanel.registerActionListener(handler);
		fileOutputPanel.registerActionListener(handler);
		
		MyDocumentListener docListener = new MyDocumentListener();
		//register document listener for the center panel
		centerPanel.registerDocumentListener(docListener);
		
		//Establishes a mouse listener for the output file list that allows editing by way of double-clicking
			MouseListener mouseListener = new MouseAdapter() {
				   public void mouseClicked(MouseEvent e) {
				       if (e.getClickCount() == 2) {
				          int index = fileOutputPanel.getOutputFileList().locationToIndex(e.getPoint());
				          edit(index);
				        }
				   }
			};
			//register mouse listener for output panel
			fileOutputPanel.registerMouseListener(mouseListener);
	}

	/**
	 * Loads default settings for this frame
	 */
	public void loadDefaults() {
		
		//Default program size is 1024x768 pixels
		setSize(1024, 768);	
		
		//re-center
		setLocationRelativeTo(null);
	}
	
	/**
	 * Once a directory is chosen by the user, this method populates the file input list with the sub-video files
	 * in that folder. It then creates renaming policies for all of those input files
	 * @param root
	 * @throws SOException
	 */
	public void setRootDirectory(File root) throws SOException
	{
		//confirm that root is a directory
		if (!root.isDirectory())
			throw new SOException("Supplied file is not a directory!");
		
		//generate subfile list from supplied folder
		File[] subFiles = root.listFiles();
		
		//subVideoFiles is a subset of subFiles, consisting of video files only. All other file types are ignored
		ArrayList<VideoFile> subVideoFiles = new ArrayList<VideoFile>();
		
		//Populate the list of video files
		FileHandler fileHandler = FileHandler.getInstance();
		for (File subFile: subFiles)
			if (fileHandler.isVideoFile(subFile))
				subVideoFiles.add(new VideoFile(subFile.getAbsolutePath()));
		
		//Empty list, no video files found
		if (subVideoFiles.size() == 0)
			throw new SOException("No video files found in directory " + root.getAbsolutePath());
		
		//Convert ArrayList to array of video files
		VideoFile[] subVideoFilesArray = new VideoFile[subVideoFiles.size()];
		for (int i = 0; i < subVideoFiles.size(); i++)
			subVideoFilesArray[i] = subVideoFiles.get(i);
		
		//inputFiles is assigned here
		inputFiles = subVideoFiles;
		
		//Finally, Change File Input list contents
		fileInputPanel.setListContents(subVideoFilesArray);
		
		//create default policies for input files
		Renamer.getInstance().createPolicies(inputFiles.size());
		
		//Change program status
		setStatus("Found " + subVideoFilesArray.length + " episodes in folder \'" + root.getAbsolutePath() + "\'");
	}
	
	/**
	 * Allows the user to inspect the file at the selected index
	 * @param index
	 */
	public void inspect(int index)
	{
		VideoFile inspectedFile = fileInputPanel.getFileAt(index); //get file object
		RenamePolicy inspectedPolicy = Renamer.getInstance().getRenamePolicies().get(index);
		new InspectFrame(inspectedFile, inspectedPolicy).setVisible(true);
	}
	
	/**
	 * Excludes the specified file from the renaming process
	 * @param index
	 */
	public void toggleExclude(int index)
	{
		VideoFile excludedFile = fileInputPanel.getFileAt(index); //get file object
		excludedFile.toggleExclusion();
	}
	
	/**
	 * Allows the user to change the output name of the file at index
	 * @param index
	 */
	public void edit(int index)
	{
		String editedName = JOptionPane.showInputDialog(null, "Edit the name as desired.", outputFiles.get(index).getName());
		if (editedName != null)
		{
			VideoFile file = new VideoFile(outputFiles.get(index).getParentFile().getAbsolutePath().concat("\\" + editedName));
			outputFiles.remove(index);
			outputFiles.add(index, file);
			
			//update list contents to reflect change
			fileOutputPanel.setListContents(outputFiles);
			setStatus("Edit successful");
		}
	}
	
	/**
	 * Change the status of the statusBar
	 * @param m
	 */
	public void setStatus(String m)
	{
		statusBar.setStatus(m);
	}
	
	//singleton
	public static SOFrame getInstance()
	{
		return soFrame;
	}

	/**
	 * Private inner class handles events for this frame
	 * @author Peter Pretorius
	 *
	 */
	private class ClickHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt) {
			//clicked on browse button
			if (evt.getSource() == fileInputPanel.getBrowseButton())
			{
				//get filehandler object, change directory and populate input list
				FileHandler fileHandler = FileHandler.getInstance();
				File root = fileHandler.changeDirectory();
				
				//user closed the file dialog, do nothing
				if (root == null)
					return;
				
				try {
					setRootDirectory(root);
					
					//Attempt to detect series name and if successful, change the format field accordingly
					String name = fileHandler.determineSeriesName(root);
					if (name != null)
						centerPanel.setFormat(name);
					else
						centerPanel.setFormat(CenterPanel.DEFAULT_FORMAT);
					
					//format field now becomes editable
					centerPanel.enableFormatEditing();
							
					//Populate output file list straight away
					//output files is assigned here
					outputFiles = new ArrayList<VideoFile>(fileHandler.determineNewNames(inputFiles, centerPanel.getFormat()));
							
					fileOutputPanel.setListContents(outputFiles);
					
					
				} catch (SOException e) {
					//If the program fails to populate the input list, the status is set to the returned error message
					setStatus(e.getMessage());
				}
			}
			
			//clicked on convert button
			if (evt.getSource() == fileOutputPanel.getConvertButton())
			{
				if (fileOutputPanel.getOutputFileList().getModel().getSize() == 0)
					JOptionPane.showMessageDialog(null, "Output list is empty");
				else
				{
					for (int i = 0; i < inputFiles.size(); i++)
					{
						//rename files with exclusion check
						if(!inputFiles.get(i).isExcluded())
							inputFiles.get(i).renameTo(outputFiles.get(i));
					}
					//refresh input file list to reflect new folder contents
					try {
						setRootDirectory(inputFiles.get(0).getParentFile());
					} catch (SOException e) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please contact Peter Pretorius (ERROR_CODE = 2");
					}
					
					JOptionPane.showMessageDialog(null, "Conversion complete!");
				}
			}
		}
	}
	
	/**
	 * This Listener ensures that output dynamically changes as the output format is changed. 
	 * @author Peter Pretorius
	 *
	 */
	private class MyDocumentListener implements DocumentListener
	{

		@Override
		public void changedUpdate(DocumentEvent arg0) {
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			ArrayList<VideoFile> outFileList = FileHandler.getInstance().determineNewNames(inputFiles, centerPanel.getFormat());
			fileOutputPanel.setListContents(outFileList);
			outputFiles = outFileList;
			
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			ArrayList<VideoFile> outFileList = FileHandler.getInstance().determineNewNames(inputFiles, centerPanel.getFormat());
			fileOutputPanel.setListContents(outFileList);
			outputFiles = outFileList;
		}
		
	}
}
