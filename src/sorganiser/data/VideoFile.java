package sorganiser.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * VideoFile class is a simple extension of the File class. Only video files will be recognised by this program
 * @author Peter Pretorius
 *
 */
public class VideoFile extends File implements Comparable<File> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//The extension of this video file
	private String extension;
	
	//The comparator for this class assists with list sorting
	private static Comparator<VideoFile> comparator;

	public VideoFile(String path) {
		super(path);
		
		comparator = new VideoComparator();
		
		//grab extension from filename
		extension = path.substring(path.lastIndexOf("."));
		
	}
	
	/**
	 * Finds groups of integers present in this file's filename
	 * @param n
	 * @return
	 */
	public ArrayList<String> getNumbers()
	{
		String n = this.getName();
		char[] chars = n.toCharArray();
		ArrayList<String> int_groups = new ArrayList<String>(); //The collection of all groups of integers present in a filename
		String int_group = ""; //The current group of integers
		for (int j = 0; j < chars.length-1; j++)
		{
			if (Character.isDigit(chars[j])) //search for a digit
			{
				int_group += (chars[j]+"");
				int curIndex = j+1;
				while (Character.isDigit(chars[curIndex])) //grab all consecutive digits
				{
					int_group += (chars[curIndex++]+"");
					j++;
				}
				int_groups.add(int_group);
				int_group = "";
			}
		}
		return int_groups;
	}
	
	public String getExtension()
	{
		return extension;
	}

	public static Comparator<VideoFile> getComparator()
	{
		return comparator;
	}
	

	private class VideoComparator implements Comparator<VideoFile>
	{

		@Override
		public int compare(VideoFile vf1, VideoFile vf2) {
						
			return vf1.getName().compareTo(vf2.getName());
		}
		
	}

}
