package System;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import Other.Orbit;

public class Group extends SystemElement{
	public Group()
	{
		subelements = new String[]{"Primary", "Siblings", "Orbitals"};
		selectedColor = new Color(255, 255, 0, 85);
	}
	public void paint(Graphics g, Orbit o)
	{
		System.out.println("Painting <Group>");
		g.setColor(getColor());
		if(hasVisibleChildren())
		{
			paintChildren(g, o);
		} else {
			Point2D.Double pos = o.getPoint();
			g.drawOval((int) pos.getX() - 5, (int) pos.getY() - 5, 10, 10);
		}
		
	}
}
