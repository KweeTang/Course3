/**
 * A class that represents a maze to navigate through.
 */
package week2example;

/**
 * @author Christine
 *
 */
public class Maze 
{
	private MazeCell[][] cells;
	
	/** Create a new empty Maze */
	public Maze(int width, int height)
	{
		cells = new MazeCell[height][width];
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				cells[r][c] = new MazeCell();
			}
		}
	}
	
	/** Create a new Maze by loading it from a file */
	public Maze(String filename) {
		
		
	}
	
}
