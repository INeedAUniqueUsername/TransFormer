package System;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;

public class WindowPanel extends JPanel {
	static SystemElement selectedElement;
	static ArrayList<SystemElement> elements;
	static SystemElement system;
	static PropertiesPanel properties;
	static GraphicsPanel graphics;
	static OptionsPanel options;
	
	final static Font FONT_TITLE = new Font("Convection", Font.BOLD, 32);
	final static Font FONT_SUBTITLE = new Font("Convection", 0, 22);
	final static Font FONT_TEXT = new Font("Convection", 0, 16);
	final static Font FONT_CODE = new Font("Consolas", 0, 20);
	public void setPanels(PropertiesPanel p, GraphicsPanel g, OptionsPanel o)
	{
		properties = p;
		graphics = g;
		options = o;
	}
	public void selectElement(SystemElement se)
	{
		system.setSelectedFamily(false);
		/*
		if(selectedElement != null)
		{
			selectedElement.setSelected(false);
		}
		*/
		selectedElement = se;
		se.setSelected(true);
		properties.initializeElement(se);
		//graphics.repaint();
		options.initializeElement(se);
		System.out.println("Selected element: " + selectedElement.getName());
	}
	public void repaintAll()
	{
		properties.repaint();
		graphics.repaint();
		options.repaint();
	}
}
