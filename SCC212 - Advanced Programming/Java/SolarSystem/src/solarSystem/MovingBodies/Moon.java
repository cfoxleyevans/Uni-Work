package solarSystem.MovingBodies;

import solarSystem.Point;
import solarSystem.SolarSystem;

public class Moon extends MovingPlanetaryBody
{
	// instance vars
	Planet myPlanet;
	
	// constructor
	public Moon(Point position, int diameter, double rotationSpeed, Planet p)
	{
		super(position, diameter, PlanetaryBodyColors.WHITE, rotationSpeed);
		this.myPlanet = p;
	}
	
	//methods 
	//draw planets and move them for next call
	public void drawPlanetaryBody(SolarSystem x)
	{
		// draw the planet to the solar system
		x.drawSolarObjectAbout(myPlanet.position.getDistance(), myPlanet.position.getAngle(),
				this.diameter, this.getPlanetaryBodyColor(),
				this.position.getDistance(), this.position.getAngle());

		// update the planet angle
		this.movePlanetaryBody();
	}
}
