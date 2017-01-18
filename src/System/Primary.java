package System;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import Other.Orbit;

public class Primary extends SystemElement{
	public Primary()
	{
		subelements = new String[]{"Station"};
		selectedColor = new Color(255, 153, 0, 85);
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
			g.fillOval((int) pos.getX() - 2, (int) pos.getY() - 2, 4, 4);
		}
		
	}
}
