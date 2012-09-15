package sorganiser.renamer;

import java.util.ArrayList;

public class Renamer {
	
	//singleton
	private static Renamer renamer;

	/**
	 * The list of policies employed by this Renamer. Each policy is different for each input file
	 */
	private ArrayList<RenamePolicy> policies;
	
	public Renamer()
	{
		policies = new ArrayList<RenamePolicy>();
	}
	
	/**
	 * Creates a fresh set of policies for a specified number of input files
	 * @param numFiles
	 */
	public void createPolicies(int numFiles)
	{
		for (int i = 0; i < numFiles; i++)
			policies.add(new RenamePolicy(RenamePolicy.SEASON_AND_EPISODE_NUMBERS));
	}
	
	public ArrayList<RenamePolicy> getRenamePolicies()
	{
		return policies;
	}
	
	//singleton
	public static Renamer getInstance()
	{
		if (renamer == null)
			renamer = new Renamer();
		
		return renamer;
	}

	@SuppressWarnings("finally")
	public static String rename(RenamePolicy policy, String outFormat) {
		String season = null;
		String episode = null;
		
		int rule = policy.getRule(); //rule determines which numbers we need to deal with
		
		if (rule == RenamePolicy.SEASON_AND_EPISODE_NUMBERS)
		{
			//find season and episode number
			try {
				season = policy.getKeyNumbers().get(policy.getSeasonIndex());
				episode = policy.getKeyNumbers().get(policy.getEpisodeIndex());
				policy.registerSeasonNumber(season);
				policy.registerEpisodeNumber(episode);
			} catch (IndexOutOfBoundsException e) {
				episode = "YY";
			}
			
			finally
			{
				if (season.length() == 3 && !season.startsWith("0")) //season number greater than 100? Yeah right...
				{
					episode = "YY"; //Invalidates episode number, forcing the algorithm to re-evaluate
				}
				
				
				//Looking for 2 numbers but only found 1
				if (episode.equals("YY"))
				{
					String tempSeason = season;
					//four digits found for season, assume it's in XXYY format
					if (season.length() == 4)
					{
						season = tempSeason.substring(0, 2);
						episode = tempSeason.substring(2);
					}

					//three digits found for season, assume it's in XYY format
					else if (season.length() == 3)
					{
						season = ""+tempSeason.charAt(0);
						episode = tempSeason.substring(1);
					}
					
					else //Found a 2 digit number and nothing else, must be the episode number
					{
						episode = season;
						season = "XX"; //Let the user know that the season number for this episode could not be determined
					}
				}
				
				policy.registerSeasonNumber(season);
				policy.registerEpisodeNumber(episode);
				
				if (season.length() == 1) //pad season to 2 digits
					season = "0" + season;
				
				if (episode.length() == 1) //pad episode to 2 digits
					episode = "0" + episode;
			
				
				outFormat = outFormat.replace("XX", season);
				outFormat = outFormat.replace("YY", episode);
				
				return outFormat;
			}
		}

		else
		{
			try {
				episode = policy.getKeyNumbers().get(policy.getEpisodeIndex());
			} catch (IndexOutOfBoundsException e) {
				episode = "YY";
			}
			finally
			{
				if (episode.length() == 1) //pad episode to 2 digits
					episode = "0" + episode;
				
				outFormat = outFormat.replace("YY", episode);
				
				return outFormat;
			}	
		}
	}
}
