package foxOnRails.geometry;

import static org.lwjgl.opengl.GL11.*;

import foxOnRails.engine.Vector3f;
import foxOnRails.gameContent.Colors;

public class PolyBullet
{	
	private static final float BULLET_SPEED = 1000f;
	private static final float BULLET_RANGE = -1000f;
	private static final float RESET_Z_POS = 100f;
	
	private Vector3f position;
	private boolean isActive;

	public PolyBullet(float size) {		
		isActive = false;
		position = new Vector3f(0.0f, 5.0f, 0.0f);
	}

	public void update(float deltaTime) {
		if (isActive && position.z > BULLET_RANGE) {
			position.z -= BULLET_SPEED * deltaTime; 
		}
		else {
			position.z = RESET_Z_POS;
			isActive = false;
		}
	}

	public void setIsActive(Vector3f startPos) {
		position.set(startPos);
		this.isActive = true;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void render(){
		glPushMatrix(); 
		glTranslatef(position.x, position.y, position.z); // Apply translation for the first object
		
		glLineWidth(5f);
		glBegin(GL_LINE_STRIP);
		glColor4f(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2], Colors.WHITE[3]);
			glVertex3f( 0, 0, 0);
			glColor4f(Colors.YELLOW[0], Colors.YELLOW[1], Colors.YELLOW[2], Colors.YELLOW[3]);
			glVertex3f( 0, 0, 1);
			glColor4f(Colors.PINK[0], Colors.PINK[1], Colors.PINK[2], Colors.TRANSPARENT[3]);
			glVertex3f( 0, 0, 10);
		glEnd();

		glPopMatrix(); // Restore the previous matrix
	}
	
}
