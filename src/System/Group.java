package System;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import Other.Orbit;

public class Group extends SystemElement{
	public Group()
	{
		subelements = new String[]{"Primary", "Siblings", "Orbitals"};
	}
	public void paint(Graphics g, Orbit o)
	{
		paintChildren(g, o);
	}
}
