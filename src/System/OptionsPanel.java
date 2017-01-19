package System;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class OptionsPanel extends WindowPanel implements ActionListener, TreeSelectionListener {

	//JList<String> orbitalRenderOptions;
	JButton generateButton;
	/* Deprecated: Replaced by JTree
	JPanel elementPanel;
	JButton parentButton;
	JList<String> subelementList;
	JButton selectButton;
	JButton deleteButton;
	*/
	DefaultTreeModel elementTreeModel;
	JTree elementTree;
	JScrollPane elementTreePane;
	DefaultTreeCellRenderer elementTreeCellRenderer;

	public void init() {
		/* Deprecated: Custom Orbital Rendering no longer supported
		orbitalRenderOptions = new JList<String>(
				new String[] { "Render extremes", "Render distances", "Render dots", "Render random" });
		orbitalRenderOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orbitalRenderOptions.setLayoutOrientation(JList.VERTICAL);
		orbitalRenderOptions.addListSelectionListener(this);
		orbitalRenderOptions.setFont(FONT_TEXT);
		*/
		try {
			elementTreeCellRenderer = new DefaultTreeCellRenderer() {
				ImageIcon icon_SystemGroup = new ImageIcon(ImageIO.read(new File("src/System/SystemGroup.png")));
				ImageIcon icon_Orbitals= new ImageIcon(ImageIO.read(new File("src/System/Orbitals.png")));
				ImageIcon icon_Group = new ImageIcon(ImageIO.read(new File("src/System/Group.png")));
				ImageIcon icon_Primary = new ImageIcon(ImageIO.read(new File("src/System/Primary.png")));
				ImageIcon icon_Siblings = new ImageIcon(ImageIO.read(new File("src/System/Siblings.png")));
				ImageIcon icon_Station = new ImageIcon(ImageIO.read(new File("src/System/Station.png")));
				
				public Component getTreeCellRendererComponent(
			        JTree tree,
			        Object value,
			        boolean sel,
			        boolean expanded,
			        boolean leaf,
			        int row,
			        boolean hasFocus) {

				    super.getTreeCellRendererComponent(
				                    tree, value, sel,
				                    expanded, leaf, row,
				                    hasFocus);
				    SystemElement element = (SystemElement) ((DefaultMutableTreeNode) value).getUserObject();
				    //System.out.println("Rendering " + element.toString() + " Icon");
				    if(element instanceof SystemGroup) {
				        setIcon(icon_SystemGroup);
				        setToolTipText("Contains all other elements that define the system.");
				    } else if(element instanceof Orbitals){
				    	setIcon(icon_Orbitals);
				        setToolTipText("Defines the position of its child element relative to its parent element.");
				    } else if(element instanceof Group) {
				    	setIcon(icon_Group);
				    	setToolTipText("Attaches all its child elements to its parent element");
				    } else if(element instanceof Primary) {
				    	setIcon(icon_Primary);
				    	setToolTipText("Contains the primary element in a <Group>");
				    } else if(element instanceof Siblings) {
				    	setIcon(icon_Siblings);
				    	setToolTipText("Creates its child elements in orbits similar to that of its parent.");
				    } else if(element instanceof Station) {
				    	setIcon(icon_Station);
				    	setToolTipText("Defines an object in space such as a star, planet, or station.");
				    }
				
				    return this;
				}
			};
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DefaultMutableTreeNode systemgroupNode = system.toTreeNode();
		elementTreeModel = new DefaultTreeModel(systemgroupNode);
		elementTree = new JTree(elementTreeModel);
	    elementTree.getSelectionModel().setSelectionMode
	    	(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    elementTree.setAlignmentX(Component.CENTER_ALIGNMENT);
	    elementTree.setFont(FONT_TEXT);
	    elementTree.setName("System");
	    elementTree.setShowsRootHandles(true);
	    elementTree.setCellRenderer(elementTreeCellRenderer);
	    elementTree.expandRow(0);
	    elementTree.setSelectionPath(new TreePath(systemgroupNode));
	    elementTree.addTreeSelectionListener(this);
	    
	    elementTreePane = new JScrollPane(elementTree);
	    elementTreePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    
		generateButton = new JButton("Generate");
		generateButton.addActionListener(this);
		//elementPanel = new JPanel();
		//add(orbitalRenderOptions);
		add(elementTreePane);
		add(generateButton);
		//add(elementPanel);
	}

	public void updateTreeText(SystemElement se)
	{
		elementTreeModel.nodeChanged(getNode(se));
	}
	public void addElement(SystemElement se)
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(se);
		elementTreeModel.insertNodeInto(node, (DefaultMutableTreeNode) elementTree.getLastSelectedPathComponent(), 0);
	    expandTreeNodes();
	    System.out.println("Tree Path (Create): " + new TreePath(elementTreeModel.getPathToRoot(node)));
	    elementTree.setSelectionPath(new TreePath(elementTreeModel.getPathToRoot(node)));
	}
	public void deleteElement(SystemElement se)
	{
		DefaultMutableTreeNode node = getNode(se);
		TreeNode node_parent = node.getParent();
	    System.out.println("Tree Path (Delete): " + new TreePath(elementTreeModel.getPathToRoot(node_parent)).toString());
	    elementTree.setSelectionPath(new TreePath(elementTreeModel.getPathToRoot(node_parent)));
		se.destroy();
		elementTreeModel.removeNodeFromParent(node);
	    expandTreeNodes();
	}
	public DefaultMutableTreeNode getNode(SystemElement se)
	{
		DefaultMutableTreeNode theNode = null;
		for (Enumeration<DefaultMutableTreeNode> e = (Enumeration<DefaultMutableTreeNode>) ((DefaultMutableTreeNode) elementTreeModel.getRoot()).depthFirstEnumeration(); e.hasMoreElements() && theNode == null;) {
		    DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
		    if (node.getUserObject().equals(se)) {
		        theNode = node;
		        break;
		    }
		}
		System.out.println("Found Node: " + theNode.toString());
		System.out.println("Found Parent: " + theNode.getParent().toString());
		return theNode;
	}
	public void expandTreeNodes()
	{
		for(int i = 0; i < elementTree.getRowCount(); i++){
	        elementTree.expandRow(i);
	    }
	}
	public void initializeElement(SystemElement se) {
		/*
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

		selectButton = new JButton("Select subelement");
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
		*/
	}

	/*
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		/*
		if (source.equals(orbitalRenderOptions)) {
			System.out.println("Orbital Render Options changed");
			Orbitals.setRenderOption(orbitalRenderOptions.getSelectedIndex());
			repaintAll();
		}
		*/
		/*
		if (source.equals(subelementList)) {
			System.out.println("Subelement selected");
			selectedElement.setSelectedChildren(false);
			SystemElement selected = selectedElement.getChildren().get(subelementList.getSelectedIndex());
			selected.setSelectedFamily(true);
			selectButton.setEnabled(true);
			deleteButton.setEnabled(true);
			repaintAll();
		}
		*//*
	}
	*/

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source.equals(generateButton)) {
			String xml = system.getXML();
			JOptionPane.showInputDialog("XML", xml);
		}
		/*
		else if(source.equals(selectButton))
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
		*/
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				elementTree.getLastSelectedPathComponent();
		SystemElement element = (SystemElement) node.getUserObject();
		selectElement(element);
	}

}
