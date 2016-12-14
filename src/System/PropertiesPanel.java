package System;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
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

public class PropertiesPanel extends JPanel implements MouseListener,  ActionListener{
	GraphicsPanel system_panel;
	SystemElement selectedElement;
	HashMap<String, JTextField> attributeFields = new HashMap<String, JTextField>();
	
	
	public void setSystemPanel(GraphicsPanel sp)
	{
		system_panel = sp;
	}

	public void selectElement(SystemElement se)
	{
		System.out.println("Clicked element: " + se.getName());
		
		selectedElement = se;
		//Clear everything
		removeAll();
		JLabel title = new JLabel("Element Properties");
		JLabel elementName = new JLabel(se.getName());
		JPanel titlePanel = generateGridPanel(0, 1);
		titlePanel.add(title);
		titlePanel.add(elementName);
		titlePanel.setPreferredSize(new Dimension(320, 180));
		add(titlePanel);
		attributeFields.clear();
		JPanel attributePanel = generateGridPanel(0, 2);
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		for(String attribute: se.getAttributeKeys())
		{
			attributePanel.add(new JLabel(attribute));
			
			JTextField valueField = new JTextField(se.getAttribute(attribute));
			attributeFields.put(attribute, valueField);
			
			attributePanel.add(valueField);
		}
		add(attributePanel);
		attributePanel.setPreferredSize(new Dimension(320, 360));
		JPanel actionPanel = generateGridPanel(0, 1);
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(this);
		actionPanel.add(applyButton);
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		actionPanel.add(deleteButton);
		if(se instanceof SystemGroup)
		{
			deleteButton.setEnabled(false);
		}
		actionPanel.setPreferredSize(new Dimension(320, 180));
		add(actionPanel);
		JPanel createPanel = generateGridPanel(0, 1);
		createPanel.setPreferredSize(new Dimension(320, 360));
		createPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		createPanel.add(new JLabel("Create New Sub-Element"));
		ArrayList<String> compatibleSubelements = new ArrayList<String>(Arrays.asList(se.getCompatibleSubElements()));
		for(String subelement: new String[]{"Group", "Primary", "Siblings", "Orbitals", "Station", "Table", "Label"})
		{
			JButton createButton = new JButton(subelement);
			createButton.addActionListener(this);
			createPanel.add(createButton);
			createButton.setEnabled(compatibleSubelements.contains(subelement));
		}
		
		add(createPanel);
		
		System.out.println("Count: " + getComponentCount());
		revalidate();
		repaint();
		
		system_panel.repaint();
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
		JButton pressed = (JButton) e.getSource();
		if(pressed != null)
		{
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
			else
			{
				createChildElement(buttonText);
			}
		}
		repaint();
		system_panel.repaint();
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
			for(String attribute: element.getAttributeKeys())
			{
				element.setAttribute(attribute, "" + (int) (Math.random()*100));
			}
			
			selectedElement.addChild(element);
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
