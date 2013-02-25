package solarSystem;

import java.util.Vector;

import solarSystem.Point;
import solarSystem.PlanetaryBody.PlanetaryBodyColors;
import solarSystem.MovingBodies.Asteroid;
import solarSystem.MovingBodies.MovingPlanetaryBody;
import solarSystem.MovingBodies.Planet;
import solarSystem.MovingBodies.Moon;
import solarSystem.StationaryBodies.Sun;

public class Main
{
	public static void main(String[] args)
	{
		//array to hold various objects
		Vector<MovingPlanetaryBody> stuff = new Vector<MovingPlanetaryBody>();
		
		// create solar system to place all the objects in
		SolarSystem SS = new SolarSystem(1250, 850);
		
		//create sun about which everything moves
		Sun theSun = new Sun(new Point(0,0), 50);
	
		//create planets
		Planet mercuary = new Planet(new Point(40,0), 10, PlanetaryBodyColors.DARK_GRAY, 5, theSun);
		Planet venus = new Planet(new Point(65,0), 20, PlanetaryBodyColors.ORANGE, 4, theSun);
		Planet earth = new Planet(new Point(110,0), 30, PlanetaryBodyColors.BLUE, 3, theSun);
		Planet mars = new Planet(new Point(185,0), 40, PlanetaryBodyColors.RED, 2, theSun);
		Planet jupitor = new Planet(new Point(350,0), 50, PlanetaryBodyColors.PINK, 1, theSun);
		
		//add planets to planets vector
		stuff.add(earth);
		stuff.add(venus);
		stuff.add(mercuary);
		stuff.add(jupitor);
		stuff.add(mars);
	
		//create moons
		Moon earthMoon = new Moon(new Point(25,0), 10, 3, earth);
		Moon marsMoon1 = new Moon(new Point(30,0), 10, 3, mars);
		Moon marsMoon2 = new Moon(new Point(45,0), 10, 2, mars);
		Moon jupitorMoon1 = new Moon(new Point(35,0), 10, 3, jupitor);
		Moon jupitorMoon2 = new Moon(new Point(45,0), 10, 2, jupitor);
		Moon jupitorMoon3 = new Moon(new Point(55,0), 10, 1, jupitor);
		Moon jupitorMoon4 = new Moon(new Point(65,0), 10, .5, jupitor);
		
		//add moons to moons vector
		stuff.add(earthMoon);
		stuff.add(marsMoon1);
		stuff.add(marsMoon2);
		stuff.add(jupitorMoon1);
		stuff.add(jupitorMoon2);
		stuff.add(jupitorMoon3);
		stuff.add(jupitorMoon4);

		//create asteroids []
		Asteroid.createAsteroidBelt(stuff, 250, 290, theSun, 1, 750);
		
		//draw loop
		while (true)
		{
			//draw the sun
			Sun.drawSun(SS, theSun);
			
			//draw stuff
			for(MovingPlanetaryBody i : stuff)
				i.drawPlanetaryBody(SS);
			
			//blit to screen and update solar system
			SS.finishedDrawing();
		}
	}
}