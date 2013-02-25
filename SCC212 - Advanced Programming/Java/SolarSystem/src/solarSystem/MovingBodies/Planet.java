package solarSystem.MovingBodies;

import solarSystem.Point;
import solarSystem.SolarSystem;
import solarSystem.StationaryBodies.Sun;

public class Planet extends MovingPlanetaryBody
{
	//instance vars
	Sun mySun;

	//constructor
	public Planet(Point position, int diameter, PlanetaryBodyColors color, double rotationSpeed, Sun s)
	{
		super(position, diameter, color, rotationSpeed);
		this.mySun = s;
	}
	
	//methods 
	//draw planets and move them for next call
	public void drawPlanetaryBody(SolarSystem x)
	{
		//draw the planet to the solar system
		x.drawSolarObjectAbout(mySun.position.getDistance(), mySun.position.getAngle(), this.diameter, 
				this.getPlanetaryBodyColor(),this.position.getDistance(), this.position.getAngle());
		
		//update the planet angle
		this.movePlanetaryBody();
	}
}
