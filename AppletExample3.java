import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


@SuppressWarnings("serial")
public class AppletExample3 extends JApplet implements ActionListener, Runnable {

	Thread t = null;
	Button b;
	TextField tf;
	String username;
	int paneWidth=800, paneHeight=800;
	
	public void init() {
		setLayout(null);
		
		b = new Button("Login");
		b.setBounds(paneWidth/2-30, paneHeight/2+60, 60, 50);
		b.addActionListener(this);
		getContentPane().add(b);
		
		tf = new TextField();
		tf.setBounds(paneWidth/2-75, paneHeight/2-40,150,20);
		tf.addActionListener(this);
		getContentPane().add(tf);

		username = "Enter Username";
		
		getContentPane().setBackground(Color.white);
	}
	
	public void start() {
		t = new Thread(this);
		t.start();
	}
	
	public AppletExample3() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (tf.getText().length() > 0) {
			b.setLabel(tf.getText());
			//getContentPane().remove(tf);
		}
		repaint();
		//getContentPane().remove(b);
	}

	@Override
	public void run() {
		try {
			while (true) {
//				paneWidth = getContentPane().getBounds().width;
//				paneHeight = getContentPane().getBounds().height;
				//repaint();
				t.sleep(1000);
			} 
		} catch (InterruptedException e) {}
	}
	
	public void paint(Graphics g) {
		paintLogin(g);
	}
	
	public void paintLogin(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 800);
		g.setColor(Color.BLACK);
		g.drawString("Enter your username to login to your account", paneWidth/2-75, paneHeight/2);
		g.drawString("or Enter a new username to create a new account", paneWidth/2-75, paneHeight/2+15);
	}

}
