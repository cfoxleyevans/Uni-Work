package solarSystem.StationaryBodies;

import solarSystem.Point;
import solarSystem.SolarSystem;

public class Sun extends StationaryPlanetaryBody
{
	// instance vars
	
	
	// constructor
	public Sun(Point position, int diameter)
	{
		super(position, diameter, PlanetaryBodyColors.YELLOW);
	}
	
	//methods
	//draw sun
	public static void drawSun(SolarSystem x, Sun s)
	{
		x.drawSolarObject(s.position.getDistance(), s.position.getAngle(), s.diameter, 
				s.getPlanetaryBodyColor() );
	}

}
