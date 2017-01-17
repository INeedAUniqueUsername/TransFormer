package System;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import Other.Orbit;

public class SystemGroup extends SystemElement{
	public SystemGroup() {
		subelements = new String[]{"Orbitals"};
	}
	public void paint(Graphics g, Orbit o)
	{
		print("Painting SystemGroup");
		Point2D pos = o.getPoint();
		double pos_x = pos.getX();
		double pos_y = pos.getY();
		System.out.println("Orbit: " + o.toString());
		System.out.println("(" + pos_x + ", " + pos_y + ")");
		g.setColor(Color.WHITE);
		g.fillOval((int) pos_x-2, (int) pos_y-2, 4, 4);
		paintChildren(g, o);
	}
}
