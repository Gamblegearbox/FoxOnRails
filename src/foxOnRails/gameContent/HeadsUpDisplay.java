package foxOnRails.gameContent;

import org.lwjgl.util.vector.Matrix4f;
import foxOnRails.geometry.Rectangle2D;

public class HeadsUpDisplay
{
	private Matrix4f modelMatrix = new Matrix4f();
	
	private static final int STANDARD_POSITION_X = 0;
	private static final int STANDARD_POSITION_Y = 0;
	
	private int position_x;
	private int position_y;
	private Rectangle2D background;
	
	public HeadsUpDisplay() {
		position_x = STANDARD_POSITION_X;
		position_y = STANDARD_POSITION_Y;
	}
	
	public HeadsUpDisplay(int x, int y, String font) {
		position_x = x;
		position_y = y;

		float[][] bgColors = new float[][] {
			Colors.WHITE,
			Colors.BLUE,
			Colors.GREEN,
			Colors.YELLOW
		};
		this.background = new Rectangle2D(position_x, position_y, 200, 40, bgColors);
	}
	
	public void update(String text) {
	}
	
	public Rectangle2D getBackgroundMesh() {
		return background;
	}
	
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}
}