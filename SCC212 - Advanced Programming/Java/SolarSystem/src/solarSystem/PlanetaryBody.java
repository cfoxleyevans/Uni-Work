package solarSystem;

import solarSystem.Point;

public abstract class PlanetaryBody
{
	 public static enum PlanetaryBodyColors
	{
		BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW
	};
	
	//instance vars
	public Point position;
	protected int diameter;
	protected PlanetaryBodyColors color;
	
	//constructor

	//methods
	public String getPlanetaryBodyColor()
	{
		return this.color.toString();
	}
}
