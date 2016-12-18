package System;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class SystemGroup extends SystemElement{
	public SystemGroup() {
		subelements = new String[]{"Orbitals"};
	}
	public void paint(Graphics g, double pos_parent_x, double pos_parent_y)
	{
		print("Painting SystemGroup");
		g.setColor(Color.WHITE);
		g.fillOval((int) pos_parent_x-2, (int) pos_parent_y-2, 4, 4);
		paintChildren(g, pos_parent_x, pos_parent_y);
	}
}
