package System;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class OptionsPanel extends JPanel implements ActionListener, ListSelectionListener {

	GraphicsPanel system_panel;
	JList<String> orbitalRenderOptions;
	JButton generateButton;
	JPanel elementPanel;
	JList<String> subelementList;
	JButton parentButton;
	public void init()
	{
		orbitalRenderOptions = new JList<String>(new String[]{"Render extremes", "Render distances", "Render dots", "Render random"});
		orbitalRenderOptions.setSelectedIndex(0);
		orbitalRenderOptions.setLocation(500, 500);
		orbitalRenderOptions.setVisible(true);
		orbitalRenderOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orbitalRenderOptions.setLayoutOrientation(JList.VERTICAL);
		orbitalRenderOptions.addListSelectionListener(this);
		add(orbitalRenderOptions);
		generateButton = new JButton("Generate");
		generateButton.addActionListener(this);
		add(generateButton);
		
	}
	
	public void setSystemPanel(GraphicsPanel panel)
	{
		system_panel = panel;
	}
	
	public void selectElement(SystemElement se)
	{
		remove(elementPanel);
		elementPanel = new JPanel();
		subelementList = new JList<String>();
		//Add a button for each subelement and one for the parent
		for(SystemElement subelement : se.getChildren())
		{
			//subelementList.add
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source.equals(orbitalRenderOptions))
		{
			System.out.println("Orbital Render Options changed");
			Orbitals.setRenderOption(orbitalRenderOptions.getSelectedIndex());
			system_panel.repaint();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source.equals(generateButton))
		{
			String xml = system_panel.system.getXML();
			JOptionPane.showInputDialog("XML", xml);
		}
	}

}
