package sorganiser.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

/**
 * The panel in the center of the main program
 * @author Peter Pretorius
 *
 */
public class CenterPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//format label
	private JLabel formatLabel;
	
	//label that informs the users about missing episode markers
	private JLabel missingMarkersLabel, missingMarkersLabel2;
	
	//format text field
	private JTextField formatField;
	
	//arrow image
	private ImageIcon arrowImage;
	
	//default format field text
	public static final String DEFAULT_FORMAT = "SeriesName.SXX.EYY";
	
	//determines whether or not the format field is valid i.e. contains proper markers and is non-empty
	private boolean validFormat;
	
	public CenterPanel()
	{
		//layout
		setLayout(new java.awt.GridLayout(10, 1));
		
		init();
		startNameValidator();
	}

	private void init() {
		//establish format label
		formatLabel = new JLabel("                                                    New format:");
		
		//establish missing markers label
		missingMarkersLabel = new JLabel("Specify season/episode marker location");
		missingMarkersLabel2 = new JLabel("season - XX, episode - YY");
		missingMarkersLabel.setForeground(Color.RED);
		missingMarkersLabel2.setForeground(Color.RED);
		missingMarkersLabel.setVisible(false);
		missingMarkersLabel2.setVisible(false);
		
		//init format text field
		formatField = new JTextField(DEFAULT_FORMAT);
		formatField.setColumns(20); //maximum 20 columns in format field
		formatField.setHorizontalAlignment(JTextField.CENTER); //format field contents are centered
		formatField.setEditable(false); //format field is initially not editable
		Font font = new Font("Times New Roman", Font.PLAIN, 30); //font for the format field is larger than default
		formatField.setFont(font);
		
		//attempt to load image
		URL url = this.getClass().getResource("arrow.jpg");
		arrowImage = new ImageIcon(url);
		
		add(new JPanel());
		add(missingMarkersLabel);
		add(missingMarkersLabel2);
		add(formatLabel);
		add(formatField);
		add(new JPanel());
		add(new ImagePanel());
		
	}
	
	/**
	 * Allows the format field to be edited
	 */
	public void enableFormatEditing()
	{
		formatField.setEditable(true);
	}
	
	/**
	 * Changes the text in the format field
	 * @param format
	 */
	public void setFormat(String format)
	{
		formatField.setText(format);
	}
	
	/**
	 * This method starts a new thread which checks periodically whether or not the format field is valid i.e. contains the correct
	 * markers etc. If the field is invalid, the missing markers label is set to visible
	 */
	private void startNameValidator()
	{
		new Thread()
		{
			public void run()
			{
				while(true)
				{
					if(!formatField.getText().contains("YY"))
					{
						missingMarkersLabel.setVisible(true);
						missingMarkersLabel2.setVisible(true);
						validFormat = false;
					}
					else
					{
						missingMarkersLabel.setVisible(false);
						missingMarkersLabel2.setVisible(false);
						validFormat = true;
					}
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						
					}
				}
			}
		}.start();
	}
	
	/**
	 * Assigns a Document Listener to the format field of this Panel
	 * @param docListener
	 */
	public void registerDocumentListener(DocumentListener docListener) {
		formatField.getDocument().addDocumentListener(docListener);
	}
	
	/**
	 * Returns the format specified by the format field
	 * @return
	 */
	public String getFormat()
	{
		return formatField.getText();
	}
	
	public boolean formatIsValid()
	{
		return validFormat;
	}

	private class ImagePanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g)
		{
			g.drawImage(arrowImage.getImage(), 125, 0, null);
		}
	}
}
