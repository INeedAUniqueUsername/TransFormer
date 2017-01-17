package System;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import Other.Orbit;

public class Siblings extends SystemElement {
	final String ATTRIBUTE_COUNT = "count";
	final String ATTRIBUTE_DISTRIBUTION = "distribution";
	final String ATTRIBUTE_MIN_RADIUS = "minRadius";
	final String ATTRIBUTE_RADIUS_INC = "radiusInc";
	final String ATTRIBUTE_RADIUS_DEC = "radiusDec";
	final String ATTRIBUTE_ANGLE = "angle";
	final String ATTRIBUTE_ARC_INC = "arcInc";
	final String ATTRIBUTE_ANGLE_ADJ = "angleAdj";
	final String ATTRIBUTE_ANGLE_INC = "angleInc";

	public Siblings() {
		subelements = new String[] { TAG_STATION, TAG_LOOKUP };
		attributeKeys = new String[] { ATTRIBUTE_COUNT, ATTRIBUTE_DISTRIBUTION, ATTRIBUTE_MIN_RADIUS,
				ATTRIBUTE_RADIUS_INC, ATTRIBUTE_RADIUS_DEC, ATTRIBUTE_ANGLE, ATTRIBUTE_ARC_INC, ATTRIBUTE_ANGLE_ADJ,
				ATTRIBUTE_ANGLE_INC };
	}

	public void paint(Graphics g, Orbit o) {
		String count_attribute = getAttribute("count");
		int count = isBlank(count_attribute) ? 1 : roll(count_attribute);
		String distribution_attribute = getAttribute("distribution");
		double scale = getScale();
		if (!isBlank(distribution_attribute)) {
			for (int i = 0; i < count; i++) {
				double distribution = roll(distribution_attribute);
				Orbit orbit_sibling = new Orbit(o.getFocus(), o.getSemiMajorAxis() + scale * distribution, (Math.random() * 3600) / 10,
						o.getEccentricity(), o.getRotation());
				getChildren().get(0).paint(g, orbit_sibling);;
			}
		} else {
			String radiusAdj_range = "";
			double radiusAdjScale;
			String minRadius_attribute = getAttribute(ATTRIBUTE_MIN_RADIUS);
			String radiusInc_attribute = getAttribute(ATTRIBUTE_RADIUS_INC);
			String radiusDec_attribute = getAttribute(ATTRIBUTE_RADIUS_DEC);
			if(!isBlank(minRadius_attribute))
			{
				int minRadius = (int) (Integer.parseInt(minRadius_attribute) * scale);
				minRadius = minRadius > 0 ? minRadius : 0;
				double semimajor = o.getSemiMajorAxis();
				if(minRadius > semimajor)
				{
					radiusAdj_range = "" + Math.round((minRadius - semimajor) / scale);
					radiusAdjScale = scale;
				}
				else
				{
					radiusAdjScale = 0;
				}
			} else if(!isBlank(radiusInc_attribute)){
				radiusAdj_range = getAttribute(radiusInc_attribute);
				radiusAdjScale = -scale;
			}
			else
			{
				radiusAdjScale = 0;
			}
			//IncTypes declared below method
			IncTypes AngleInc;
			int[] angles = new int[count];
			
			String angle_attribute = getAttribute(ATTRIBUTE_ANGLE);
			String arcInc_attribute = getAttribute(ATTRIBUTE_ARC_INC);
			String angleAdj_attribute = getAttribute(ATTRIBUTE_ANGLE_ADJ);
			String angleInc_attribute = getAttribute(ATTRIBUTE_ANGLE_INC);
			if(!isBlank(angle_attribute))
			{
				AngleInc = IncTypes.incFixed;
				angles = generateAngles(angle_attribute, count);
			} else if(!isBlank(arcInc_attribute)){
				AngleInc = IncTypes.incAngle;
				angleAdj_attribute = arcInc_attribute;
			} else if(!isBlank(angleAdj_attribute)) {
				AngleInc = IncTypes.incAngle;
				//angleAdj_attribute = angleAdj_attribute;
			} else if(!isBlank(angleInc_attribute)) {
				AngleInc = IncTypes.incAngle;
				angleAdj_attribute = angleInc_attribute;
			} else {
				AngleInc = IncTypes.incNone;
			}
			Orbit[] orbits = new Orbit[count];
			for(int i = 0; i < count; i++)
			{
				double radiusAdj = radiusAdjScale * roll(radiusAdj_range);
				double angleAdj;
				switch(AngleInc)
				{
				case incArc:
					double circ = o.getSemiMajorAxis() + radiusAdj;
					angleAdj = circ > 0 ? (roll(angleAdj_attribute) * scale) / circ : 0;
					break;
				case incAngle:
					angleAdj = (360 + roll(angleAdj_attribute)) % 360;
					break;
				case incFixed:
					angleAdj = angles[i] - o.getAngle();
					break;
				default:
					angleAdj = 0;
				}
				orbits[i] = new Orbit(o.getFocus(), o.getSemiMajorAxis() + radiusAdj, o.getAngle() + angleAdj, o.getEccentricity(), o.getRotation());
			}
			ArrayList<SystemElement> children = getChildren();
			int childrenCount = children.size();
			int pos = 0;
			int obj = 0;
			int loops = count > childrenCount ? count : childrenCount;
			for(int i = 0; i < loops; i++)
			{
				children.get(obj).paint(g, orbits[pos]);
				obj = (obj + 1) % childrenCount;
				pos = (pos + 1) % count;
			}
		}
	}
	enum IncTypes {incNone, incAngle, incArc, incFixed};
}
