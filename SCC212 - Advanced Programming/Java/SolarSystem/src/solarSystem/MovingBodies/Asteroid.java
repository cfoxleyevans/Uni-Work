package solarSystem.MovingBodies;

import solarSystem.Point;
import java.util.Random;
import java.util.Vector;

import solarSystem.SolarSystem;
import solarSystem.StationaryBodies.Sun;

public class Asteroid extends MovingPlanetaryBody
{
	// instance vars
	Sun mySun;
	
	// constructor
	public Asteroid(Point position, int diameter, double rotationSpeed, Sun s)
	{
		super(position, diameter, PlanetaryBodyColors.GRAY, rotationSpeed);
		this.mySun = s;
	}
	
	//create asteroid belt
	public static Vector<MovingPlanetaryBody> createAsteroidBelt(Vector<MovingPlanetaryBody> a, int minDistance, int maxDistance, Sun s, double rotationSpeed, int numberOfDroids)
	{
		//to place droids in random pos
		Random r = new Random();
		//generate a.length droids and put them into the belt array
		for (int i = 0; i < numberOfDroids; i++)
		{
			Asteroid tempAsteroid =  new Asteroid(new Point(minDistance + (maxDistance - minDistance) * r.nextDouble(), 0 + (360 - 0) * r.nextDouble()), 
					5, rotationSpeed, s);
			a.add(tempAsteroid);
		}
		//return the populated array
		return a;
	}
	
	//draw asteroids and move asteroid belt
	public void drawPlanetaryBody(SolarSystem x)
	{
		x.drawSolarObjectAbout(mySun.position.getDistance(), mySun.position.getAngle(), this.diameter, 
				this.getPlanetaryBodyColor(),this.position.getDistance(), this.position.getAngle());
		
		//update droid angle
		this.movePlanetaryBody();
	}
}
