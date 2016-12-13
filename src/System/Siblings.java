package System;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Siblings extends SystemElement{
	String attribute_angleInc;
	String attribute_radiusInc;
	String attribute_count;
	String attribute_distribution;
	public Siblings() {
		subelements = new String[]{"Station", "Lookup"};
	}
}
