
 /* 
 * Cette classe représente les points tracés et
 * contient les elements relatifs à ce point
 */

package com.jpaint.dessins;

import java.awt.Color;

public class JPoint 
{
	private int mAbs = -100;
	private int mOrd = -100;
	private int mTaillePoint = 10;
	private String mTypePoint = "ROND";
	private Color mCouleurPoint =  Color.red;
	
	public JPoint(int abs, int ord, int taillePoint, String typePoint,
			Color couleurPoint) 
	{
		super();
		this.mAbs = abs;
		this.mOrd = ord;
		this.mTaillePoint = taillePoint;
		this.mTypePoint = typePoint;
		this.mCouleurPoint = couleurPoint;
	}
	
	public JPoint()
	{
		
	}

	/**
	 * @return the mAbs
	 */
	public int getmAbs() 
	{
		return mAbs;
	}

	/**
	 * @return the mOrd
	 */
	public int getmOrd() 
	{
		return mOrd;
	}

	/**
	 * @return the mTaillePoint
	 */
	public int getmTaillePoint() 
	{
		return mTaillePoint;
	}

	/**
	 * @return the mTypePoint
	 */
	public String getmTypePoint() 
	{
		return mTypePoint;
	}

	/**
	 * @return the mCouleurPoint
	 */
	public Color getmCouleurPoint() 
	{
		return mCouleurPoint;
	}

	/**
	 * @param mAbs the mAbs to set
	 */
	public void setmAbs(int mAbs) 
	{
		this.mAbs = mAbs;
	}

	/**
	 * @param mOrd the mOrd to set
	 */
	public void setmOrd(int mOrd) 
	{
		this.mOrd = mOrd;
	}

	/**
	 * @param mTaillePoint the mTaillePoint to set
	 */
	public void setmTaillePoint(int mTaillePoint) 
	{
		this.mTaillePoint = mTaillePoint;
	}

	/**
	 * @param mTypePoint the mTypePoint to set
	 */
	public void setmTypePoint(String mTypePoint) 
	{
		this.mTypePoint = mTypePoint;
	}

	/**
	 * @param mCouleurPoint the mCouleurPoint to set
	 */
	public void setmCouleurPoint(Color mCouleurPoint) 
	{
		this.mCouleurPoint = mCouleurPoint;
	}
		
}
