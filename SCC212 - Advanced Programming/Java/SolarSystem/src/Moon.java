public class Moon
{
	// enum for color
	enum moonColor
	{
		BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW
	};

	// instance vars
	Point position;
	double currentAngle;
	int diamiter;
	moonColor color;
	double roationSpeed;

	// constructor
	Moon(Point position, int diamiter, moonColor color, double roationSpeed)
	{
		this.currentAngle = position.angle;
		this.position = position;
		this.diamiter = diamiter;
		this.color = color;
		this.roationSpeed = roationSpeed;
	}

	// methods
	void moveMoon()
	{
		if (this.position.angle < 360)
			this.position.angle = this.position.angle + roationSpeed;
		else
			this.position.angle = 0;
	}

	String getMoonColor()
	{
		return this.color.toString();
	}

}