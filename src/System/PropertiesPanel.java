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

	public void initializeElement(SystemElement se)
	{
		//Clear everything
		removeAll();
		
		JLabel title = new JLabel("Element Properties");
		title.setFont(FONT_TITLE);
		JLabel elementName = new JLabel(se.toString());
		elementName.setFont(FONT_SUBTITLE);
		JPanel titlePanel = generateGridPanel(0, 1);
		titlePanel.add(title);
		titlePanel.add(elementName);
		titlePanel.setPreferredSize(new Dimension(320, 90));
		add(titlePanel);
		
		attributeFields.clear();
		JPanel attributePanel = generateGridPanel(0, 2);
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.black));
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
		//attributePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(attributePanel);
		attributePanel.setPreferredSize(new Dimension(320, 360));
		JPanel actionPanel = generateGridPanel(0, 1);
		JButton applyButton = new JButton("Apply");
		applyButton.setFont(FONT_TEXT);
		applyButton.addActionListener(this);
		actionPanel.add(applyButton);
		
		JButton visibilityButton = new JButton(se.getVisible() ? "Hide" : "Show");
		visibilityButton.setFont(FONT_TEXT);
		visibilityButton.addActionListener(this);
		actionPanel.add(visibilityButton);
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.setFont(FONT_TEXT);
		deleteButton.addActionListener(this);
		actionPanel.add(deleteButton);
		if(se instanceof SystemGroup)
		{
			deleteButton.setEnabled(false);
		}
		actionPanel.setPreferredSize(new Dimension(320, 180));
		add(actionPanel);
		
		JLabel createLabel = new JLabel("Create New Sub-Element");
		createLabel.setFont(FONT_SUBTITLE);
		JPanel createPanel = generateGridPanel(0, 1);
		createPanel.setPreferredSize(new Dimension(320, 360));
		createPanel.setBorder(BorderFactory.createLineBorder(Color.black));
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
		
		add(createPanel);
		
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
			if(buttonText.equals("Apply"))
			{
				for(String name: attributeFields.keySet())
				{
					JTextField valueField = attributeFields.get(name);
					String value = valueField.getText();
					selectedElement.setAttribute(name, value);
				}
				//JOptionPane.showMessageDialog(this, "Applied changes to " + selectedElement.getName() + ".");
			}
			else if(buttonText.equals("Delete"))
			{
				SystemElement selectedParent = selectedElement.getParent();
				selectedElement.destroy();
				selectElement(selectedParent);
			}
			else if(buttonText.equals("Hide") || buttonText.equals("Show"))
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
			// element = new Table();
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
			selectedElement.addChild(element);
			element.setVisible(false);
			selectElement(element);
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
