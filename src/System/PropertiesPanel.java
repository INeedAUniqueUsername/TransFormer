package System;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PropertiesPanel extends WindowPanel implements MouseListener,  ActionListener{
	HashMap<String, JTextField> attributeFields = new HashMap<String, JTextField>();
	
	JPanel titlePanel;
	JLabel title;
	JLabel elementName;
	JTextField elementDescriptionField;
	
	JPanel attributePanel;
	
	JPanel actionPanel;
	JButton applyButton;
	JButton visibilityButton;
	JButton deleteButton;
	
	JPanel createPanel;
	JLabel createLabel;
	public void init()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(320, 1080));
		setName("Options");
		setBorder(BorderFactory.createLineBorder(Color.black));
		setAlignmentY(0f);
		
		title = new JLabel("Element Properties");
		title.setFont(FONT_TITLE);
		
		elementName = new JLabel("Element Name");
		elementName.setFont(FONT_SUBTITLE);
		
		elementDescriptionField = new JTextField();
		elementDescriptionField.setToolTipText("Set a description for this element");
		elementDescriptionField.setFont(FONT_SUBTITLE);
		
		titlePanel = generateGridPanel(0, 1);
		titlePanel.add(title);
		titlePanel.add(elementName);
		titlePanel.add(elementDescriptionField);
		titlePanel.setPreferredSize(new Dimension(320, 90));
		
		attributePanel = generateGridPanel(0, 2);
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		attributePanel.setPreferredSize(new Dimension(320, 360));
		
		actionPanel = generateGridPanel(0, 1);
		actionPanel.setPreferredSize(new Dimension(320, 180));
		
		applyButton = new JButton("Apply");
		applyButton.setFont(FONT_TEXT);
		applyButton.addActionListener(this);
		
		visibilityButton = new JButton();
		visibilityButton.setFont(FONT_TEXT);
		visibilityButton.addActionListener(this);
		
		deleteButton = new JButton("Delete");
		deleteButton.setFont(FONT_TEXT);
		deleteButton.addActionListener(this);
		
		actionPanel.add(applyButton);
		actionPanel.add(visibilityButton);
		actionPanel.add(deleteButton);
		
		createPanel = generateGridPanel(0, 1);
		createPanel.setPreferredSize(new Dimension(320, 360));
		createPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		createLabel = new JLabel("Create New Sub-Element");
		createLabel.setFont(FONT_SUBTITLE);
		
		createPanel.add(createLabel);
		
		add(titlePanel);
		add(attributePanel);
		add(actionPanel);
		add(createPanel);
		//Add "Copy" button that clones the selected element into a variable
		//Add "Paste" button that pastes the copied element into the selected element (check if they are compatible first)
	}
	public void initializeElement(SystemElement se)
	{
		
		//Update titlePanel
		elementName.setText(se.toString());
		elementDescriptionField.setText(se.getDescription());
		
		//Update attributePanel and attributeFields
		attributePanel.removeAll();
		attributeFields.clear();
		for(String attribute: se.getAttributeKeys())
		{
			JLabel nameLabel = new JLabel(attribute + "=");
			nameLabel.setFont(FONT_CODE);
			attributePanel.add(nameLabel);
			
			JTextField valueField = new JTextField(se.getAttribute(attribute));
			valueField.setFont(FONT_CODE);
			attributePanel.add(valueField);
			
			attributeFields.put(attribute, valueField);
		}
		
		//Update visibility button
		visibilityButton.setText(se.getVisible() ? "Hide" : "Show");
		
		//Update delete button
		deleteButton.setEnabled(!(se instanceof SystemGroup));
		
		createPanel.removeAll();
		createPanel.add(createLabel);
		ArrayList<String> compatibleSubelements = new ArrayList<String>(Arrays.asList(se.getCompatibleSubElements()));
		for(String subelement: new String[]{"Group", "Primary", "Siblings", "Orbitals", "Station", "Table", "Label"})
		{
			JButton createButton = new JButton(subelement);
			createButton.setFont(FONT_TEXT);
			createButton.addActionListener(this);
			createPanel.add(createButton);
			createButton.setEnabled(compatibleSubelements.contains(subelement));
		}
		
		System.out.println("Count: " + getComponentCount());
		revalidate();
		repaint();
	}
	
	public JPanel generateGridPanel(int rows, int cols)
	{
		JPanel result = new JPanel();
		result.setLayout(new GridLayout(rows, cols));
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source instanceof JButton)
		{
			JButton pressed = (JButton) source;
			String buttonText = pressed.getText();
			System.out.println("Button pressed: " + buttonText);
			if(pressed.equals(applyButton))
			{
				applyProperties();
				//JOptionPane.showMessageDialog(this, "Applied changes to " + selectedElement.getName() + ".");
			}
			else if(pressed.equals(deleteButton))
			{
				options.deleteElement(selectedElement);
				//selectElement(selectedParent);
			}
			else if(pressed.equals(visibilityButton))
			{
				boolean visible = !selectedElement.getVisible();
				selectedElement.setVisible(visible);
				System.out.println(selectedElement.toString() + " is now " + (visible ? "visible" : "hidden"));
				pressed.setText(visible ? "Hide" : "Show");
			}
			else
			{
				createChildElement(buttonText);
			}
		}
		repaintAll();
	}

	public void applyProperties() {
		selectedElement.setDescription(elementDescriptionField.getText());
		for(String name: attributeFields.keySet())
		{
			JTextField valueField = attributeFields.get(name);
			String value = valueField.getText();
			selectedElement.setAttribute(name, value);
		}
		options.updateTreeText(selectedElement);
	}
	public void createChildElement(String name)
	{
		SystemElement element = null;
		switch (name) {
		 //Cannot make a new <SystemGroup>
		//case "SystemGroup":
		 //element = new SystemGroup();
		//break;
		case "Group":
			element = new Group();
			break;
		case "Primary":
			element = new Primary();
			break;
		case "Siblings":
			element = new Siblings();
			break;
		case "Orbitals":
			element = new Orbitals();
			break;
		case "Station":
			element = new Station();
			break;
		case "Table":
			element = new Table();
			break;
		case "LevelTable":
			// element = new LevelTable();
			break;
		case "Label":
			// element = new Label();
			break;
		}
		if(element != null)
		{
			/*
			for(String attribute: element.getAttributeKeys())
			{
				element.setAttribute(attribute, "" + (int) (Math.random()*100));
			}
			*/
			System.out.println("Adding " + element.toString() + " to " + selectedElement.toString());
			element.setVisible(false);
			selectedElement.addChild(element);
			options.addElement(element);
			if(selectedElement instanceof Table)
			{
				element.addAttributeKey("chance");
			}
			else if(selectedElement instanceof LevelTable)
			{
				element.addAttributeKey("levelFrequency");
			}
			
			//selectElement(element);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
