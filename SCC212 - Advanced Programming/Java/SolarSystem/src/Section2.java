public class Section2
{
	public static void main(String[] args)
	{
		// create screen
		SolarSystem SS = new SolarSystem(600, 600);

		// create planets
		Sun theSun = new Sun(new Point(0, 0), 50, Sun.planetColor.YELLOW);
		Planet theMercuary = new Planet(new Point(50, 0), 15,
				Planet.planetColor.GREEN, 3);

		Planet theVenus = new Planet(new Point(100, 0), 25,
				Planet.planetColor.ORANGE, 2);

		Planet theEarth = new Planet(new Point(170, 0), 35,
				Planet.planetColor.BLUE, 1);

		Planet theMars = new Planet(new Point(240, 0), 45,
				Planet.planetColor.RED, 0.5);

		// create moons
		Moon earthMoon = new Moon(new Point(0, 0), 10, Moon.moonColor.WHITE,
				1.5);
		Moon marsMoon1 = new Moon(new Point(0, 0), 10, Moon.moonColor.WHITE, 2);
		Moon marsMoon2 = new Moon(new Point(0, 0), 10, Moon.moonColor.WHITE,
				1.3);

		// draw loop
		while (true)
		{
			// draw sun
			SS.drawSolarObject(theSun.position.distance, theSun.position.angle,
					theSun.diamiter, theSun.getPlanetColor());

			// draw planets

			// draw mercuary
			SS.drawSolarObjectAbout(theSun.position.distance,
					theSun.position.angle, theMercuary.diamiter,
					theMercuary.getPlanetColor(),
					theMercuary.position.distance, theMercuary.position.angle);

			// draw venus
			SS.drawSolarObjectAbout(theSun.position.distance,
					theSun.position.angle, theVenus.diamiter,
					theVenus.getPlanetColor(), theVenus.position.distance,
					theVenus.position.angle);

			// draw earth
			SS.drawSolarObjectAbout(theSun.position.distance,
					theSun.position.angle, theEarth.diamiter,
					theEarth.getPlanetColor(), theEarth.position.distance,
					theEarth.position.angle);

			// draw mars
			SS.drawSolarObjectAbout(theSun.position.distance,
					theSun.position.angle, theMars.diamiter,
					theMars.getPlanetColor(), theMars.position.distance,
					theMars.position.angle);

			// draw the moons

			// draw earths moon
			SS.drawSolarObjectAbout(theEarth.position.distance,
					theEarth.position.angle, earthMoon.diamiter,
					earthMoon.getMoonColor(), 25, earthMoon.position.angle);

			// draw first mars moon
			SS.drawSolarObjectAbout(theMars.position.distance,
					theMars.position.angle, marsMoon1.diamiter,
					marsMoon1.getMoonColor(), 35, marsMoon1.position.angle);

			// draw first mars moon
			SS.drawSolarObjectAbout(theMars.position.distance,
					theMars.position.angle, marsMoon2.diamiter,
					marsMoon2.getMoonColor(), 55, marsMoon2.position.angle);

			// move planets
			theMercuary.movePlanet();
			theVenus.movePlanet();
			theEarth.movePlanet();
			theMars.movePlanet();

			// move moons
			earthMoon.moveMoon();
			marsMoon1.moveMoon();
			marsMoon2.moveMoon();

			// update screen
			SS.finishedDrawing();
		}
	}
}
