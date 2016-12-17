package System;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Station extends SystemElement {
	String attribute_chance;
	String attribute_name;
	String attribute_type;
	
	public Station() {
		subelements = new String[]{"Orbitals"};
		attributeKeys = new String[]{"Name", "Type"};
	}
	public void paint(Graphics g)
	{
		int parent_x = (int) parent.getPosX();
		int parent_y = (int) parent.getPosY();
		int width = 10;
		int width_half = width/2;
		parent_x -= width_half;
		parent_y -= width_half;
		g.setColor(selected ? Color.CYAN : Color.GRAY);
		g.drawRect(parent_x, parent_y, width, width);
	}
}
