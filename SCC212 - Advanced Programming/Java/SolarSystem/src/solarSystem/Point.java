package solarSystem;

public class Point
{
	// instance vars
	double distance;
	double angle;

	// constructor
	public Point(double distance, double angle)
	{
		this.distance = distance;
		this.angle = angle;
	}

	//get/set
	public double getDistance()
	{
		return distance;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public double getAngle()
	{
		return angle;
	}

	public void setAngle(double angle)
	{
		this.angle = angle;
	}

	// methods
}