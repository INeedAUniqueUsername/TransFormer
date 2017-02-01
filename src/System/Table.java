package System;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import Other.Orbit;

public class Table extends SystemElement {
	public void paint(Graphics g, Orbit o)
	{
		System.out.println("Painting Table");
		int result = (int) Math.random()*100 + 1;
		for(SystemElement se : children)
		{
			if(result - Integer.parseInt(se.getAttribute("chance")) < 0)
			{
				se.paint(g, o);
				break;
			}
		}
	}
}
