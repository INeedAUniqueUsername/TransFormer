package System;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OptionsPanel extends WindowPanel implements ActionListener, ListSelectionListener {

	JList<String> orbitalRenderOptions;
	JButton generateButton;
	JPanel elementPanel;
	JList<String> subelementList;
	JButton selectButton;
	JButton deleteButton;
	JButton parentButton;

	public void init() {
		orbitalRenderOptions = new JList<String>(
				new String[] { "Render extremes", "Render distances", "Render dots", "Render random" });
		orbitalRenderOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orbitalRenderOptions.setLayoutOrientation(JList.VERTICAL);
		orbitalRenderOptions.addListSelectionListener(this);
		orbitalRenderOptions.setFont(FONT_TEXT);
		generateButton = new JButton("Generate");
		generateButton.addActionListener(this);
		elementPanel = new JPanel();
		add(orbitalRenderOptions);
		add(generateButton);
		add(elementPanel);
	}

	public void initializeElement(SystemElement se) {

		remove(elementPanel);

		ArrayList<String> subelementNames = new ArrayList<String>();
		for (SystemElement e : se.getChildren()) {
			subelementNames.add(e.getName());
		}
		elementPanel = new JPanel();
		elementPanel.setLayout(new GridLayout(0, 1));
		elementPanel.setAlignmentY(0f);

		subelementList = new JList<String>(subelementNames.toArray(new String[subelementNames.size()]));
		subelementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		subelementList.setLayoutOrientation(JList.VERTICAL);
		subelementList.addListSelectionListener(this);
		subelementList.setFont(FONT_SUBTITLE);

		selectButton = new JButton("Select Subelement");
		selectButton.addActionListener(this);
		selectButton.setEnabled(false);
		
		deleteButton = new JButton("Delete subelement");
		deleteButton.addActionListener(this);
		deleteButton.setEnabled(false);
		
		// Add a button for each subelement and one for the parent
		SystemElement se_parent = se.getParent();
		parentButton = new JButton();
		parentButton.addActionListener(this);
		if (se_parent == null) {
			parentButton.setText("No Parent");
			parentButton.setEnabled(false);
		} else {
			parentButton.setText(se_parent.getName());
		}
		elementPanel.add(subelementList);
		elementPanel.add(selectButton);
		elementPanel.add(deleteButton);
		elementPanel.add(parentButton);
		add(elementPanel);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source.equals(orbitalRenderOptions)) {
			System.out.println("Orbital Render Options changed");
			Orbitals.setRenderOption(orbitalRenderOptions.getSelectedIndex());
			repaintAll();
		} else if (source.equals(subelementList)) {
			System.out.println("Subelement selected");
			selectedElement.setSelectedChildren(false);
			SystemElement selected = selectedElement.getChildren().get(subelementList.getSelectedIndex());
			selected.setSelectedFamily(true);
			selectButton.setEnabled(true);
			deleteButton.setEnabled(true);
			repaintAll();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source.equals(generateButton)) {
			String xml = system.getXML();
			JOptionPane.showInputDialog("XML", xml);
		} else if(source.equals(selectButton))
		{
			SystemElement selected = selectedElement.getChildren().get(subelementList.getSelectedIndex());
			selectElement(selected);
		} else if(source.equals(deleteButton))
		{
			SystemElement selected = selectedElement.getChildren().get(subelementList.getSelectedIndex());
			selected.destroy();
			selectElement(selectedElement);
		} else if (source.equals(parentButton)) {
			selectElement(selectedElement.getParent());
		}
	}

}
