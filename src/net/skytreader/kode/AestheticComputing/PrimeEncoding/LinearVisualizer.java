package net.skytreader.kode.AestheticComputing.PrimeEncoding;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
TODO: Make this code more object-oriented...

or not? Consider speed issues if we'll still make this one
more object-oriented.
*/
public class LinearVisualizer extends EncodingVisualizerComponent{
	private final Color IDENTITY_COLOR = new Color(255, 220, 180);
	private final Color USED_COLOR = Color.BLACK;
	private final Color SKIPPED_COLOR = Color.LIGHT_GRAY;
	
	private final int SQUARE_SIDE = 25;
	private final int DELTA = 5;
	private final int DEFAULT_ORIGIN = 50;
	
	private PrimeEncoding Encoder;
	private boolean isRaised = true;
	private int x_origin;
	private int y_origin;
	private boolean notFirstTime = false;
	private int x = 0;
	
	//TODO: Make an OO abstraction for PrimeEncoding then specify it here!
	//Change the type of Encoder to that abstraction as well.
	public LinearVisualizer(PrimeEncoding Encoder){
		this.Encoder = Encoder;
		x_origin = DEFAULT_ORIGIN;
		y_origin = DEFAULT_ORIGIN;
	}
	
	public LinearVisualizer(int limit){
		this.Encoder = new PrimeEncoding(limit);
		x_origin = DEFAULT_ORIGIN;
		y_origin = DEFAULT_ORIGIN;
	}
	
	public LinearVisualizer(int limit, int x_origin, int y_origin){
		this.Encoder = new PrimeEncoding(limit);
		this.x_origin = x_origin;
		this.y_origin = y_origin;
	}
	
	public void setIsRaised(boolean b){
		isRaised = b;
	}
	
	private void drawSquare(Graphics g, int x, int y){
		g.fill3DRect(x, y, SQUARE_SIDE, SQUARE_SIDE, isRaised);
	}
	
	private void drawEncoding(Graphics g, Color color, int x, int y, int width, int height){
		g.setColor(color);
		g.fill3DRect(x, y, width, height, isRaised);
	}
	
	private void drawIdentity(Graphics g, int x, int y){
		drawEncoding(g, IDENTITY_COLOR, x, y, SQUARE_SIDE, SQUARE_SIDE);
	}
	
	private void drawUsed(Graphics g, int x, int y, int width, int height){
		drawEncoding(g, USED_COLOR, x, y, width, height);
	}
	
	private void drawSkipped(Graphics g, int x, int y, int width, int height){
		drawEncoding(g, SKIPPED_COLOR, x, y, width, height);
	}
	
	public void drawEncoding(Graphics g, String encoding){
		try{
			drawEncoding(g, encoding, x_origin, y_origin);
			if(notFirstTime){
				repaint();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void drawEncoding(Graphics g, int x){
		try{
			drawEncoding(g, Encoder.encode(x), x_origin, y_origin);
			repaint();
		} catch(Exception e){
		}
	}
	
	private int countIndicators(String encoding){
		int i = 0;
		int limit = encoding.length();
		int count = 0;
		
		while(i < limit){
			if(encoding.charAt(i) == EncodingConstants.USED_INDICATOR ||
			   encoding.charAt(i) == EncodingConstants.SKIPPED_INDICATOR){
			   	count++;
			}
			
			i++;
		}
		
		return count;
	}
	
	//TODO: How do I refactor this code?
	public void drawEncoding(Graphics g, String encoding, int startX, int startY){
		int i = 0;
		int limit = encoding.length();
		int x = startX;
		int y = startY;
		
		while(i < limit){
			char thischar = encoding.charAt(i);
			
			if(thischar == EncodingConstants.IDENTITY){
				drawIdentity(g, x, y);
				x += SQUARE_SIDE + DELTA;
				i++;
			} else if(thischar == EncodingConstants.USED_INDICATOR){
				drawUsed(g, x, y, SQUARE_SIDE, SQUARE_SIDE);
				x += SQUARE_SIDE + DELTA;
				i++;
			} else if(thischar == EncodingConstants.SKIPPED_INDICATOR){
				drawSkipped(g, x, y, SQUARE_SIDE, SQUARE_SIDE);
				x += SQUARE_SIDE + DELTA;
				i++;
			} else if(thischar == EncodingConstants.USED_OPENER){
				int encloserLimit = Encoder.getSubcode(encoding, i);
				String subencoding = encoding.substring(i + 1, encloserLimit - 1);
				int lengthMultiplier = countIndicators(subencoding);
				int multipliedWidth = lengthMultiplier * SQUARE_SIDE + ((lengthMultiplier - 1) * DELTA);
				int downTranslation = y + SQUARE_SIDE + DELTA;
				
				drawUsed(g, x, y, multipliedWidth, SQUARE_SIDE);
				drawEncoding(g, subencoding, x, downTranslation);
				
				x += multipliedWidth + DELTA;
				i = encloserLimit;
				
			} else if(thischar == EncodingConstants.SKIPPED_OPENER){
				int encloserLimit = Encoder.getSubcode(encoding, i);
				String subencoding = encoding.substring(i + 1, encloserLimit - 1);
				int lengthMultiplier = countIndicators(subencoding);
				int multipliedWidth = lengthMultiplier * SQUARE_SIDE + ((lengthMultiplier - 1) * DELTA);
				int downTranslation = y + SQUARE_SIDE + DELTA;
				
				drawSkipped(g, x, y, multipliedWidth, SQUARE_SIDE);
				drawEncoding(g, subencoding, x, downTranslation);
				
				x += multipliedWidth + DELTA;
				i = encloserLimit;
			}
		}
	}
	
	public void setDisplay(int x){
		this.x = x;
		repaint();
	}
	
	public void setXOrigin(int x){
		x_origin = x;
	}
	
	public void setYOrigin(int y){
		y_origin = y;
	}
	
	public void paintComponent(Graphics g){
		drawEncoding(g, Encoder.encode(x));
	}
	
	public int getEncoderLimit(){
		return Encoder.getLimit();
	}
}
