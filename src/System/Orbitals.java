package System;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Orbitals extends SystemElement{
	/*
	String attribute_count =  "";
	String attribute_distance = "";
	String attribute_scale = ""; //light-second by default
	String attribute_eccentricity = "";
	String attribute_angle = "";
	*/
	
	final static int RENDER_EXTREMES = 0;
	final static int RENDER_DISTANCES = 1;
	final static int RENDER_DOTS = 2;
	final static int RENDER_RANDOM = 3;
	static int renderOption = RENDER_EXTREMES;
	final static String[] renderOptionNames = new String[]{"Render extremes", "Render distances", "Render dots", "Render random"};
	
	public Orbitals() {
		subelements = new String[]{"Station", "Group"};
		attributeKeys = new String[]{"count", "angle", "distance", "eccentricity", "rotation"};
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
		
		String angle_attribute = getAttribute("angle");
		String distance_attribute = getAttribute("distance");
		String count_attribute = getAttribute("count");
		String eccentricity_attribute = getAttribute("eccentricity");
		String rotation_attribute = getAttribute("rotation");
		
		switch(renderOption)
		{
		case RENDER_EXTREMES:
			ArrayList<Integer> angleRange = diceRangeOrbital(angle_attribute, DICERANGE_EXTREME);
			int angleMin = angleRange.get(0);
			int angleMax = angleRange.get(1);
			int angleArc = angleMax - angleMin;
			
			ArrayList<Integer> distanceRange = diceRangeOrbital(distance_attribute, DICERANGE_EXTREME);
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
			
		case RENDER_DISTANCES:
			angleRange = diceRangeOrbital(angle_attribute, DICERANGE_EXTREME);
			angleMin = angleRange.get(0);
			angleMax = angleRange.get(1);
			angleArc = angleMax - angleMin;
			
			ArrayList<Integer> distances = diceRangeOrbital(distance_attribute, DICERANGE_DISTRIBUTED);
			for(int distance : distances)
			{
				int drawRadius = distance*PIXELS_PER_LIGHT_SECOND;
				int drawDiameter = drawRadius*2;
				g.setColor(new Color(255, 255, 255, 102));
				g.drawArc((int) (pos_parent_x - drawRadius), (int) (pos_parent_y - drawRadius), drawDiameter, drawDiameter, angleMin, angleArc);
			}
			break;
			
		case RENDER_DOTS:
			ArrayList<Integer> angle_list = diceRangeOrbital(angle_attribute, DICERANGE_DISTRIBUTED);
			ArrayList<Integer> distance_list = diceRangeOrbital(distance_attribute, DICERANGE_DISTRIBUTED);
			for(int angle : angle_list)
			{
				for(int distance : distance_list)
				{
					int distance_ls = distance * PIXELS_PER_LIGHT_SECOND;
					g.setColor(new Color(255, 255, 255, 102));
					g.fillOval((int) (pos_parent_x + distance_ls * cosDegrees(angle)), (int) (pos_parent_y - distance_ls * sinDegrees(angle)), 2, 2);
				}
			}
			break;
			
		case RENDER_RANDOM:
			int count = diceRangeToRoll(count_attribute);
			if(angle_attribute.equals("equidistant"))
			{
				int angle_interval = 360 / count;
				int angle_offset = new Random().nextInt(angle_interval);
				for(int i = 0; i < count; i++)
				{
					int distance = diceRangeToRoll(distance_attribute);
					int eccentricity = diceRangeToRoll(eccentricity_attribute);
					int rotation = diceRangeToRoll(rotation_attribute);
					int angle = i * angle_interval;
					pos_x = pos_parent_x; 
					pos_y = pos_parent_y;
					HashMap<String, Double> offset = getOffset(distance, eccentricity, rotation, angle);
					pos_x += offset.get("x");
					pos_y -= offset.get("y");
					paintChildren(g);
				}
			
			break;
			}
		}
	}
	
	public HashMap<String, Double> getOffset(int semimajor, int eccentricity, int rotation, int angle)
	{
		HashMap<String, Double> result = new HashMap<String, Double>();
		eccentricity /= 100;
		double radius = semimajor * (1 - square(eccentricity)) / (1 - eccentricity * cosDegrees(angle)); 
		result.put("x", cosDegrees(angle + rotation) * radius);
		result.put("y", sinDegrees(angle + rotation) * radius);
		return result;
	}
	
	public static void setRenderOption(int option)
	{
		renderOption = option;
	}
	public static void setRenderOption(String option)
	{
		for(int i = 0; i < renderOptionNames.length - 1; i++)
		{
			if(option.equals(renderOptionNames[i]))
			{
				renderOption = i;
				break;
			}
		}
	}
	public static void cycleRenderOption()
	{
		renderOption += 1;
		if(renderOption > RENDER_DOTS) //CHANGE THIS TO RENDER_RANDOM
		{
			renderOption = 0;
		}
	}
	public static String getRenderOptionName()
	{
		return renderOptionNames[renderOption];
	}
	
	public boolean clickedOn(int clicked_x, int clicked_y)
	{
		double diff_x = clicked_x - pos_x;
		double diff_y = clicked_y - pos_y;
		
		double distance_clicked = Math.sqrt(Math.pow(diff_x, 2) + Math.pow(diff_y, 2))/PIXELS_PER_LIGHT_SECOND;
		int distance_buffer = 1;
		ArrayList<Integer> distance_range = diceRangeOrbital(getAttribute("distance"), DICERANGE_EXTREME);
		int distance_min = distance_range.get(0) - distance_buffer;
		int distance_max = distance_range.get(1) + distance_buffer;
		
		double angle_clicked = Math.toDegrees(Math.atan2(clicked_y, clicked_x));
		ArrayList<Integer> angle_range = diceRangeOrbital(getAttribute("angle"), DICERANGE_EXTREME);
		int angle_buffer = 10;
		int angle_min = angle_range.get(0) - angle_buffer;
		int angle_max = angle_range.get(1) + angle_buffer;
		
		System.out.println("Distance Clicked: " + distance_clicked);
		System.out.println("Distance Minimum: " + distance_min);
		System.out.println("Distance Maximum: " + distance_max);
		System.out.println("Angle Clicked: " + angle_clicked);
		System.out.println("Angle Minimum: " + angle_min);
		System.out.println("Angle Maximum: " + angle_max);
		
		return ((distance_min < distance_clicked) && (distance_clicked < distance_max) && (angle_min < angle_clicked) && (angle_clicked < angle_max));
	}
	
	public ArrayList<Integer> diceRangeOrbital(String input, int option)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		switch(input.split(":")[0])
		{
		case "random":
		case "minSeparation":
		case "incrementing":
		case "equidistant":
			switch(option)
			{
			case DICERANGE_EXTREME:
				result.add(0);
				result.add(360);
				break;
			case DICERANGE_DISTRIBUTED:
				for(int i = 0; i <= 359; i++)
				{
					result.add(i);
				}
				break;
			}
			break;
		default:
			result = diceRange(input, option);
			break;
		}
		System.out.println("Orbital Range: " + input);
		System.out.println("Option: " + option);
		System.out.println("Result: " + result);
		return result;
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
