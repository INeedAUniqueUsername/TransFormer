package deprecated;
import java.awt.TextField;
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
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window_Test {

	String file_name = "0.txt";
	File file;
	Writer writer;
	BufferedReader reader;
	JFrame frame;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Window_Test window = new Window_Test();
		window.initialize();
	}
	
	public Window_Test(){
	}
	public void initialize()
	{
		frame = new JFrame();
		JPanel names = new JPanel(), descriptions = new JPanel();
		for(int i = 0; i < 10; i++)
		{
			TextField input_name = new TextField(), input_description = new TextField();
			input_name.setSize(500, 12); input_description.setSize(500, 12);
			names.add(input_name);
			descriptions.add(input_description);
		}
		frame.add(names); frame.add(descriptions);
		frame.setVisible(true);
		frame.setTitle("TransFormer");
		frame.setSize(1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String output = "";
		String chars_alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		String chars_special ="~`!@#$%^&*()_-+={[}]|\\:;\"\'<,>.?/";
		String chars_number = "01234567890";
		//String chars_white = " \b\t" + System.lineSeparator() + "\f\r";
		String chars_white = " ";
		String chars_all = chars_alphabet + chars_special + chars_number + chars_white;
		int chars_count = chars_all.length();
		for(int i = 0; i < 1000; i++)
		{
			if(i < 1024)
			{
				output = output + chars_all.charAt(new Random().nextInt(chars_count));
			}
			else
			{
				output = output + System.lineSeparator();
			}
		}
		
		write(output);
		//print();
		close();
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

}
