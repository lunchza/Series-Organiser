package sorganiser.data;

import java.io.File;
import java.util.ArrayList;

/**
 * VideoFile class is a simple extension of the File class. Only video files will be recognised by this program
 * @author Peter Pretorius
 *
 */
public class VideoFile extends File {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//The extension of this video file
	private String extension;
	
	//season and episode numbers of this video file
	private int season, episode;

	public VideoFile(String path) {
		super(path);
		
		//grab extension from filename
		extension = path.substring(path.lastIndexOf("."));
		
		determineNumbers();
	}
	
	/**
	 * Attempts to find the season and episode numbers present in this filename
	 */
	public void determineNumbers()
	{
		ArrayList<String> numbers = getNumbers(getName());
		
		if (numbers.isEmpty())
		{
			episode = 0;
			season = 0;
		}
		
		else if (numbers.size() == 1)
			episode = Integer.parseInt(numbers.get(0));
		
		else
		{
			season = Integer.parseInt(numbers.get(0));
			episode = Integer.parseInt(numbers.get(1));
		}	
	}
	
	/**
	 * Finds groups of integers present in a filename
	 * @param n
	 * @return
	 */
	public ArrayList<String> getNumbers(String n)
	{
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
	
	public int getSeason()
	{
		return season;
	}
	
	public int getEpisode()
	{
		return episode;
	}

}
