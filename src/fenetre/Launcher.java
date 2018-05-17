package fenetre;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class Launcher 
{
	public static void main (String args[]) 
	{      
		sausageScreen sausage = new sausageScreen();
		
    	JWindow window = new JWindow();
    	window.getContentPane().add(
    	    new JLabel(new ImageIcon("images/plogo.png")),BorderLayout.CENTER);
    	
    	window.getContentPane().add(sausage.getSaucisse(), BorderLayout.SOUTH);
    	window.setBounds(500, 150, 450, 350);
    	window.setVisible(true);
    	
    	try 
    	{
    	    Thread.sleep(5000);
    	} 
    	catch (InterruptedException e) 
    	{
    	    e.printStackTrace();
    	}
    	
    	window.setVisible(false);
    	JpaintInterface jpaint = new JpaintInterface();
    	window.dispose();
    	
    }
}
