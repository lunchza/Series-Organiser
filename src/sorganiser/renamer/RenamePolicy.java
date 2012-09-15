package sorganiser.renamer;

import java.util.ArrayList;

/**
 * A rename policy consistently chooses the same indices for season and episode numbers. The default policy takes 
 * @author Peter Pretorius
 *
 */
public class RenamePolicy {
	
	public static final int DEFAULT_SEASON_INDEX = 0; 
	public static final int DEFAULT_EPISODE_INDEX = 1; 
	
	public static final int SEASON_AND_EPISODE_NUMBERS = 0;
	public static final int EPISODE_NUMBERS_ONLY = 1;
	
	/**
	 * The indices that this policy recognises as being the season and episode indexes
	 */
	private int seasonIndex, episodeIndex;
	
	/**
	 * String representation of season and episode numbers
	 */
	private String seasonNumber, episodeNumber;
	
	/**
	 * The key numbers used by this policy
	 */
	private ArrayList<String> keyNumbers;
	
	public RenamePolicy(int rule)
	{
		seasonIndex = episodeIndex = -1;
		
		if (rule == SEASON_AND_EPISODE_NUMBERS) //season and episode
		{
			seasonIndex = DEFAULT_SEASON_INDEX;
			episodeIndex = DEFAULT_EPISODE_INDEX;
		}
		
		else //episode only
			episodeIndex = DEFAULT_SEASON_INDEX;
	}
	
	public RenamePolicy(int sI, int eI)
	{
		seasonIndex = sI;
		episodeIndex = eI;
	}
	
	public int getSeasonIndex()
	{
		return seasonIndex;
	}
	
	public int getEpisodeIndex()
	{
		return episodeIndex;
	}
	
	public void setSeasonIndex(int sI)
	{
		seasonIndex = sI;
	}
	
	public void setEpisodeIndex(int eI)
	{
		episodeIndex = eI;
	}
	
	public void setKeyNumbers(ArrayList<String> numbers)
	{
		keyNumbers = numbers;
	}
	
	public ArrayList<String> getKeyNumbers()
	{
		return keyNumbers;
	}
	
	public void changeRule()
	{
		if (seasonIndex == -1)
		{
			seasonIndex = DEFAULT_SEASON_INDEX;
			episodeIndex = DEFAULT_EPISODE_INDEX;
		}
		
		else
			episodeIndex = DEFAULT_SEASON_INDEX;
			
	}
	
	public void registerSeasonNumber(String s)
	{
		seasonNumber = s;
	}
	
	public void registerEpisodeNumber(String s)
	{
		episodeNumber = s;
	}
	
	public int getRule()
	{
		if (seasonIndex == -1)
			return EPISODE_NUMBERS_ONLY;
		else
			return SEASON_AND_EPISODE_NUMBERS;
	}

	public String getSeasonNumber() {
		return seasonNumber;
	}

	public String getEpisodeNumber() {
		return episodeNumber;
	}

	public void setSeasonNumber(String seasonNumber) {
		this.seasonNumber = seasonNumber;
	}

	public void setEpisodeNumber(String episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

}
