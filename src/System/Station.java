package System;
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
}
