package System;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Orbitals extends SystemElement{
	/*
	String attribute_count =  "";
	String attribute_distance = "";
	String attribute_scale = ""; //light-second by default
	String attribute_eccentricity = "";
	String attribute_angle = "";
	*/
	static String renderOption = "Render extremes";
	public Orbitals() {
		subelements = new String[]{"Station", "Group"};
		attributeKeys = new String[]{"count", "distance", "eccentricity", "angle"};
	}
	/*
	public void initializeAttributes()
	{
		attribute_count = JOptionPane.showInputDialog("Count");
		attribute_distance = JOptionPane.showInputDialog("Distance");
		attribute_eccentricity = JOptionPane.showInputDialog("Eccentricity");
		attribute_angle = JOptionPane.showInputDialog("Angle");
	}
	*/
	public void paint(Graphics g) {
		System.out.println("Painting Orbital");
		double pos_parent_x = parent.getPosX();
		double pos_parent_y = parent.getPosY();
		
		ArrayList<Integer> angleRange = diceRangeToMinMax(getAttribute("angle"));
		int angleMin = angleRange.get(0);
		int angleMax = angleRange.get(1);
		int angleArc = angleMax - angleMin;
		
		switch(renderOption)
		{
		case "Render extremes":
			ArrayList<Integer> distanceRange = diceRangeToMinMax(getAttribute("distance"));
			int distanceMin = distanceRange.get(0);
			int distanceMax = distanceRange.get(1);
			int drawRadiusMin = distanceMin*PIXELS_PER_LIGHT_SECOND;
			int drawRadiusMax = distanceMax*PIXELS_PER_LIGHT_SECOND;
			int drawDiameterMin = drawRadiusMin*2;
			int drawDiameterMax = drawRadiusMax*2;
			g.setColor(new Color(255, 255, 255, 102));
			g.drawArc((int) (pos_parent_x - drawRadiusMin), (int) (pos_parent_y - drawRadiusMin), drawDiameterMin, drawDiameterMin, angleMin, angleArc);
			g.drawArc((int) (pos_parent_x - drawRadiusMax), (int) (pos_parent_y - drawRadiusMax), drawDiameterMax, drawDiameterMax, angleMin, angleArc);
			
			break;
			
		case "Render distances":
			ArrayList<Integer> distances = diceRangeToNumberList(getAttribute("distance"));
			for(int distance : distances)
			{
				int drawRadius = distance*PIXELS_PER_LIGHT_SECOND;
				int drawDiameter = drawRadius*2;
				g.setColor(new Color(255, 255, 255, 102));
				g.drawArc((int) (pos_parent_x - drawRadius), (int) (pos_parent_y - drawRadius), drawDiameter, drawDiameter, angleMin, angleArc);
			}
			
			break;
			
		case "Render dots":
			
			ArrayList<Integer> angle_list = diceRangeToNumberList(getAttribute("angle"));
			ArrayList<Integer> distance_list = diceRangeToNumberList(getAttribute("distance"));
			for(int angle : angle_list)
			{
				for(int distance : distance_list)
				{
					int distance_ls = distance * PIXELS_PER_LIGHT_SECOND;
					g.setColor(new Color(255, 255, 255, 101));
					g.fillOval((int) (pos_parent_x + distance_ls * cosDegrees(angle)), (int) (pos_parent_y + distance_ls * sinDegrees(angle)), 2, 2);
				}
			}
			
			break;
		}
		paintChildren(g);
	}
	public static void setRenderOption(String option)
	{
		renderOption = option;
	}
	
	/*
	public String attributesToXML()
	{
		String result = "";
		if(StringIsNotEmptyOrNull(attribute_count)) {
			result += attributeToXML("count", attribute_count);
		}
		if(StringIsNotEmptyOrNull(attribute_distance)) {
			result += attributeToXML("distance", attribute_distance);
		}
		if(StringIsNotEmptyOrNull(attribute_eccentricity)) {
			result += attributeToXML("eccentricity", attribute_eccentricity);
		}
		if(StringIsNotEmptyOrNull(attribute_angle)) {
			result += attributeToXML("angle", attribute_angle);
		}
		return result;
	}
	*/
}
