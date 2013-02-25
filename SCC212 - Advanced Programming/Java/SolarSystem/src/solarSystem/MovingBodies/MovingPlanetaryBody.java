package solarSystem.MovingBodies;

import java.util.Vector;

import solarSystem.Point;
import solarSystem.PlanetaryBody;
import solarSystem.SolarSystem;

public class MovingPlanetaryBody extends PlanetaryBody
{
	//instance vars
	private double rotationSpeed;
	
	//constructor
	MovingPlanetaryBody(Point position, int diameter, PlanetaryBodyColors color, double rotationSpeed)
	{
		this.position = position;
		this.diameter = diameter;
		this.color = color;
		this.rotationSpeed = rotationSpeed;
	}
	
	//methods
	public void movePlanetaryBody()
	{
		if (this.position.getAngle() < 360)
			this.position.setAngle(this.position.getAngle() + this.rotationSpeed);
		else
			this.position.setAngle(0);
	}
	
	
	public void drawPlanetaryBody(SolarSystem x)
	{
		//overridden by all subclasses
	}	
}
