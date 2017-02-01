package System;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Other.Orbit;

public class Orbitals extends SystemElement {
	final String ATTRIB_COUNT = "count";
	final String ATTRIB_ANGLE = "angle";
	final String ATTRIB_DISTANCE = "distance";
	final String ATTRIB_ECCENTRICITY = "eccentricity";
	final String ATTRIB_ROTATION = "rotation";
	final String ATTRIB_SCALE = "scale";
	final String ATTRIB_BODE_DISTANCE_START = "bodeDistanceStart";
	final String ATTRIB_BODE_DISTANCE_END = "bodeDistanceEnd";
	final String ATTRIB_NO_OVERLAP = "noOverlap";
	final String ATTRIB_EXCLUSION_RADIUS = "exclusionRadius";
	
	/*
	 * String attribute_count = ""; String attribute_distance = ""; String
	 * attribute_scale = ""; //light-second by default String
	 * attribute_eccentricity = ""; String attribute_angle = "";
	 */

	/*
	final static int RENDER_EXTREMES = 0;
	final static int RENDER_DISTANCES = 1;
	final static int RENDER_DOTS = 2;
	final static int RENDER_RANDOM = 3;
	static int renderOption = RENDER_EXTREMES;
	final static String[] renderOptionNames = new String[] { "Render extremes", "Render distances", "Render dots",
			"Render random" };
	*/
	public Orbitals() {
		subelements = new String[] { "Station", "Group" };
		attributeKeys = new ArrayList<String>(Arrays.asList(new String[] { ATTRIB_COUNT, ATTRIB_ANGLE, ATTRIB_DISTANCE, ATTRIB_ECCENTRICITY, ATTRIB_ROTATION, ATTRIB_SCALE, ATTRIB_BODE_DISTANCE_START, ATTRIB_BODE_DISTANCE_END, ATTRIB_NO_OVERLAP, ATTRIB_EXCLUSION_RADIUS}));
		selectedColor = new Color(102, 102, 255, 85);
	}

	/*
	 * public void initializeAttributes() { attribute_count =
	 * JOptionPane.showInputDialog("Count"); attribute_distance =
	 * JOptionPane.showInputDialog("Distance"); attribute_eccentricity =
	 * JOptionPane.showInputDialog("Eccentricity"); attribute_angle =
	 * JOptionPane.showInputDialog("Angle"); }
	 */
	public void paint(Graphics g, Orbit o) {
		System.out.println("Painting <Orbitals>");
		
		Point2D.Double pos = o.getPoint();
		double pos_x = pos.getX();
		double pos_y = pos.getY();
		
		ArrayList<SystemElement> children = getChildren();
		int childrenCount = children.size();
		boolean hasVisibleChildren = hasVisibleChildren();
		
		String count_attribute = getAttribute("count");
		String angle_attribute = getAttribute("angle");
		if (isBlank(angle_attribute)) {
			angle_attribute = "0";
		}
		String distance_attribute = getAttribute("distance");
		/*
		if (!StringIsNotEmptyOrNull(distance_attribute)) {
			distance_attribute = "0";
		}
		*/
		String eccentricity_attribute = getAttribute("eccentricity");
		if (isBlank(eccentricity_attribute)) {
			eccentricity_attribute = "0";
		}
		String rotation_attribute = getAttribute("rotation");
		if (isBlank(rotation_attribute)) {
			rotation_attribute = "0";
		}
		
		double scale = getScale();

		String bodeDistanceStart_attribute = getAttribute("bodeDistanceStart");
		String bodeDistanceEnd_attribute = getAttribute("bodeDistanceEnd");
		String noOverlap_attribute = getAttribute("noOverlay");
		String exclusionRadius_attribute = getAttribute("exclusionRadius");
		boolean noOverlap = noOverlap_attribute == "true" ? true : false;
		int exclusionRadius = isBlank(exclusionRadius_attribute) ? 0 : Integer.valueOf(exclusionRadius_attribute);
		
		g.setColor(getColor());
		
		System.out.println("Orbitals: Render");
		
		int count = isBlank(count_attribute) ? 1 : roll(count_attribute);
		if(childrenCount > 1)
		{
			count = count > childrenCount ? count : childrenCount;
		}
		
		double[] distances = new double[count];
		if(!isBlank(distance_attribute))
		{
			for(int i = 0; i < count; i++)
			{
				distances[i] = scale * roll(distance_attribute);
			}
		}
		
		//Bode Distance
		
		else
		{
			for(int i = 0; i < count; i++)
			{
				SystemElement child = children.get(i % childrenCount);
				double distance_child = roll(child.getAttribute("distance"));
				String scale_attribute_child = child.getAttribute("scale");
				double scale_child = (!isBlank(scale_attribute_child) && scale_attribute_child.equals("light-minute"))
						? LIGHT_MINUTE : LIGHT_SECOND;
				distances[i] = scale_child * distance_child;
			}
		}
		
		boolean configurationOK = true;
		int[] angles = new int[count];
		if(angle_attribute.equals("random"))
		{
			for(int i = 0; i < count; i++)
			{
				boolean angleOK = true;
				int tries = 20;
				/*
				do
				{
					angles[i] = (int) (Math.random() * 360);
					angleOK = true;
					if(exclusionRadius > 0)
					{
						
					}
				}
				*/
			}
		} else {
			int tries = 10;
			do {
				angles = generateAngles(angle_attribute, count);
				
				//INCOMPLETE
				/*
				if(exclusionRadius > 0)
				{
					configurationOK = true;
				}
				else if(noOverlap)
				{
					configurationOK = true;
				}
				*/
			}
			while(!configurationOK && --tries > 0);
		}
		if(!configurationOK)
		{
			if(exclusionRadius > 0)
				System.out.println("<Orbitals>: Unable to find clear exclusion zone: " + exclusionRadius + " ls radius.");
			else if(noOverlap)
				System.out.println("<Orbitals>: Unable to find non-overlapping configuration.");
			else
				System.out.println("<Orbitals>: Unable to find valid configuration.");
		}
		
		double[] eccentricities = new double[count];
		for(int i = 0; i < count; i++)
		{
			double e = roll(eccentricity_attribute) / 100;
			if(e > 0.99)
			{
				e = 0.99;
			}
			eccentricities[i] = e;
		}
		
		int[] rotations = new int[count];
		for(int i = 0; i < count; i++){
			rotations[i] = roll(rotation_attribute);
		}
		
		
		int obj = 0;
		int entry = 0;
		int loops = count > childrenCount ? count : childrenCount;
		if(hasVisibleChildren)
		{
			for(int i = 0; i < loops; i++)
			{
				SystemElement child = children.get(obj);
				Orbit o2 = new Orbit(pos, distances[entry], angles[entry], eccentricities[entry], rotations[entry]);
				if(child.getVisible())
				{
					child.paint(g, o2);
				}
				obj = (obj + 1) % childrenCount;
				entry = (entry + 1) % count;
			}
		} else {
			for(int i = 0; i < loops; i++)
			{
				System.out.println("X: " + pos_x);
				System.out.println("Y: " + pos_y);
				System.out.println("Angle: " + angles[entry]);
				System.out.println("Distances: " + distances[entry]);
				System.out.println("Eccentricity: " + eccentricities[entry]);
				System.out.println("Rotations: " + rotations[entry]);
				System.out.println("Scale: " + scale);
				drawEllipsePoint(g, pos_x, pos_y, angles[entry], distances[entry], eccentricities[entry], rotations[entry], scale);
				entry = (entry + 1) % count;
			}
		}
		/*
		switch (renderOption) {
		/*
		case RENDER_EXTREMES:
			System.out.println("Orbitals: Render Extremes");
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
			*//*
			
			break;
		/*
		case RENDER_DISTANCES:
			System.out.println("Orbitals: Render Distances");
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
			System.out.println("Orbitals: Render Dots");
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
		*//*
		case RENDER_RANDOM:
			break;
		}
		*/
		/*
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
		*/
	}
	
	/*
	public void drawEllipse(Graphics g, double parent_x, double parent_y, int angle, int distance, int eccentricity, int rotation, double scale)
	{
		System.out.println("Draw Ellipse");
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
	/*
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
		
		System.out.println("Draw Ellipse");
	}
	*/
	/*
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
	*/
	//Draws a point at a location defined by specific parameters.
	public void drawEllipsePoint(Graphics g, double parent_x, double parent_y, double angle, double distance, double eccentricity, double rotation, double scale)
	{
		System.out.println("Draw Ellipse Point");
		g.setColor(getColor());
		Point2D.Double offset = getOffset(distance, eccentricity, rotation, angle);
		parent_x += offset.x * scale;
		parent_y -= offset.y * scale;
		g.fillOval((int) parent_x - 3, (int) parent_y - 3, 6, 6);
		//paintChildren(g, parent_x, parent_y);
	}
	public Point2D.Double getOffset(double semimajor, double eccentricity, double rotation, double angle) {
		double radius = semimajor * (1 - square(eccentricity)) / (1 - eccentricity * cosDegrees(angle));
		return new Point2D.Double(cosDegrees(angle + rotation) * radius, sinDegrees(angle + rotation) * radius);
	}
	/*
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
	*/
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

		System.out.println("Distance Clicked: " + distance_clicked);
		System.out.println("Distance Minimum: " + distance_min);
		System.out.println("Distance Maximum: " + distance_max);
		System.out.println("Angle Clicked: " + angle_clicked);
		System.out.println("Angle Minimum: " + angle_min);
		System.out.println("Angle Maximum: " + angle_max);

		return ((distance_min < distance_clicked) && (distance_clicked < distance_max) && (angle_min < angle_clicked)
				&& (angle_clicked < angle_max));
	}
 	*/
	/*
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
	*/

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
