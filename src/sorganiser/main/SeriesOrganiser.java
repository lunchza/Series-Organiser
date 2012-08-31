package sorganiser.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import sorganiser.gui.SOFrame;


public class SeriesOrganiser {
	
	//Program frame
	private SOFrame frame;
	
	public SeriesOrganiser()
	{
		//load all necessary components for program execution
		init();
		
		//show program frame
		frame.setVisible(true);	
	}
	
	public static void main(String[] args) {
		new SeriesOrganiser();
	}
	
	/**
	 * Loads the program interface, core components and sets up event listeners
	 */
	public void init()
	{
		//load GUI interface
		frame = new SOFrame();
		
		//load frame config
		if (!loadConfig())
			frame.loadDefaults();
	}
	
	/**
	 * Loads settings from the config file. If no config file exists, default settings are loaded.
	 * @return true if config was loaded, false if no config file found or failed to load config
	 */
	public boolean loadConfig()
	{
		//config file
		File configFile = new File("config");
		
		//first run or config file deleted
		if (!configFile.exists())
			return false;
		
		//establish scanner to read file contents
		Scanner sc = null;
		try {
			sc = new Scanner(configFile);
		} catch (FileNotFoundException e) {
			return false;
		}
		
		//read width from file
		int width = sc.nextInt();
		
		//read height from file
		int height = sc.nextInt();
		
		//read preserve setting from file TODO: notify the renamer (somewhere in data) about this setting
		boolean preserve = (sc.next().equals("T") ? true : false);
		
		//change frame size, and re-center
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		
		return true;
	}
}
