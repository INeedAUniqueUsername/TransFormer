package System;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class SystemGroup extends SystemElement{
	public SystemGroup() {
		subelements = new String[]{"Orbitals"};
		pos_x = 0;
		pos_y = 0;
	}
	public void paint(Graphics g)
	{
		System.out.println("Painting SystemGroup");
		g.setColor(Color.WHITE);
		pos_x = (int) GraphicsPanel.center_x;
		pos_y = (int) GraphicsPanel.center_y;
		g.fillRect((int) pos_x-5, (int) pos_y-5, 10, 10);
		
		paintChildren(g);
	}
}
