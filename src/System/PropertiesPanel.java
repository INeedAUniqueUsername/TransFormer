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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PropertiesPanel extends JPanel implements MouseListener, KeyListener, ActionListener{
	GraphicsPanel system_panel;
	SystemElement selectedElement;
	
	JLabel title = new JLabel("Element Properties");
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
		add(title);										//Add the title
		add(new JLabel(se.getName()));					//Add the element name
		JPanel attributePanel = generateGridPanel(0, 2);
		attributePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		attributeFields.clear();
		for(String attribute: se.getAttributeKeys())
		{
			attributePanel.add(new JLabel(attribute));
			
			JTextField valueField = new JTextField(se.getAttribute(attribute));
			attributeFields.put(attribute, valueField);
			
			attributePanel.add(valueField);
		}
		add(attributePanel);
		attributePanel.setPreferredSize(new Dimension(320, 1080));
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(this);
		applyButton.setAlignmentX(CENTER_ALIGNMENT);
		add(applyButton);
		if(!(se instanceof SystemGroup))
		{
			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(this);
			deleteButton.setAlignmentX(CENTER_ALIGNMENT);
			add(deleteButton);
		}
		JPanel createPanel = generateGridPanel(0, 1);
		createPanel.setPreferredSize(new Dimension(320, 1080));
		createPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		createPanel.add(new JLabel("Create New Sub-Element"));
		for(String subelement: se.getCompatibleSubElements())
		{
			JButton createButton = new JButton(subelement);
			createButton.addActionListener(this);
			createPanel.add(createButton);
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
		result.setAlignmentY(0f);
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
				SystemElement element = null;
				switch (buttonText) {
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
		}
		repaint();
		system_panel.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == e.VK_ENTER)
		{
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
