package sorganiser.gui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;

import sorganiser.data.VideoFile;

class CustomCellRenderer extends DefaultListCellRenderer  {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof VideoFile) {
            VideoFile file = (VideoFile) value;
            
            
            
            setText(file.getName());
            setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));

            if (!file.isExcluded())
            {

            	if (isSelected) {
            		setBackground(list.getSelectionBackground());
            		setForeground(list.getSelectionForeground());

            	} else {
            		setBackground(list.getBackground());
            		setForeground(list.getForeground());     		
            	}
            }
            
            else
            {
            	if (isSelected) {
            		setBackground(list.getSelectionBackground());
            		setForeground(Color.GRAY);

            	} else {
            		setBackground(list.getBackground());
            		setForeground(Color.GRAY);     		
            	}
            }
            
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            repaint();
        }
        return this;
    }
}