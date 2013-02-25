public class Planet
{
	// enum for color
	enum planetColor
	{
		BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW
	};

	// instance vars
	Point position;
	double currentAngle;
	int diamiter;
	planetColor color;
	double roationSpeed;

	// constructor
	Planet(Point position, int diamiter, planetColor color, double roationSpeed)
	{
		this.currentAngle = position.angle;
		this.position = position;
		this.diamiter = diamiter;
		this.color = color;
		this.roationSpeed = roationSpeed;
	}

	// methods
	void movePlanet()
	{
		if (this.position.angle < 360)
			this.position.angle = this.position.angle + roationSpeed;
		else
			this.position.angle = 0;
	}

	String getPlanetColor()
	{
		return this.color.toString();
	}
}
