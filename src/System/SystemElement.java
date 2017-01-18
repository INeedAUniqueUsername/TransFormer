package System;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import Other.Orbit;

public class SystemElement {
	final String SCALE_AU = "AU";
	final String SCALE_LIGHT_MINUTE = "light-minute";
	final String SCALE_LIGHT_SECOND = "light-second";
	final String SCALE_PIXEL = "pixel";
	
	final String TAG_ADD_ATTRIBUTE = "AddAttribute";
	final String TAG_ADD_TERRITORY = "AddTerritory";
	final String TAG_ANTI_TROJAN = "AntiTrojan";
	final String TAG_CODE = "Code";
	final String TAG_ESCORTS = "Escorts";
	final String TAG_EVENTS = "Events";
	final String TAG_FILL_LOCATIONS = "FillLocations";
	final String TAG_FILL_RANDOM_LOCATION = "FillRandomLocation";
	final String TAG_GROUP = "Group";
	final String TAG_INITIAL_DATA = "InitialData";
	final String TAG_ITEM = "Item";
	final String TAG_ITEMS = "Items";
	final String TAG_LABEL = "Label";
	final String TAG_LEVELTABLE = "LevelTable";
	final String TAG_LOCATION_CRITERIA_TABLE = "LocationCriteriaTable";
	final String TAG_LOOKUP = "Lookup";
	final String TAG_MARKER = "Marker";
	final String TAG_NULL = "Null";
	final String TAG_OFFSET = "Offset";
	final String TAG_ON_CREATE = "OnCreate";
	final String TAG_ORBITAL_DISTRIBUTION = "OrbitalDistribution";
	final String TAG_ORBITALS = "Orbitals";
	final String TAG_PARTICLES = "Particles";
	final String TAG_PLACE_RANDOM_STATION = "PlaceRandomStation";
	final String TAG_PRIMARY = "Primary";
	final String TAG_RANDOM_LOCATION = "RandomLocation";
	final String TAG_RANDOM_STATION = "RandomStation";
	final String TAG_SATELLITES = "Satellites";
	final String TAG_SHIP = "Ship";
	final String TAG_SHIPS = "Ships";
	final String TAG_SIBLINGS = "Siblings";
	final String TAG_SPACE_ENVIRONMENT = "SpaceEnvironment";
	final String TAG_STARGATE = "Stargate";
	final String TAG_STATION = "Station";
	final String TAG_SYSTEM_GROUP = "SystemGroup";
	final String TAG_TABLE = "Table";
	final String TAG_TABLES = "Tables";
	final String TAG_TROJAN = "Trojan";
	final String TAG_VARIANT = "Variant";
	final String TAG_VARIANTS = "Variants";
	final String TAG_VARIANT_TABLE = "VariantTable";
	
	final String ATTRIBUTE_SCALE = "scale";
	
	private boolean selected = false;
	private boolean visible = true;

	private static int tabCount = 1;

	/*
	final int DICERANGE_EXTREME = 0;
	final int DICERANGE_DISTRIBUTED = 1;
	 */

	double LIGHT_SECOND = 1;
	double LIGHT_MINUTE = LIGHT_SECOND * 60;
	
	double PIXEL = LIGHT_SECOND / 23.9834;
	double AU = LIGHT_SECOND * 499.004784;
	ArrayList<SystemElement> children = new ArrayList<SystemElement>();
	SystemElement parent;

	String[] subelements = new String[0];

	HashMap<String, String> attributes = new HashMap<String, String>();
	String[] attributeKeys = new String[0];

	Color selectedColor = new Color(0, 0, 255, 85);
	Color defaultColor = new Color(255, 255, 255, 85);
	public SystemElement() {
	}
	
	public boolean getVisible()
	{
		return visible;
	}
	public void setVisible(boolean b)
	{
		visible = b;
	}
	public void paint(Graphics g, Orbit o) {
		System.out.println("Default Paint");
	}

	/*
	public void paintChildren(Graphics g) {
		for (SystemElement se : children) {
			se.paint(g);
		}
	}
	*/

	public void paintChildren(Graphics g, Orbit o) {
		for (SystemElement se : children) {
			if(se.visible)
			{
				se.paint(g, o);
			}
			else
			{
				System.out.println("Not Painting " + se.toString());
			}
		}
	}

	public boolean clickedOn(int clicked_x, int clicked_y) {
		System.out.println("Default Clicked");
		return false;
	}

	public final void addChild(SystemElement child) {
		children.add(child);
		child.setParent(this);
	}

	public final void removeChild(SystemElement child) {
		children.remove(child);
		child.setParent(null);
	}

	public final ArrayList<SystemElement> getChildren() {
		return children;
	}

	public final boolean hasVisibleChildren()
	{
		boolean result = false;
		for(SystemElement child : getChildren())
		{
			if(child.getVisible())
			{
				result = true;
				break;
			}
		}
		return result;
	}
	public final SystemElement getParent() {
		return parent;
	}

	public final void setParent(SystemElement newParent) {
		parent = newParent;
	}

	/*
	public final Orbit getOrbit()
	{
		return orbit;
	}
	public final void setOrbit(Orbit o)
	{
		orbit = o;
	}
	*/
	
	public final double getScale(){
		String scale_attribute = getAttribute(ATTRIBUTE_SCALE);
		if(isBlank(scale_attribute))
		{
			return LIGHT_SECOND;
		}
		else
		{
			switch(scale_attribute)
			{
			case "AU": return AU;
			case "light-minute": return LIGHT_MINUTE;
			case "light-second": return LIGHT_SECOND;
			case "pixel": return PIXEL;
			
			default: return LIGHT_SECOND;
			}
		}
	}
	
	
	public final int[] generateAngles(String angle_attribute, int count)
	{
		int[] angles = new int[count];
		if(angle_attribute.equals("random"))
		{
			for(int i = 0; i < count; i++)
			{
				angles[i] = (int) (Math.random() * 360);
			}
		}
		else if(angle_attribute.contains("minSeparation"))
		{
			String separation_range = angle_attribute.split(":")[1];
			int minSeparation = roll(separation_range);
			for(int i = 0; i < count; i++)
			{
				boolean angleOK;
				int tries_angle = 20;
				do
				{
					angles[i] = (int) Math.random() * 100;
					angleOK = true;
					for(int k = 0; k < i; k++)
					{
						if(Math.abs(angles[i] - angles[k]) < minSeparation
								|| angles[i] + 360 - angles[k] < minSeparation
								|| angles[k] + 360 - angles[i] < minSeparation
								)
						{
							angleOK = false;
							break;
						}
					}
				}
				while(tries_angle-- > 0 && angleOK);
			}
		}
		else if(angle_attribute.contains("equidistant"))
		{
			
			String offset_range = angle_attribute.contains(":") ? angle_attribute.split(":")[1] : "0";
			int start = (int) Math.random() * 3600;
			int separation = 3600 / count;
			for(int i = 0; i < count; i++)
			{
				angles[i] = roll(offset_range) + ((start + separation * i) % 3600) / 10;
			}
		}
		else if(angle_attribute.contains("incrementing"))
		{
			String inc_range = angle_attribute.split(":")[1];
			int angle = (int) Math.random() * 360;
			for(int i = 0; i < count; i++)
			{
				angles[i] = angle % 360;
				angle += roll(inc_range);
			}
		}
		else
		{
			for(int i = 0; i < count; i++)
			{
				angles[i] = roll(angle_attribute);
			}
		}
		return angles;
	}
	
	public final void initializeAttributes() {
		for (String attribute : attributeKeys) {
			attributes.put(attribute, JOptionPane.showInputDialog(attribute));
		}
	}
	/*
	 * public void click() { int choice_index =
	 * JOptionPane.showOptionDialog(null,
	 * "What would you like to create around this " +
	 * getClass().getName().substring(7) + "?", "Create New Element",
	 * JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
	 * subelements, null); if (choice_index != JOptionPane.CLOSED_OPTION) {
	 * String choice = subelements[choice_index]; SystemElement element = null;
	 * switch (choice) { //Cannot make a new <SystemGroup> //case "SystemGroup":
	 * //element = new SystemGroup(); //break; case "Group": element = new
	 * Group(); break; case "Primary": element = new Primary(); break; case
	 * "Siblings": element = new Siblings(); break; case "Orbitals": element =
	 * new Orbitals(); break; case "Station": element = new Station(); break;
	 * case "Table": // element = new Table(); break; case "Label": // element =
	 * new Label(); break;
	 * 
	 * } if (element != null) { element.initializeAttributes();
	 * addChild(element);
	 * 
	 * System.out.println(getXML()); } } }
	 */
	
	/*
	public final Point2D getPos()
	{
		return orbit.getPoint();
	}
	*/
	
	public final DefaultMutableTreeNode toTreeNode()
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for(SystemElement child : getChildren())
		{
			node.add(child.toTreeNode());
		}
		return node;
	}

	public final void setSelected(boolean selection) {
		selected = selection;
	}

	public final void setSelectedChildren(boolean selection) {
		for (SystemElement se : getChildren()) {
			se.setSelectedFamily(selection);
		}
	}

	public final void setSelectedFamily(boolean selection) {
		selected = selection;
		setSelectedChildren(selection);
	}
	
	public final boolean getSelected()
	{
		return selected;
	}

	/*
	public final double getPosX() {
		return pos_x;
	}

	public final double getPosY() {
		return pos_y;
	}
	*/

	public final String toString() {
		String name = getClass().getName();
		int index = name.indexOf('.');
		if(index > -1)
		{
			name = name.substring(index+1);
		}
		return name;
	}

	public final String getXML() {
		String name = toString();
		String tabs = "";
		System.out.println("Tab Count: " + tabCount);
		for (int i = 0; i < tabCount; i++) {
			tabs += "\t";
		}

		String tag_open_start = tabs + "<";
		String tag_open_middle = name;
		tag_open_middle += attributesToXML();
		String tag_open_end = ">";
		String tag_open = tag_open_start + tag_open_middle + tag_open_end;
		System.out.println("Open:" + tag_open);

		tabCount += 1;
		String tag_middle = "";
		for (SystemElement e : children) {
			tag_middle += System.lineSeparator() + e.getXML();
		}
		System.out.println("Middle:" + tag_middle);
		tabCount -= 1;

		String tag_close = tabs + "</" + name + ">";
		System.out.println("Close:" + tag_close);

		return tag_open + tag_middle + System.lineSeparator() + tag_close;
	}

	public final String attributesToXML() {
		String result = "";
		for (String key : attributeKeys) {
			String value = attributes.get(key);
			if (!isBlank(value)) {
				result += "\t" + key + "=\"" + value + "\"";
			}
		}
		return result;
	}
	/*
	 * public String attributeToXML(String name, String value) { return "\t" +
	 * name + "=\"" + value + "\""; }
	 */

	// Places every object in this object's parent-child hierarchy system into a
	// single list
	public final ArrayList<SystemElement> collapseHierarchy() {
		ArrayList<SystemElement> result = new ArrayList<SystemElement>();
		result.add(this);
		for (SystemElement child : children) {
			result.addAll(child.collapseHierarchy());
		}
		return result;
	}

	public final static boolean isBlank(String input) {
		return (input == null || input.length() < 1);
	}

	/*
	 * public ArrayList<Integer> diceRangeToDistribution(String input) { // Dice
	 * ArrayList<Integer> result = new ArrayList<Integer>();
	 * 
	 * switch(input) { case "random": case "equidistant": for(int i = 0; i <=
	 * 359; i++) { result.add(i); } break; default: int dice_index =
	 * input.indexOf('d'); if (dice_index != -1) { int rolls =
	 * Integer.valueOf(input.substring(0, dice_index)); int sides; int bonus;
	 * int bonus_index = input.indexOf("+"); if (bonus_index == -1) {
	 * bonus_index = input.indexOf("-"); } // No bonus if (bonus_index == -1) {
	 * sides = Integer.valueOf(input.substring(dice_index + 1)); bonus = 0; }
	 * else { sides = Integer.valueOf(input.substring(dice_index + 1,
	 * bonus_index)); bonus = Integer.valueOf(input.substring(bonus_index)); }
	 * result.addAll(roll(rolls, sides, bonus)); } else { int range_index =
	 * input.indexOf("-"); if (range_index != -1) { int min =
	 * Integer.valueOf(input.substring(0, range_index)); int max =
	 * Integer.valueOf(input.substring(range_index + 1)); /* for(int i = min; i
	 * < max; i++) { result.add(i); }
	 */ /*
		 * for(int i = min; i <= max; i++) { result.add(i); } } else { int
		 * constant = Integer.valueOf(input); result.add(constant); } } } return
		 * result; } public ArrayList<Integer> diceRangeToMinMax(String input) {
		 * // Dice ArrayList<Integer> result = new ArrayList<Integer>();
		 * 
		 * switch(input) { case "random": case "equidistant": result.add(0);
		 * result.add(360); break; default: int dice_index = input.indexOf('d');
		 * if (dice_index != -1) { int rolls =
		 * Integer.valueOf(input.substring(0, dice_index)); int sides; int
		 * bonus; int bonus_index = input.indexOf("+"); if (bonus_index == -1) {
		 * bonus_index = input.indexOf("-"); } // No bonus if (bonus_index ==
		 * -1) { sides = Integer.valueOf(input.substring(dice_index + 1)); bonus
		 * = 0; } else { sides = Integer.valueOf(input.substring(dice_index + 1,
		 * bonus_index)); bonus = Integer.valueOf(input.substring(bonus_index));
		 * } result.add(rolls + bonus); // Min case result.add(rolls * sides +
		 * bonus); // Max case } else { int range_index = input.indexOf("-"); if
		 * (range_index != -1) { int min = Integer.valueOf(input.substring(0,
		 * range_index)); int max = Integer.valueOf(input.substring(range_index
		 * + 1)); /* for(int i = min; i < max; i++) { result.add(i); }
		 */ /*
			 * result.add(min); result.add(max); } else { int constant =
			 * Integer.valueOf(input); result.add(constant);
			 * result.add(constant); } } } return result; }
			 */
	/*
	public final ArrayList<Integer> diceRange(String input, int option) {
		// Dice
		ArrayList<Integer> result = new ArrayList<Integer>();

		int dice_index = input.indexOf('d');
		if (dice_index != -1) {
			int rolls = Integer.valueOf(input.substring(0, dice_index));
			int sides;
			int bonus;
			int bonus_index = input.indexOf("+");
			if (bonus_index == -1) {
				bonus_index = input.indexOf("-");
			}
			// No bonus
			if (bonus_index == -1) {
				sides = Integer.valueOf(input.substring(dice_index + 1));
				bonus = 0;
			} else {
				sides = Integer.valueOf(input.substring(dice_index + 1, bonus_index));
				bonus = Integer.valueOf(input.substring(bonus_index));
			}
			switch (option) {
			case DICERANGE_EXTREME:
				result.add(rolls + bonus); // Min case
				result.add(rolls * sides + bonus); // Max case
				break;
			case DICERANGE_DISTRIBUTED:
				result.addAll(rollPermutations(rolls, sides, bonus));
				break;
			}
		} else {
			int range_index = input.indexOf("-");
			if (range_index != -1) {
				int min = Integer.valueOf(input.substring(0, range_index));
				int max = Integer.valueOf(input.substring(range_index + 1));
				/*
				 * for(int i = min; i < max; i++) { result.add(i); }
				 *//*
				switch (option) {
				case DICERANGE_EXTREME:
					result.add(min);
					result.add(max);
					break;
				case DICERANGE_DISTRIBUTED:
					for (int i = min; i <= max; i++) {
						result.add(i);
					}
					break;
				}
			} else {
				int constant = Integer.valueOf(input);

				switch (option) {
				case DICERANGE_EXTREME:
					result.add(constant);
					result.add(constant);
					break;

				case DICERANGE_DISTRIBUTED:
					result.add(constant);
					break;
				}
			}
		}
		return result;
	}
	*/

	public final static int roll(String input) {
		// Dice
		int result = 0;
		if(isBlank(input))
		{
			return result;
		}
		else
		{
			int dice_index = input.indexOf('d');
			if (dice_index != -1) {
				int rolls = Integer.valueOf(input.substring(0, dice_index));
				int sides;
				int bonus;
				int bonus_index = input.indexOf("+");
				if (bonus_index == -1) {
					bonus_index = input.indexOf("-");
				}
				// No bonus
				if (bonus_index == -1) {
					sides = Integer.valueOf(input.substring(dice_index + 1));
					bonus = 0;
				} else {
					sides = Integer.valueOf(input.substring(dice_index + 1, bonus_index));
					bonus = Integer.valueOf(input.substring(bonus_index));
				}
				result = roll(rolls, sides, bonus);
			} else {
				int range_index = input.indexOf('-', input.startsWith("-") ? 1 : 0);
				if (range_index != -1) {
					int min = Integer.valueOf(input.substring(0, range_index));
					int max = Integer.valueOf(input.substring(range_index + 1));
					int range = max - min;
					result = min + new Random().nextInt(range + 1);
				} else {
					int constant = Integer.valueOf(input);
					result = constant;
				}
			}
			return result;
		}
		
	}
	public Color getColor()
	{
		return getSelected() ? selectedColor : defaultColor;
	}
	public final String[] getCompatibleSubElements() {
		return subelements;
	}

	public final String[] getAttributeKeys() {
		return attributeKeys;
	}

	public final String getAttribute(String key) {
		return attributes.get(key);
	}

	public final void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public static final double square(double input) {
		return Math.pow(input, 2);
	}

	public static final double sinDegrees(double input) {
		return Math.sin(Math.toRadians(input));
	}

	public static final double cosDegrees(double input) {
		return Math.cos(Math.toRadians(input));
	}

	public final void destroy() {
		parent.removeChild(this);
	}
	
	public final static int roll(int dice, int sides, int bonus) {
		int result = 0;
		for (int i = 0; i < dice; i++) {
			result += new Random().nextInt(sides) + 1;
		}
		result += bonus;
		return result;
	}

	public final static ArrayList<Integer> rollPermutations(int dice, int sides, int bonus) {
		ArrayList<Integer> choices = new ArrayList<Integer>();
		for (int i = 1; i <= sides; i++) {
			choices.add(i);
		}
		ArrayList<ArrayList> rollLists = permute(choices, dice);
		ArrayList<Integer> totals = new ArrayList<Integer>();
		for (ArrayList rolls : rollLists) {
			int total = 0;
			for (int roll : (ArrayList<Integer>) rolls) {
				total += roll;
			}
			total += bonus;
			totals.add(total);
		}
		return totals;
	}

	public final static ArrayList<ArrayList> permute(ArrayList... source) {
		int source_count = source.length;
		ArrayList<Integer> source_lengths = new ArrayList<Integer>();
		ArrayList<Integer> source_indices = new ArrayList<Integer>();
		for (int i = 0; i < source_count; i++) {
			source_lengths.add(source[i].size());
			source_indices.add(0);
		}
		ArrayList<ArrayList> result = new ArrayList<ArrayList>();
		boolean active = true;
		while (active) {
			ArrayList permutation = new ArrayList();
			for (int i = 0; i < source_indices.size(); i++) {
				int index = source_indices.get(i);
				ArrayList list = source[i];
				Object item = list.get(index);
				permutation.add(item);
			}
			int firstIndex = source_indices.get(0);
			firstIndex++;
			source_indices.set(0, firstIndex);
			for (int i = 0; i < source_indices.size(); i++) {
				int index = source_indices.get(i);
				int limit = source_lengths.get(i);
				if (index == limit) {
					source_indices.set(i, 0);
					int i_nextIndex = i + 1;
					if (i_nextIndex == source_count) {
						active = false;
					} else {
						int nextIndex = source_indices.get(i_nextIndex);
						nextIndex++;
						source_indices.set(i_nextIndex, nextIndex);
					}
				}
			}
			result.add(permutation);
		}
		return result;

	}

	public final static ArrayList<ArrayList> permute(ArrayList source, int k) {
		ArrayList<Integer> indices = new ArrayList<Integer>(); // A list of
																// indexes with
																// length equal
																// to k. A
																// permutation
																// set is formed
																// by an object
																// from the
																// source set at
																// each index
																// specified in
																// the list.
																// Every time a
																// permutation
																// set is taken,
																// the first
																// index
																// increases by
																// one. When an
																// index reaches
																// the length of
																// the source
																// set, it
																// resets back
																// to 0 and
																// index in
																// front of it
																// increases by
																// one. When the
																// last index
																// reaches the
																// length of the
																// source set,
																// the method
																// returns the
																// result.
		for (int i = 0; i < k; i++) {
			indices.add(0);
		}
		ArrayList<ArrayList> result = new ArrayList<ArrayList>();
		boolean active = true;
		int source_indexMax = source.size() - 1;

		while (active) {
			ArrayList<Object> permutation = new ArrayList<Object>();
			for (int i : indices) {
				Object item = source.get(i);
				permutation.add(item);
			}
			int firstIndex = indices.get(0);
			firstIndex += 1;
			indices.set(0, firstIndex);
			/*
			 * //Since numbers generally roll over only when the first index
			 * rolls over, wait until that happens if(firstIndex ==
			 * source_indexMax) { indices.set(0, 0); int secondIndex =
			 * indices.get(1); secondIndex += 1; indices.set(1, secondIndex);
			 * for(int i = 1; i < source_indexMax; i++) { int index_i =
			 * indices.get(i); if(index_i > source_indexMax) { indices.set(i,
			 * 0); int i_next = i + 1; int nextIndex = indices.get(i_next);
			 * nextIndex += 1; indices.set(i_next, nextIndex); } } int
			 * indices_lastIndex = indices.size() - 1; int lastIndex =
			 * indices.get(indices_lastIndex); if(lastIndex > source_indexMax) {
			 * active = false; } }
			 */
			/// *
			for (int i = 0; i < k - 1; i++) {
				int index_i = indices.get(i);
				if (index_i > source_indexMax) {
					indices.set(i, 0);
					int i_next = i + 1;
					int index_next = indices.get(i_next);
					index_next += 1;
					indices.set(i_next, index_next);
				}
			}
			int indices_lastIndex = indices.size() - 1;
			int lastIndex = indices.get(indices_lastIndex);
			if (lastIndex > source_indexMax) {
				active = false;
			}
			// */
			result.add(permutation);
		}
		return result;
	}
	public static final void printVariable(String name, Object value)
	{
		System.out.println(name + ": " + value.toString());
	}
	public static final void print(String message)
	{
		System.out.println(message);
	}
}
