
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import System.PropertiesPanel;
import System.SystemElement;
import System.WindowPanel;
import System.GraphicsPanel;
import System.OptionsPanel;

public class Window implements Runnable {
	String file_name = "0.txt";
	File file;
	Writer writer;
	BufferedReader reader;
	JFrame frame;
	
	WindowPanel frame_panel;
	PropertiesPanel properties_panel;
	GraphicsPanel graphics_panel;
	OptionsPanel options_panel;
	
	ArrayList<StationType> StationTypes;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Window window = new Window();
		window.initialize();
	}
	
	public Window(){
	}
	public void initialize()
	{
		frame = new JFrame();
		frame.setTitle("TransFormer");
		frame.setSize(1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame_panel = new WindowPanel();
		frame_panel.setLayout(new BoxLayout(frame_panel, BoxLayout.X_AXIS));
		frame_panel.init();
		frame.add(frame_panel);
		
		frame.pack();
		frame.setVisible(true);
	}
	public void print()
	{
		try {
			for(String s: Files.readAllLines(Paths.get(file_name)))
			{
				System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void write(String input)
	{
		   try {
			Files.write(Paths.get(file_name), input.getBytes("utf-16"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void delete()
	{
		try {
			Files.delete(Paths.get(file_name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close()
	{
		System.exit(0);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
