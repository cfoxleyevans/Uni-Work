public class Section1
{
	public static void main(String[] args)
	{
		// create window
		SolarSystem SS = new SolarSystem(400, 400);

		// track position of earth
		int roatationPosition = 0;

		while (true)
		{
			// draw sun
			SS.drawSolarObject(0, 0, 50, "YELLOW");

			// check for full circle
			if (roatationPosition > 360)
				roatationPosition = 0;

			// draw earth
			SS.drawSolarObjectAbout(0, 0, 20, "BLUE", 80, roatationPosition);

			// rotate further
			roatationPosition++;

			// update screen
			SS.finishedDrawing();
		}
	}
}
