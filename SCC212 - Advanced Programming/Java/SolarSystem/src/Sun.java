public class Sun
{
	// enum for color
	enum planetColor
	{
		BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW
	};

	// instance vars
	planetColor color;
	Point position;
	int diamiter;

	// constructor
	Sun(Point position, int diamiter, planetColor color)
	{
		this.position = position;
		this.diamiter = diamiter;
		this.color = color;
	}

	// methods
	String getPlanetColor()
	{
		return this.color.toString();
	}
}
