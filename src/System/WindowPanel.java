package System;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class WindowPanel extends JPanel {
	static SystemElement selectedElement;
	static SystemElement system;
	static PropertiesPanel properties;
	static GraphicsPanel graphics;
	static OptionsPanel options;
	
	final static Font FONT_TITLE = new Font("Convection", Font.BOLD, 32);
	final static Font FONT_SUBTITLE = new Font("Convection", 0, 22);
	final static Font FONT_TEXT = new Font("Convection", 0, 16);
	final static Font FONT_CODE = new Font("Consolas", 0, 20);
	
	public void init()
	{
		system = new SystemGroup();
		
		properties = new PropertiesPanel();
		properties.init();
		add(properties);
		
		graphics = new GraphicsPanel();
		graphics.setLayout(null);
		graphics.setPreferredSize(new Dimension(1280, 1080));
		graphics.setName("View");
		graphics.init();
		add(graphics);
		
		options = new OptionsPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		options.setAlignmentY(0f);
		options.setPreferredSize(new Dimension(320, 1080));
		options.init();
		add(options);
		
		selectElement(system);
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
		graphics.repaint();
		options.initializeElement(se);
		System.out.println("Selected element: " + selectedElement.toString());
	}
	public void repaintAll()
	{
		properties.repaint();
		graphics.repaint();
		options.repaint();
	}
}
