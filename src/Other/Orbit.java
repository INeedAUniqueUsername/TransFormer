package Other;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import System.SystemElement;

public class Orbit {
	Point2D.Double focus = new Point2D.Double(0, 0);
	double semimajor, angle, eccentricity, rotation = 0;
	SystemElement owner;

	public Orbit() {
	}

	public Orbit(Point2D.Double p, double s, double a, double e, double r) {
		focus = p;
		semimajor = s;
		angle = a;
		eccentricity = e;
		rotation = r;
	}

	public Point2D.Double getFocus() {
		return focus;
	}

	public double getSemiMajorAxis() {
		return semimajor;
	}

	public double getAngle() {
		return angle;
	}

	public double getEccentricity() {
		return eccentricity;
	}

	public double getRotation() {
		return rotation;
	}

	public Point2D.Double getOffset() {
		double radius = semimajor * (1 - SystemElement.square(eccentricity))
				/ (1 - eccentricity * SystemElement.cosDegrees(angle));
		return new Point2D.Double(SystemElement.cosDegrees(angle + rotation) * radius,
				SystemElement.sinDegrees(angle + rotation) * radius);
	}

	public Point2D.Double getPoint() {
		double radius = semimajor * (1 - SystemElement.square(eccentricity))
				/ (1 - eccentricity * SystemElement.cosDegrees(angle));
		return new Point2D.Double(focus.x + SystemElement.cosDegrees(angle + rotation) * radius,
				focus.y - SystemElement.sinDegrees(angle + rotation) * radius);
	}

	public void paintArc(Graphics g) {
		double semiminor = semimajor * Math.sqrt(1 - Math.pow(eccentricity, 2));
		double focus_to_center = Math.sqrt(Math.pow(semimajor, 2) - Math.pow(semiminor, 2)); // Distance
																								// from
																								// center
																								// to
																								// focus
		double focus_to_side = Math.pow(semimajor, 2) - focus_to_center;
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.rotate(Math.toRadians(-rotation), focus.x, focus.y);
		g2d.setColor(getColor());
		g2d.drawArc((int) (focus.x - focus_to_side), (int) (focus.y - semiminor), (int) (semimajor * 2),
				(int) (semiminor * 2), (int) angle, 360);
		g2d.dispose();
	}

	public Color getColor() {
		return owner.getSelected() ? new Color(255, 255, 0, 85) : new Color(255, 255, 255, 85);
	}
}
