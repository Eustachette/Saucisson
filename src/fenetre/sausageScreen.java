package fenetre;

import java.awt.Color;

import javax.swing.JProgressBar;

public class sausageScreen
{
	private Thread p;
	private JProgressBar saucisse;
	
	public sausageScreen()
	{
		p = new Thread(new Traitement());
		saucisse = new JProgressBar();
		saucisse.setMaximum(100);
		saucisse.setMinimum(0);
		saucisse.setStringPainted(true);
		saucisse.setBackground(Color.gray);
		saucisse.setForeground(Color.green);
		
		p.start();
	}
	
	class Traitement implements Runnable
	{
		public void run()
		{
			for(int val = 0; val <= 500; val++)
			{
				saucisse.setValue(val);
				try 
				{
					p.sleep(100);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @return the saucisse
	 */
	public JProgressBar getSaucisse()
	{
		return saucisse;
	}

	/**
	 * @param saucisse the saucisse to set
	 */
	public void setSaucisse(JProgressBar saucisse)
	{
		this.saucisse = saucisse;
	}

}
