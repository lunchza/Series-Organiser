package sorganiser.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sorganiser.data.VideoFile;
import sorganiser.renamer.RenamePolicy;

/**
 * The frame that let's the user inspect the selected file, and 
 * @author Peter Pretorius
 *
 */
public class InspectFrame extends JFrame {
	
	//video file under inspection
	private VideoFile videoFile;
	
	//policy of inspected file
	private RenamePolicy policy;
	
	//known locations of season/episode numbers
	private int seasonIndex, episodeIndex;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InspectFrame(VideoFile inspectedFile, RenamePolicy inspectedPolicy)
	{
		super("Inspect file");
		videoFile = inspectedFile;
		policy = inspectedPolicy;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 200);
		String seasonNumber = policy.getSeasonNumber();
		String episodeNumber = policy.getEpisodeNumber();
		
		if (seasonNumber != null) //has season number
			seasonIndex = inspectedFile.getName().indexOf(seasonNumber);
		if (episodeNumber != null) //has episode number
			episodeIndex =inspectedFile.getName().indexOf(episodeNumber);
		
		setContentPane(new DrawPanel(videoFile.getName()));
		setLocationRelativeTo(null);
		setResizable(false);
		repaint();
	}
	
	private class DrawPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		//legend image
		private ImageIcon legendImage;
		
		
		private String text;
		
		public DrawPanel(String text)
		{
			this.text = text;
			
			//attempt to load image
			URL url = this.getClass().getResource("legend.jpg");
			legendImage = new ImageIcon(url);
			
		}
		
		public void paintComponent(Graphics g)
		{
			// Find the size of string 'text' in the current Graphics context g.

			FontMetrics fm   = g.getFontMetrics();
			java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g);

			int textHeight = (int)(rect.getHeight()); 
			int textWidth  = (int)(rect.getWidth());
			int panelHeight= this.getHeight();
			int panelWidth = this.getWidth();

			// Center text horizontally and vertically
			int x = (panelWidth  - textWidth)  / 2;
			int y = (panelHeight - textHeight) / 2  + fm.getAscent();
			
			g.drawImage(legendImage.getImage(), 0, 0, null);
			
			String[] nameFragments = getNameSubsets(text);
			
			//change font size
			g.setFont(new Font("Times New Roman", Font.BOLD, 16));
			
			if (nameFragments.length == 5) //season and episode number
			{
				int offset = 0;
				
				g.drawString(nameFragments[0], x, y); //pre-season
				
				offset += nameFragments[0].length()*7;
				
				g.setColor(Color.RED);
				
				g.drawString(nameFragments[1], x + offset, y); //season
				
				offset += nameFragments[1].length()*7;
				
				g.setColor(Color.BLACK);
				
				g.drawString(nameFragments[2], x + offset, y); //pre-ep
				
				offset += nameFragments[2].length()*7;
				
				g.setColor(Color.BLUE);
				
				g.drawString(nameFragments[3], x + offset, y);//ep
				
				offset += nameFragments[3].length()*7;
				
				g.setColor(Color.BLACK);
				
				g.drawString(nameFragments[4], x + offset, y);//post-ep
			}
			
			else 
			{
				int offset = 0;
				
				g.drawString(nameFragments[0], x, y); //pre-ep
				
				offset += nameFragments[0].length()*7;
				
				g.setColor(Color.BLUE);
				
				g.drawString(nameFragments[1], x + offset, y); //ep
				
				offset += nameFragments[1].length()*7;
				
				g.setColor(Color.BLACK);
				
				g.drawString(nameFragments[2], x + offset, y);
			}
			
		}
	}
	
	/**
	 * Returns either a 3-tuple or 5-tuple set, factoring the String s into the following substrings:
	 * 1: pre-season number
	 * 2: season number
	 * 3: pre-episode number **if there is no season index then the above-regarded season number is considered to be the episode number and the method returns here
	 * 4: episode number
	 * 5: post-episode number
	 * @param s
	 * @return
	 */
	private String[] getNameSubsets(String s)
	{
		if (policy.getSeasonNumber().equals("XX")) //no season index
		{
			String ep = policy.getEpisodeNumber();
			
			String preEp = s.substring(0, s.indexOf(ep));
			String postEp = s.substring(s.indexOf(ep) + ep.length());
			
			return new String[]{preEp, ep, postEp};
		}
		
		else
		{
			String season = policy.getSeasonNumber();
			String ep = policy.getEpisodeNumber();
			
			int counter = 0;
			
			String preSeason = s.substring(0, s.indexOf(season));
			counter += preSeason.length();
			
			counter += season.length();
			
			String preEp = s.substring(counter, s.indexOf(ep));
			counter += preEp.length();
			
			counter += ep.length();
			
			String postEp = s.substring(counter);
			
			return new String[]{preSeason, season, preEp, ep, postEp};
		}
	}
}
