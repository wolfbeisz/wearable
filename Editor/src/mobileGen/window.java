package mobileGen;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class window extends JFrame implements ActionListener
{
	//JPanel panel;
	private JLayeredPane panel1 = getLayeredPane();
	private JLayeredPane panel2 = getLayeredPane();

	public window()
	{
		super("Demo Designer");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setVisible(true);
		
		
		panel1=new JLayeredPane();
		panel1.setLayout(new FlowLayout());
		JButton button=new JButton("CLICK HERE");
		panel1.add(button,BorderLayout.SOUTH);
		button.addActionListener(this);
		
		panel2 = new JLayeredPane();
		panel2.setLayout(new FlowLayout());
		JButton button2=new JButton("CLICK HERE");
		panel2.add(button2,BorderLayout.NORTH);
		button2.addActionListener(this);

		panel1.setVisible(false);
		panel2.setVisible(true);

	}

	public void actionPerformed(ActionEvent evt)
	{
		panel1.add(new JButton("Button"));
		panel1.revalidate();
		validate();
	}

	public static void main(String[]args)
	{
		window acojfar=new window();
	}
}