package main;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class OptionPanel extends JPanel implements ActionListener {
	
	Main w;
	JButton button1;
	JButton button2;
	JButton button3, b4;
	
	public static int level = 1;
	
	public OptionPanel(Main w) {
		this.w = w;
		button1 = new JButton("Level 1");
		button2 = new JButton("Level 2");
		button3 = new JButton("Level 3");
		b4 = new JButton("Level 4");
		
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		b4.addActionListener(this);
		
		add(button1);
		add(button2);
		add(button3);
		add(b4);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button1) {
			level = 1;
		}
		else if (e.getSource()==button2) {
			level = 2;
		}
		else if (e.getSource()==button3) {
			level = 3;
		}
		else if(e.getSource() == b4)
		{
			level = 4;
		}
		else {
			level = 0;
		}
		w.changePanel();
	}
	
}