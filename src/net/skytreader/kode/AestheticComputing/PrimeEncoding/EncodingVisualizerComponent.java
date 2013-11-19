package net.skytreader.kode.AestheticComputing.PrimeEncoding;

import java.awt.Graphics;
import javax.swing.JComponent;

public abstract class EncodingVisualizerComponent extends JComponent{
	
	public abstract int getEncoderLimit();
	
	public abstract void drawEncoding(Graphics g, int x);
	
	public abstract void drawEncoding(Graphics g, String encoding, int startX, int startY);
	
	public abstract void setDisplay(int x);
	
	public abstract void setXOrigin(int x);
	
	public abstract void setYOrigin(int y);
	
	public void paintComponent(Graphics g){
	}
	
}
