package foxOnRails.gameContent;

import org.lwjgl.util.vector.Matrix4f;
import foxOnRails.geometry.Rectangle;

public class HeadsUpDisplay
{
	private Matrix4f modelMatrix = new Matrix4f();
	
	private static final int STANDARD_POSITION_X = 0;
	private static final int STANDARD_POSITION_Y = 0;
	
	private int position_x;
	private int position_y;
	private Rectangle background;
	
	public HeadsUpDisplay() {
		position_x = STANDARD_POSITION_X;
		position_y = STANDARD_POSITION_Y;
	}
	
	public HeadsUpDisplay(int x, int y, String font) {
		position_x = x;
		position_y = y;

		float[][] bgColors = new float[][] {
			Colors.WHITE,
			Colors.L_BLUE,
			Colors.L_GREEN,
			Colors.YELLOW
		};
		this.background = new Rectangle(position_x, position_y, 0, 200, 40, bgColors, true);
	}
	
	public void update(String text) {
	}
	
	public Rectangle getBackgroundMesh() {
		return background;
	}
	
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}
}