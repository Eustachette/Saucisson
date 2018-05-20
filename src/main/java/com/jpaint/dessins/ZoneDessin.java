package com.jpaint.dessins;

//Imports the Google Cloud client library
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;

import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import javax.swing.*;


public class ZoneDessin extends JPanel 
{
	private int mPointerAbs = -50;
	private int mPointerOrd = -20;
	private boolean mClear = true;
	private boolean mEraseMode = false; //indique si on est en mode gomme
	private boolean bgChanging = false; // nous indique si nous sommes entrain de changer de background
	private boolean eraseExist = false; //indique s'il y eu au moins un effacement (gommage)
	private int mPointerTaille = 10;
	private String mPointerType = "CIRCLE";
	private Color mCouleurPointer = Color.black;
	private Color mCouleurBackground = Color.white;
	private ArrayList<JPoint> mEnsembleJPoint = new ArrayList<JPoint>();
	private ArrayList<JPoint> mEraser = new ArrayList<JPoint>(); // liste des points effacés
	private boolean drawingForme = false;
	private String results;

	private int formeAbs = 0;
	private int formeOrd = 0;
	
	public ZoneDessin()
	{
		this.setPreferredSize(new Dimension(600, 400));
		
		this.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent souris) 
			{
				mEnsembleJPoint.add(new JPoint(souris.getX() - (mPointerTaille/2),
						souris.getY() - (mPointerTaille/2), mPointerTaille,
						mPointerType,mCouleurPointer));
				repaint();
			}
		});

		this.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseMoved(MouseEvent souris)
			{

			}

			@Override
			public void mouseDragged(MouseEvent souris)
			{
				if(!mEraseMode)
				{
					mEnsembleJPoint.add(new JPoint(souris.getX() - (mPointerTaille/2),
							souris.getY() - (mPointerTaille/2), mPointerTaille,
							mPointerType,mCouleurPointer));
				}
				else
				{
					eraseExist = true;
					mEraser.add(new JPoint(souris.getX() - (mPointerTaille/2),
							souris.getY() - (mPointerTaille/2), mPointerTaille,
							mPointerType,mCouleurBackground));
				}
				repaint();
			}
		});
	}
	
	public void savePaint() throws IOException {

        String name = JOptionPane.showInputDialog(null, "Veuillez donner un nom à votre oeuvre : ", "Titre du dessin", JOptionPane.QUESTION_MESSAGE);
        BufferedImage bufferedImage = new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_BYTE_INDEXED);
        this.paintComponent(bufferedImage.getGraphics());

        RenderedImage rendImage = bufferedImage;

        ImageIO.write(rendImage, "jpg", new File("draw/"+name+".png"));
        
	    	// Instantiates a client
	    	try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

	    	// The path to the image file to annotate
	    	String fileName = "draw/"+name+".png";
	    this.results="Pas de résultats";

	   // Reads the image file into memory
	   Path path = Paths.get(fileName);
	   byte[] data = Files.readAllBytes(path);
	   ByteString imgBytes = ByteString.copyFrom(data);
	
	   // Builds the image annotation request
	   List<AnnotateImageRequest> requests = new ArrayList<>();
	   Image img = Image.newBuilder().setContent(imgBytes).build();
	   Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
	   AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
	       .addFeatures(feat)
	       .setImage(img)
	       .build();
	   requests.add(request);
	
	   // Performs label detection on the image file
	   BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
	   List<AnnotateImageResponse> responses = response.getResponsesList();
	   
	   for (AnnotateImageResponse res : responses) {
	     if (res.hasError()) {
	       System.out.printf("Error: %s\n", res.getError().getMessage());
	       return;
	     }
	this.results="Résultat de l'analyse:\n";
	 for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
	   annotation.getAllFields().forEach((k, v) -> {
		   if(k.toString().contains("description")) {
			   this.results+="- "+v.toString()+" avec un score de ";
		   }
		   else if(k.toString().contains("score")) {
			   this.results+=v.toString()+"\n";
		   }
	   }
	   );
	     }
	   }
	   
	   JOptionPane.showMessageDialog(null, this.results, "Résultats", JOptionPane.INFORMATION_MESSAGE);
    	 }
    	}

	public void drawFormeCarre(int typeForme)
	{
		if(typeForme == 1)
		{
			drawingForme = true;
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics graph)
	{
		graph.setColor(mCouleurBackground);
		graph.fillRect(0, 0, this.getWidth(), this.getHeight());

		if(this.mClear)
		{
			this.mClear = false;
		}
		else
		{
			if(bgChanging && eraseExist)
			{
				for(JPoint p : this.mEnsembleJPoint)
				{
					graph.setColor(p.getmCouleurPoint());

					if(p.getmTypePoint().equals("SQUARE"))
					{
						graph.fillRect(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
					else
					{
						graph.fillOval(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
				}

				for(JPoint p : this.mEraser)
				{
					p.setmCouleurPoint(mCouleurBackground);
					graph.setColor(p.getmCouleurPoint());

					if(p.getmTypePoint().equals("SQUARE"))
					{
						graph.fillRect(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
					else
					{
						graph.fillOval(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
				}
				//mEraseMode = false;
			}

			if(!mEraseMode)
			{
				for(JPoint p : this.mEnsembleJPoint)
				{
					graph.setColor(p.getmCouleurPoint());

					if(p.getmTypePoint().equals("SQUARE"))
					{
						graph.fillRect(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
					else
					{
						graph.fillOval(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
				}

				for(JPoint p : this.mEraser)
				{
					p.setmCouleurPoint(mCouleurBackground);
					graph.setColor(p.getmCouleurPoint());

					if(p.getmTypePoint().equals("SQUARE"))
					{
						graph.fillRect(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
					else
					{
						graph.fillOval(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
				}
			}
			else if(drawingForme)
			{
				graph.setColor(Color.green);
				graph.fillRect(10, 10, this.formeAbs, this.formeOrd);
			}
			else
			{
				for(JPoint p : this.mEnsembleJPoint)
				{
					graph.setColor(p.getmCouleurPoint());

					if(p.getmTypePoint().equals("SQUARE"))
					{
						graph.fillRect(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
					else
					{
						graph.fillOval(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
				}

				for(JPoint p : this.mEraser)
				{
					graph.setColor(p.getmCouleurPoint());

					if(p.getmTypePoint().equals("SQUARE"))
					{
						graph.fillRect(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
					else
					{
						graph.fillOval(p.getmAbs(), p.getmOrd(), p.getmTaillePoint(),
								p.getmTaillePoint());
					}
				}
			}
		}
	}


	/**
	 * @return the formeAbs
	 */
	public int getFormeAbs()
	{
		return formeAbs;
	}

	/**
	 * @return the formeOrd
	 */
	public int getFormeOrd()
	{
		return formeOrd;
	}

	/**
	 * @param formeAbs the formeAbs to set
	 */
	public void setFormeAbs(int formeAbs)
	{
		this.formeAbs = formeAbs;
	}

	/**
	 * @param formeOrd the formeOrd to set
	 */
	public void setFormeOrd(int formeOrd)
	{
		this.formeOrd = formeOrd;
	}


	/**
	 * @return the mEraseMode
	 */
	public boolean ismEraseMode()
	{
		return mEraseMode;
	}

	/**
	 * @param mEraseMode the mEraseMode to set
	 */
	public void setmEraseMode(boolean mEraseMode)
	{
		this.mEraseMode = mEraseMode;
	}

	public void clear()
	{
		this.mClear = true;
		this.mEnsembleJPoint = new ArrayList<JPoint>();
		repaint();
	}

	/**
	 * @param mPointerType the mPointerType to set
	 */
	public void setmPointerType(String mPointerType)
	{
		this.mPointerType = mPointerType;
	}

	/**
	 * @param mCouleurPointer the mCouleurPointer to set
	 */
	public void setmCouleurPointer(Color mCouleurPointer)
	{
		this.mCouleurPointer = mCouleurPointer;
	}

	/**
	 * @return the mPointerTaille
	 */
	public int getmPointerTaille()
	{
		return mPointerTaille;
	}

	/**
	 * @param mPointerTaille the mPointerTaille to set
	 */
	public void setmPointerTaille(int mPointerTaille)
	{
		this.mPointerTaille = mPointerTaille;
	}

	/**
	 * @return the mCouleurBackground
	 */
	public Color getmCouleurBackground()
	{
		return mCouleurBackground;
	}

	/**
	 * @param mCouleurBackground the mCouleurBackground to set
	 */
	public void setmCouleurBackground(Color mCouleurBackground)
	{
		this.mCouleurBackground = mCouleurBackground;
	}

	/**
	 * @return the eraseExist
	 */
	public boolean isEraseExist()
	{
		return eraseExist;
	}

	/**
	 * @param eraseExist the eraseExist to set
	 */
	public void setEraseExist(boolean eraseExist)
	{
		this.eraseExist = eraseExist;
	}

	/**
	 * @return the bgChanging
	 */
	public boolean isBgChanging()
	{
		return bgChanging;
	}

	/**
	 * @param bgChanging the bgChanging to set
	 */
	public void setBgChanging(boolean bgChanging)
	{
		this.bgChanging = bgChanging;
	}

	public ArrayList<JPoint> getmEnsembleJPoint()
    {
        return this.mEnsembleJPoint;
    }
}
