package com.jpaint.fenetre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import com.jpaint.dessins.ZoneDessin;

public class JpaintInterface extends JFrame 
{
	private JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("Fichier");
	JMenu editMenu = new JMenu("Editer");
	
	JMenuItem newPaint = new JMenuItem("Nouveau", new ImageIcon("images/document.png"));
	JMenuItem savePaint = new JMenuItem("Enregistrer et Analyser", new ImageIcon("images/save.png"));
	JMenuItem quit = new JMenuItem("Quitter", new ImageIcon("images/arrow.png"));
	
	JMenuItem colorPinceauMenu = new JMenuItem("Couleur");
	JMenuItem taillePinceauMenu = new JMenuItem("Taille du crayon");

	
	JLabel message = new JLabel("Dessins en cours");
	JPanel statusBar = new JPanel();



	
	private ZoneDessin zoneDessin = new ZoneDessin();
	
	private Border zoneDeDessin = BorderFactory.createLineBorder(Color.blue, 3);
	
	JScrollPane scrollPane = new JScrollPane(zoneDessin);
	JPanel zoneCentrale = new JPanel();
	
	JPanel dock = new JPanel();

	JLabel texte;
	
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	
	public JpaintInterface()
	{
		this.setSize(700, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setupMenu();
		this.setupStatusBar();
		this.setupDock();
		this.setupZoneDessin();
		this.zoneDessin.setBorder(zoneDeDessin);
		this.zoneCentrale.add(scrollPane, BorderLayout.CENTER);
		this.getContentPane().add(zoneCentrale, BorderLayout.WEST);
		this.setVisible(true);
	}
	
	public void setupZoneDessin()
	{
		texte = new JLabel();
		texte.setTransferHandler(new TransferHandler("text"));
		texte.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e) 
			{
				JComponent comp = (JComponent) e.getSource();
				TransferHandler handle = comp.getTransferHandler();
				handle.exportAsDrag(comp, e, TransferHandler.MOVE);
			}	
		});
		
	}
	
	private void setupDock()
	{
		JPanel toolsDock = new JPanel();
		JPanel colorsDock = new JPanel();
		
		toolsDock.setLayout(new GridLayout(4,2));
		colorsDock.setLayout(new GridLayout(4,2));

		
		dock.setPreferredSize(new Dimension(100, 70));
		dock.setLayout(new GridLayout(2,1));
		dock.add(toolsDock, BorderLayout.NORTH);
		dock.add(colorsDock, BorderLayout.SOUTH);

		
		this.getContentPane().add(dock, BorderLayout.WEST);
	}
	
	private void setupStatusBar()
	{
		statusBar.add(this.message, BorderLayout.WEST);

		
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
	}
	
	private void setupMenu()
	{
		newPaint.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{

                int option = JOptionPane.showConfirmDialog(null, "Voulez vous enregistrez les modifications",
                        "Enregistrement", JOptionPane.YES_NO_CANCEL_OPTION);

                if(option == JOptionPane.OK_OPTION)
                {
                    try {
                        zoneDessin.savePaint();
                        zoneDessin.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(option == JOptionPane.NO_OPTION)
                {
                    zoneDessin.clear();

                }

			}
		});


		quit.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				int option = JOptionPane.showConfirmDialog(null, "Voulez vous fermez PaintSausage?",
						"Quitter", JOptionPane.YES_NO_CANCEL_OPTION);
				
				if(option == JOptionPane.OK_OPTION)
				{
					System.exit(0);
				}				
			}
		});

		savePaint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    zoneDessin.savePaint();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
		
		newPaint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		savePaint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		
		fileMenu.add(newPaint);
		fileMenu.addSeparator();
		fileMenu.add(savePaint);
		fileMenu.addSeparator();
		fileMenu.add(quit);
		fileMenu.setMnemonic('F');

		
		colorPinceauMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				Color newColor = JColorChooser.showDialog(
	                     JpaintInterface.this,
	                     "Choose Background Color",
	                     Color.red);
				zoneDessin.setmCouleurPointer(newColor);
			}
		});
		taillePinceauMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] taillemPinceauToolx = {"2","4","6","8","10","12","14","15"};

                String chaineTaille = (String) JOptionPane.showInputDialog (JpaintInterface.this, "choisissez la taille du Pinceau", "Pinceau",
                        JOptionPane.QUESTION_MESSAGE,
                        null,taillemPinceauToolx,taillemPinceauToolx[1]);
                int taille = Integer.parseInt(chaineTaille);
                zoneDessin.setmPointerTaille(taille);

            }
        });

		
		
		editMenu.add(colorPinceauMenu);
		editMenu.addSeparator();
		editMenu.add(taillePinceauMenu);
		editMenu.setMnemonic('E');

		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		
		this.setJMenuBar(menuBar);
	}
	

	
	class AboutDialog extends JDialog
	{
		JLabel logo;
		JButton okButton;
		
		public AboutDialog(JFrame parent, String title, boolean modal)
		{
			super(parent, title, modal);
			this.setSize(450, 300);
			this.setLocationRelativeTo(null);
			this.setResizable(false);
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			this.setup();
			
		}

		public  void setup() 
		{
			logo = new JLabel(new ImageIcon("images/about.png"));
			okButton = new JButton("OK");
			JPanel aboutPanel = new JPanel();
			aboutPanel.setBackground(Color.white);
			aboutPanel.setLayout(new BorderLayout());
			aboutPanel.add(logo, BorderLayout.CENTER);
			aboutPanel.add(okButton, BorderLayout.SOUTH);
						
			okButton.addActionListener(new ActionListener() 
			{	
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					dismissAboutDiaolg();
				}
			});
			
			this.getContentPane().add(aboutPanel);
		}
		
		public void showAboutDialog()
		{
			this.setVisible(true);
		}
		
		public void dismissAboutDiaolg()
		{
			this.setVisible(false);
		}
	
	}
	
	class FormeDialog extends JDialog
	{
		JLabel widthText;
		JLabel heightText;
		JTextField formeWidth;
		JTextField formeHeight;
		boolean filled = true;
		JButton cancelButton;
		JButton okButton;
		
		public FormeDialog(JFrame parent, String title, boolean modal)
		{
			super(parent, title, modal);
			this.setSize(220,120);
			this.setLocationRelativeTo(null);
			this.setResizable(false);
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			this.setup();
			
		}

		public  void setup() 
		{
			okButton = new JButton("OK");
			cancelButton = new JButton("Annuler");
			
			formeWidth = new JTextField();
			formeWidth.setPreferredSize(new Dimension(50, 30));
			
			formeHeight = new JTextField();
			formeHeight.setPreferredSize(new Dimension(50, 30));
			
			widthText = new JLabel("Longueur :");
			heightText = new JLabel("Largeur :");
			
			JPanel widthPanel = new JPanel();
			widthPanel.setPreferredSize(new Dimension(300, 30));
			JPanel heightPanel = new JPanel();
			heightPanel.setPreferredSize(new Dimension(300, 30));
			JPanel buttonPanel = new JPanel();
			buttonPanel.setPreferredSize(new Dimension(300, 30));
			
			widthPanel.setLayout(new BorderLayout());
			widthPanel.add(widthText, BorderLayout.WEST);
			widthPanel.add(formeWidth, BorderLayout.CENTER);
			
			heightPanel.setLayout(new BorderLayout());
			heightPanel.add(heightText, BorderLayout.WEST);
			heightPanel.add(formeHeight, BorderLayout.CENTER);
			
			buttonPanel.add(okButton, BorderLayout.WEST);
			buttonPanel.add(cancelButton, BorderLayout.EAST);
			
						
			okButton.addActionListener(new ActionListener() 
			{	
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					zoneDessin.setFormeAbs(Integer.parseInt(formeWidth.getText()));
					zoneDessin.setFormeOrd(Integer.parseInt(formeHeight.getText()));
					zoneDessin.drawFormeCarre(1);
					dismissFormeDiaolg();
				}
			});
			
			cancelButton.addActionListener(new ActionListener() 
			{	
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					dismissFormeDiaolg();
				}
			});
			
			JPanel pan = new JPanel();
			pan.setPreferredSize(new Dimension(200,110));
			pan.setLayout(new GridLayout(3,1));
			pan.add(widthPanel);
			pan.add(heightPanel);
			pan.add(buttonPanel);
			
			this.getContentPane().add(pan);
		}
		
		public void showFormeDialog()
		{
			this.setVisible(true);
		}
		
		public void dismissFormeDiaolg()
		{
			this.setVisible(false);
		}
	
	}
	
}
