import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SmartSquare extends GameSquare
{
	private boolean thisSquareHasBomb = false;
	public static final int MINE_PROBABILITY = 50;
	private boolean checked = false; 

	public SmartSquare(int x, int y, GameBoard board)
	{
		super(x, y, "images/blank.png", board);

		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
	}
	
	public void clicked()
	{
		int bombsFound = 0;
		checked = true;
		if(thisSquareHasBomb)
		{
			this.setImage("Images/bomb.png");
		}
		else
		{
			for (int i = xLocation - 1; i<= xLocation + 1; i++)
				for (int j = yLocation -1; j <= yLocation + 1; j++)
				{
					SmartSquare current = (SmartSquare) this.board.getSquareAt(i, j);
					if (current != null && current.thisSquareHasBomb)
						bombsFound++;
				}
			this.setImage("Images/" + Integer.toString(bombsFound) + ".png");

			if(bombsFound== 0)
			{
				for(int i = xLocation - 1; i<= xLocation + 1; i++)
				{
					for(int j = yLocation -1; j <= yLocation + 1; j++)
					{
						SmartSquare newSquare = (SmartSquare) board.getSquareAt(i, j);
						if(newSquare != null && !newSquare.checked)
							newSquare.clicked();
					}
				}
			}
		}
	}
}

