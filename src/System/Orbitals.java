package System;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Orbitals extends SystemElement {
	/*
	 * String attribute_count = ""; String attribute_distance = ""; String
	 * attribute_scale = ""; //light-second by default String
	 * attribute_eccentricity = ""; String attribute_angle = "";
	 */

	final static int RENDER_EXTREMES = 0;
	final static int RENDER_DISTANCES = 1;
	final static int RENDER_DOTS = 2;
	final static int RENDER_RANDOM = 3;
	static int renderOption = RENDER_EXTREMES;
	final static String[] renderOptionNames = new String[] { "Render extremes", "Render distances", "Render dots",
			"Render random" };

	public Orbitals() {
		subelements = new String[] { "Station", "Group" };
		attributeKeys = new String[] { "count", "angle", "distance", "eccentricity", "rotation", "scale" };
	}

	/*
	 * public void initializeAttributes() { attribute_count =
	 * JOptionPane.showInputDialog("Count"); attribute_distance =
	 * JOptionPane.showInputDialog("Distance"); attribute_eccentricity =
	 * JOptionPane.showInputDialog("Eccentricity"); attribute_angle =
	 * JOptionPane.showInputDialog("Angle"); }
	 */
	public void paint(Graphics g, double pos_parent_x, double pos_parent_y) {
		print("Painting Orbital");
		String count_attribute = getAttribute("count");
		if (!StringIsNotEmptyOrNull(count_attribute)) {
			count_attribute = "1";
		}
		String angle_attribute = getAttribute("angle");
		if (!StringIsNotEmptyOrNull(angle_attribute)) {
			angle_attribute = "0";
		}
		String distance_attribute = getAttribute("distance");
		if (!StringIsNotEmptyOrNull(distance_attribute)) {
			distance_attribute = "0";
		}
		String eccentricity_attribute = getAttribute("eccentricity");
		if (!StringIsNotEmptyOrNull(eccentricity_attribute)) {
			eccentricity_attribute = "0";
		}
		String rotation_attribute = getAttribute("rotation");
		if (!StringIsNotEmptyOrNull(rotation_attribute)) {
			rotation_attribute = "0";
		}
		String scale_attribute = getAttribute("scale");
		double scale = (StringIsNotEmptyOrNull(scale_attribute) && scale_attribute.equals("light-minute"))
				? PIXELS_PER_LIGHT_SECOND * 6 : PIXELS_PER_LIGHT_SECOND;

		g.setColor(getColor());

		switch (renderOption) {
		case RENDER_EXTREMES:
			print("Orbitals: Render Extremes");
			ArrayList<Integer> angleRange = diceRangeOrbital(angle_attribute, DICERANGE_EXTREME);
			int angleMin = angleRange.get(0);
			int angleMax = angleRange.get(1);
			int angleArc = angleMax - angleMin;
			
			
			ArrayList<Integer> distanceRange = diceRangeOrbital(distance_attribute, DICERANGE_EXTREME);
			int distanceMin = (int) (scale * distanceRange.get(0));
			int distanceMax = (int) (scale * distanceRange.get(1));
			
			ArrayList<Integer> eccentricityRange = diceRangeOrbital(eccentricity_attribute, DICERANGE_EXTREME);
			int eccentricityMin = eccentricityRange.get(0);
			int eccentricityMax = eccentricityRange.get(1);
			
			ArrayList<Integer> rotationRange = diceRangeOrbital(rotation_attribute, DICERANGE_EXTREME);
			int rotationMin = rotationRange.get(0);
			int rotationMax = rotationRange.get(1);
			
			drawEllipseArc(g, pos_parent_x, pos_parent_y, angleMin, angleArc, distanceMin, eccentricityMin, rotationMin, scale);
			drawEllipseArc(g, pos_parent_x, pos_parent_y, angleMin, angleArc, distanceMin, eccentricityMax, rotationMin, scale);
			
			drawEllipseArc(g, pos_parent_x, pos_parent_y, angleMin, angleArc, distanceMax, eccentricityMin, rotationMax, scale);
			drawEllipseArc(g, pos_parent_x, pos_parent_y, angleMin, angleArc, distanceMax, eccentricityMax, rotationMax, scale);
			/*
			//Deprecated
			int drawRadiusMin = distanceMin;
			int drawRadiusMax = distanceMax;
			int drawDiameterMin = drawRadiusMin * 2;
			int drawDiameterMax = drawRadiusMax * 2;
			g.drawArc((int) (pos_parent_x - drawRadiusMin), (int) (pos_parent_y - drawRadiusMin), drawDiameterMin,
					drawDiameterMin, angleMin, angleArc);
			g.drawArc((int) (pos_parent_x - drawRadiusMax), (int) (pos_parent_y - drawRadiusMax), drawDiameterMax,
					drawDiameterMax, angleMin, angleArc);
			*/
			
			break;
		/*
		case RENDER_DISTANCES:
			print("Orbitals: Render Distances");
			angleRange = diceRangeOrbital(angle_attribute, DICERANGE_EXTREME);
			angleMin = angleRange.get(0);
			angleMax = angleRange.get(1);
			angleArc = angleMax - angleMin;

			ArrayList<Integer> distances = diceRangeOrbital(distance_attribute, DICERANGE_DISTRIBUTED);
			for (int distance : distances) {
				int drawRadius = (int) (distance * scale);
				int drawDiameter = drawRadius * 2;
				g.drawArc((int) (pos_parent_x - drawRadius), (int) (pos_parent_y - drawRadius), drawDiameter,
						drawDiameter, angleMin, angleArc);
			}
			break;

		//Abandoned
		case RENDER_DOTS:
			print("Orbitals: Render Dots");
			ArrayList<Integer> angle_list = diceRangeOrbital(angle_attribute, DICERANGE_DISTRIBUTED);
			ArrayList<Integer> distance_list = diceRangeOrbital(distance_attribute, DICERANGE_DISTRIBUTED);
			for (int angle : angle_list) {
				for (int distance : distance_list) {
					int distance_ls = (int) (distance * scale);
					g.fillOval((int) (pos_parent_x + distance_ls * cosDegrees(angle)),
							(int) (pos_parent_y - distance_ls * sinDegrees(angle)), 2, 2);
				}
			}
			break;
		*/
		case RENDER_RANDOM:
			print("Orbitals: Render Random");
			
			int count = diceRangeToRoll(count_attribute);
			
			if (angle_attribute.equals("equidistant")) {
				int angle_interval = 360 / count;
				int angle_offset = new Random().nextInt(angle_interval);
				for (int i = 0; i < count; i++) {
					int angle = i * angle_interval;
					drawEllipsePoint(g, pos_parent_x, pos_parent_y, angle, distance_attribute, eccentricity_attribute, rotation_attribute, scale);
				}
			} else if(angle_attribute.equals("random")) {
				for(int i = 0; i < count; i++)
				{
					int angle = new Random().nextInt(360);
					drawEllipsePoint(g, pos_parent_x, pos_parent_y, angle, distance_attribute, eccentricity_attribute, rotation_attribute, scale);
				}
			}
			//Distance can be specified by subelements. Umm...............
			else if(angle_attribute.contains("incrementing")) {
				angle_attribute = angle_attribute.split(";")[1];
				int angle = 0;
				for(int i = 0; i < count; i ++)
				{
					angle += diceRangeToRoll(angle_attribute);
					drawEllipsePoint(g, pos_parent_x, pos_parent_y, angle, distance_attribute, eccentricity_attribute, rotation_attribute, scale);
					
				}
			}
			//Not sure how to handle this right now
			else if(angle_attribute.contains("minSeparation")) {
				double angle = 0;
			}
			else {
				for(int i = 0; i < count; i++)
				{
					drawEllipsePoint(g, pos_parent_x, pos_parent_y, angle_attribute, distance_attribute, eccentricity_attribute, rotation_attribute, scale);
				}
			}
			break;
		}
	}
	
	/*
	public void drawEllipse(Graphics g, double parent_x, double parent_y, int angle, int distance, int eccentricity, int rotation, double scale)
	{
		print("Draw Ellipse");
		eccentricity /= 100;
		double semiminor = distance * Math.sqrt(1 - Math.pow(eccentricity, 2));
		double focus_to_center = Math.sqrt(Math.pow(distance, 2) - Math.pow(semiminor, 2)); //Distance from center to focus
		double focus_to_side = Math.pow(distance, 2) - focus_to_center;
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.rotate(Math.toRadians(-rotation), parent_x, parent_y);
		g2d.setColor(getColor());
		g2d.drawOval((int) (parent_x - focus_to_side), (int) (parent_y - semiminor), (int) (distance * 2), (int) (semiminor * 2));
		g2d.dispose();
	}
	*/
	public void drawEllipseArc(Graphics g, double parent_x, double parent_y, int angle, int arc, int distance, int eccentricity, int rotation, double scale)
	{
		eccentricity /= 100;
		double semiminor = distance * Math.sqrt(1 - Math.pow(eccentricity, 2));
		double focus_to_center = Math.sqrt(Math.pow(distance, 2) - Math.pow(semiminor, 2)); //Distance from center to focus
		double focus_to_side = Math.pow(distance, 2) - focus_to_center;
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.rotate(Math.toRadians(-rotation), parent_x, parent_y);
		g2d.setColor(getColor());
		g2d.drawArc((int) (parent_x - focus_to_side), (int) (parent_y - semiminor), (int) (distance * 2), (int) (semiminor * 2), angle, arc);
		g2d.dispose();
		
		print("Draw Ellipse");
	}
	
	//Draws a point at a location defined by specific parameters.
	public void drawEllipsePoint(Graphics g, double parent_x, double parent_y, int angle, int distance, int eccentricity, int rotation, double scale)
	{
		print("Draw Ellipse Point");
		g.setColor(getColor());
		HashMap<String, Double> offset = getOffset(distance, eccentricity, rotation, angle);
		parent_x += offset.get("x") * scale;
		parent_y -= offset.get("y") * scale;
		g.fillOval((int) parent_x, (int) parent_y, 5, 5);
		paintChildren(g, parent_x, parent_y);
	}
	
	//Similar to above, but accepts dice ranges for the angle, distance, eccentricity, and rotation
	public void drawEllipsePoint(Graphics g, double parent_x, double parent_y, String angle_attribute, String distance_attribute, String eccentricity_attribute, String rotation_attribute, double scale)
	{
		int angle = diceRangeToRoll(angle_attribute);
		int distance = diceRangeToRoll(distance_attribute);
		int eccentricity = diceRangeToRoll(eccentricity_attribute);
		int rotation = diceRangeToRoll(rotation_attribute);
		drawEllipsePoint(g, parent_x, parent_y, angle, distance, eccentricity, rotation, scale);
	}
	
	//Similar to above, but takes a constant for the angle
	public void drawEllipsePoint(Graphics g, double parent_x, double parent_y, int angle, String distance_attribute, String eccentricity_attribute, String rotation_attribute, double scale)
	{
		int distance = diceRangeToRoll(distance_attribute);
		int eccentricity = diceRangeToRoll(eccentricity_attribute);
		int rotation = diceRangeToRoll(rotation_attribute);
		drawEllipsePoint(g, parent_x, parent_y, angle, distance, eccentricity, rotation, scale);
	}
	
	//Copied from https://github.com/kronosaur/Mammoth/blob/4a4cd34841761dda14a3cc33b46871b59e45c956/TSE/COrbit.cpp
	public HashMap<String, Double> getOffset(double semimajor, double eccentricity, double rotation, double angle) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		eccentricity /= 100;
		double radius = semimajor * (1 - square(eccentricity)) / (1 - eccentricity * cosDegrees(angle));
		result.put("x", cosDegrees(angle + rotation) * radius);
		result.put("y", sinDegrees(angle + rotation) * radius);
		return result;
	}

	public static void setRenderOption(int option) {
		renderOption = option;
	}

	public static void setRenderOption(String option) {
		for (int i = 0; i < renderOptionNames.length - 1; i++) {
			if (option.equals(renderOptionNames[i])) {
				renderOption = i;
				break;
			}
		}
	}

	public static void cycleRenderOption() {
		renderOption += 1;
		if (renderOption > RENDER_DOTS) // CHANGE THIS TO RENDER_RANDOM
		{
			renderOption = 0;
		}
	}

	public static String getRenderOptionName() {
		return renderOptionNames[renderOption];
	}
	/*
	public boolean clickedOn(int clicked_x, int clicked_y) {
		double diff_x = clicked_x - pos_x;
		double diff_y = clicked_y - pos_y;

		double distance_clicked = Math.sqrt(Math.pow(diff_x, 2) + Math.pow(diff_y, 2)) / PIXELS_PER_LIGHT_SECOND;
		int distance_buffer = 1;
		ArrayList<Integer> distance_range = diceRangeOrbital(getAttribute("distance"), DICERANGE_EXTREME);
		int distance_min = distance_range.get(0) - distance_buffer;
		int distance_max = distance_range.get(1) + distance_buffer;

		double angle_clicked = Math.toDegrees(Math.atan2(clicked_y, clicked_x));
		ArrayList<Integer> angle_range = diceRangeOrbital(getAttribute("angle"), DICERANGE_EXTREME);
		int angle_buffer = 10;
		int angle_min = angle_range.get(0) - angle_buffer;
		int angle_max = angle_range.get(1) + angle_buffer;

		print("Distance Clicked: " + distance_clicked);
		print("Distance Minimum: " + distance_min);
		print("Distance Maximum: " + distance_max);
		print("Angle Clicked: " + angle_clicked);
		print("Angle Minimum: " + angle_min);
		print("Angle Maximum: " + angle_max);

		return ((distance_min < distance_clicked) && (distance_clicked < distance_max) && (angle_min < angle_clicked)
				&& (angle_clicked < angle_max));
	}
 	*/

	public Color getColor()
	{
		return selected ? new Color(255, 255, 0, 85) : new Color(255, 255, 255, 85);
	}
	public ArrayList<Integer> diceRangeOrbital(String input, int option) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		switch (input.split(":")[0]) {
		case "equidistant":
		case "random":
		case "incrementing":
		case "minSeparation":
			switch (option) {
			case DICERANGE_EXTREME:
				result.add(0);
				result.add(360);
				break;
			case DICERANGE_DISTRIBUTED:
				for (int i = 0; i <= 359; i++) {
					result.add(i);
				}
				break;
			}
			break;
		default:
			result = diceRange(input, option);
			break;
		}
		printVariable("Orbital Range", input);
		printVariable("Option", option);
		printVariable("Result", result);
		return result;
	}

	/*
	 * public String attributesToXML() { String result = "";
	 * if(StringIsNotEmptyOrNull(attribute_count)) { result +=
	 * attributeToXML("count", attribute_count); }
	 * if(StringIsNotEmptyOrNull(attribute_distance)) { result +=
	 * attributeToXML("distance", attribute_distance); }
	 * if(StringIsNotEmptyOrNull(attribute_eccentricity)) { result +=
	 * attributeToXML("eccentricity", attribute_eccentricity); }
	 * if(StringIsNotEmptyOrNull(attribute_angle)) { result +=
	 * attributeToXML("angle", attribute_angle); } return result; }
	 */
}
