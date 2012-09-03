package sorganiser.data;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import sorganiser.renamer.RenamePolicy;
import sorganiser.renamer.Renamer;

/**
 * This class handles all file operations, from the initial list population to the renaming at the end
 * @author Peter Pretorius
 *
 */
public class FileHandler {
	//singleton
	private static FileHandler fileHandler;
	
	//file chooser object
	private JFileChooser fileChooser;
	
	public FileHandler()
	{
			//initialise file chooser
		   	fileChooser = new JFileChooser(new File("."));
	        fileChooser.setMultiSelectionEnabled(false);
	        fileChooser.setDialogTitle("Choose a video folder");
	        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        fileChooser.setAcceptAllFileFilterUsed(true);
	}
	
	
	//singleton instance
	public static FileHandler getInstance()
	{
		if (fileHandler == null)
			fileHandler = new FileHandler();
		
		return fileHandler;
	}
	
	/**
	 * Opens a file dialog that allows the current directory to be changed
	 */
	public File changeDirectory()
	{
		int result = fileChooser.showOpenDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		else
			return null;
	}
	
	/**
	 * Determines the new names of files, based on the specified output format.
	 * @param inputList list of original files
	 * @param outFormat specified output format
	 * @return arraylist of newly named files
	 */
	public ArrayList<VideoFile> determineNewNames(ArrayList<VideoFile> inputList, String outFormat)
	{
		ArrayList<VideoFile> outputList = new ArrayList<VideoFile>(inputList.size());
		
		File parentFolder = inputList.get(0).getParentFile();
		
		//For each video in the input list 
		for (int i = 0; i < inputList.size(); i++)
		{
			//Get the policy for this video
			RenamePolicy policy = Renamer.getInstance().getRenamePolicies().get(i);
			
			ArrayList<String> numbers = inputList.get(i).getNumbers();
			
			policy.setKeyNumbers(numbers);
			
			//Renamer determines new name and renames accordingly
			String newFormat = Renamer.rename(policy, outFormat);
			
			//New File name is the format with replacements, concatted with the extension
			String newFileName = newFormat.concat(inputList.get(i).getExtension());
			
			
			//Add new file to the output list
			outputList.add(new VideoFile(parentFolder.getAbsolutePath().concat("\\" + newFileName)));
		}		
		return outputList;
	}
	
	/**
	 * Determines if the file parameter is in fact a video file. It does so by checking the file extension and 
	 * comparing it to known video-file extensions
	 * @param f the file under scrutiny
	 * @return true if f is a video file (as recognised by this program)
	 */
	public boolean isVideoFile(File f)
	{
		//directories are ignored
		if (f.isDirectory())
			return false;
		
		String extension = f.getName().substring(f.getName().lastIndexOf("."));
		if (extension.equals(".avi") || extension.equals(".mkv") || extension.equals(".wmv") || extension.equals(".mpg") || extension.equals(".rm") || extension.equals(".rmvb") || extension.equals(".mp4") || extension.equals(".m4v") || extension.equals(".flv") || extension.equals(".mov"))
		{
			return true;
		}
		return false;
	}


	/**
	 * Scans every sub-file in the specified folder, looking for an eligible series name
	 * @param root
	 * @return null if no suitable name was found
	 */
	public String determineSeriesName(File root) {
		
		//initialise potential name to empty string
		String candidateName = "";
		
		//iterate through every sub-file
		for (File subFile: root.listFiles())
		{
			//ignore non-video files
			if (!isVideoFile(subFile))
				continue;
		
		//Get the name of the current file
		String name = subFile.getName();
		
		//iterate through every character in the name, appending non-number chars to the candidate name
		int currentIndex = 0;
			while (!Character.isDigit(name.charAt(currentIndex)))
				candidateName = candidateName.concat(""+name.charAt(currentIndex++));
		
		//Non-empty candidate name found
		if (!candidateName.equals(""))
			return candidateName.trim().replace(" ", ".");		
		}
		
		//No candidate name found. Ask the user if they want to use the name of the root folder as the series name
		if (candidateName.equals(""))
		{
			int result = JOptionPane.showConfirmDialog(null, "It appears that the series name is not present in any of the filenames. Would you like to use the root folder name \'" + root.getName() + "\' as the series name?", "Could not determine series name", JOptionPane.YES_NO_OPTION);
					
			if (result == JOptionPane.YES_OPTION)
				return root.getName().trim().replace(" ", ".");
		}
		
		//no candidate name found
		return null;
	}
}
