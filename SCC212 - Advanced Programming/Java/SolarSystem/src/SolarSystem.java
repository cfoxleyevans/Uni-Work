import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

/**
 * This class provides a graphical user interface to a model of the solar system
 * @author Joe Finney
 */

public class SolarSystem extends JFrame 
{
	private int width = 300;
	private int height = 300;
	private boolean exiting = false;

	private Vector<SolarObject> things = new Vector<SolarObject>();

	/**
	 * Create a view of the Solar System.
	 * Once an instance of the SolarSystem class is created,
	 * a window of the appropriate size is displayed, and
	 * objects can be displayed in the solar system
	 *
	 * @param width the width of the window in pixels.
	 * @param height the height of the window in pixels.
	 */
	public SolarSystem(int width, int height)
	{
		this.width = width;
		this.height = height;

		this.setTitle("The Solar System");
		this.setSize(width, height);
		this.setBackground(Color.BLACK);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);		
	}

	/**
	 * A method called by the operating system to draw onto the screen - <p><B>YOU DO NOT (AND SHOULD NOT) NEED TO CALL THIS METHOD.</b></p>
	 */
	public void paint (Graphics gr)
	{
		BufferedImage i = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = i.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		synchronized (this)
		{
			if (!this.exiting)
			{
				g.clearRect(0,0,width,height);
				for(SolarObject t : things)
				{
					g.setColor(t.col);
					g.fillOval(t.x, t.y, t.diameter, t.diameter);

					try{ Thread.sleep(0); } catch (Exception e) {} }
			}
			
			gr.drawImage(i, 0, 0, this);
		}
	}

	//
	// Shouldn't really handle colour this way, but the student's haven't been introduced
	// to constants properly yet, and Color.getColor() doesn't seem to work... hmmm....
	// 
	private Color getColourFromString(String col)
	{
		Color colour = Color.WHITE;
		col = col.toUpperCase();

		if (col.equals("BLACK"))
			colour = Color.BLACK;	

		if (col.equals("BLUE"))
			colour = Color.BLUE;	

		if (col.equals("CYAN"))
			colour = Color.CYAN;	

		if (col.equals("DARKGREY"))
			colour = Color.DARK_GRAY;	

		if (col.equals("GREY"))
			colour = Color.GRAY;	

		if (col.equals("GREEN"))
			colour = Color.GREEN;	

		if (col.equals("LIGHTGREY"))
			colour = Color.LIGHT_GRAY;	
				
		if (col.equals("MAGENTA"))
			colour = Color.MAGENTA;	

		if (col.equals("ORANGE"))
			colour = Color.ORANGE;	

		if (col.equals("PINK"))
			colour = Color.PINK;	

		if (col.equals("RED"))
			colour = Color.RED;	
		
		if (col.equals("WHITE"))
			colour = Color.WHITE;	

		if (col.equals("YELLOW"))
			colour = Color.YELLOW;	

		return colour;
	}
	
	/**
	 * Draws a round shape in the window at the given co-ordinates that represents an object in the solar system.
	 * The SolarSystem class uses <i>Polar Co-ordinates</i> to represent the location
	 * of objects in the solar system.
	 *
	 * @param distance the distance from the sun to the object.
	 * @param angle the angle (in degrees) that represents how far the planet is around its orbit of the sun.
	 * @param diameter the size of the object.
	 * @param col the colour of this object, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW</p>
	 */
	public void drawSolarObject(double distance, double angle, double diameter, String col)
	{
		Color colour = this.getColourFromString(col);
		double centreOfRotationX = ((double) width) / 2.0; 
		double centreOfRotationY = ((double) height) / 2.0; 

		double rads = Math.toRadians(angle);
		double x = (int) (centreOfRotationX + distance * Math.sin(rads)) - diameter / 2;
		double y = (int) (centreOfRotationY + distance * Math.cos(rads)) - diameter / 2;

		synchronized (this)
		{
			if (things.size() > 1000)
			{
				System.out.println("\n\n");
				System.out.println(" ********************************************************* ");
				System.out.println(" ***** Only 1000 Entities Supported per Solar System ***** ");
				System.out.println(" ********************************************************* ");
				System.out.println("\n\n");
				System.out.println("If you are't trying to add this many things");
				System.out.println("to your SolarSystem, then you have probably");
				System.out.println("forgotten to call the finishedDrawing() method");
				System.out.println("See the JavaDOC documentation for more information");
				System.out.println("\n-- Joe");
				System.out.println("\n\n");

				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			else
			{
				SolarObject t = new SolarObject((int)x, (int)y, (int)diameter, colour);
				things.add(t);
			}
		}
	}

	/**
	 * Draws a round shape in the window at the given co-ordinates. 
	 * The SolarSystem class uses <i>Polar Co-ordinates</i> to represent the location
	 * of objects in the solar system. This method operates in the same fashion as drawSolarObject, but
	 * provides additional co-ordinates to allow the programmer to use an arbitrary point about which
	 * the object orbits (e.g. a planet rather than the sun).
	 *
	 * @param distance the distance from this object to the point about which it is orbiting.
	 * @param angle the angle (in degrees) that represents how far the object is around its orbit.
	 * @param diameter the size of the object.
	 * @param col the colour of this object, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW</p>
	 * @param centreOfRotationDistance the distance part of the polar co-ordinate about which this object orbits.
	 * @param centreOfRotationAngle the angular part of the polar co-ordinate about which this object orbits.
	 */
	public void drawSolarObjectAbout(double distance, double angle, double diameter, String col, double centreOfRotationDistance, double centreOfRotationAngle)
	{
		Color colour = this.getColourFromString(col);
		double centrerads = Math.toRadians(centreOfRotationAngle);
		double centreOfRotationX = (((double) width) / 2.0) + centreOfRotationDistance * Math.sin(centrerads); 
		double centreOfRotationY = (((double) height) / 2.0) + centreOfRotationDistance * Math.cos(centrerads); 

		double rads = Math.toRadians(angle);
		double x = (int) (centreOfRotationX + distance * Math.sin(rads)) - diameter / 2;
		double y = (int) (centreOfRotationY + distance * Math.cos(rads)) - diameter / 2;

		synchronized (this)
		{
			if (things.size() > 1000)
			{
				System.out.println("\n\n");
				System.out.println(" ********************************************************* ");
				System.out.println(" ***** Only 1000 Entities Supported per Solar System ***** ");
				System.out.println(" ********************************************************* ");
				System.out.println("\n\n");
				System.out.println("If you are't trying to add this many things");
				System.out.println("to your SolarSystem, then you have probably");
				System.out.println("forgotten to call the finishedDrawing() method");
				System.out.println("See the JavaDOC documentation for more information");
				System.out.println("\n-- Joe");
				System.out.println("\n\n");

				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			else
			{
				SolarObject t = new SolarObject((int)x, (int)y, (int)diameter, colour);
				things.add(t);
			}
		}
	}

	/**
	 * Makes all objects drawn recently drawn to be made visible on the screen.
	 *  
	 * Once called, all suns, planets and moons are displayed in the window.
	 */
	public void finishedDrawing()
	{
		try
		{
			this.repaint();
			Thread.sleep(30);
		}
		catch (Exception e) { }

		synchronized (this)
		{
			things.clear();
		}
	}
	
	private class SolarObject 
	{
		public int x;
		public int y;
		public int diameter;
		public Color col;

		public SolarObject(int x, int y, int diameter, Color col)
		{
			this.x = x;
			this.y = y;
			this.diameter = diameter;
			this.col = col;
		}
	}
}
