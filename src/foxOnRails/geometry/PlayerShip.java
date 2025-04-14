package foxOnRails.geometry;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import foxOnRails.engine.Vector3f;
import foxOnRails.gameContent.Colors;
import foxOnRails.input.Keyboard;

public class PlayerShip
{	
	private static final float SPEED = 150.0f;
	private static final float ROLL_SPEED = 250f;
	private static final float ROLL_MAX = 30;
	private static final float GUN_COOLDOWN = 0.1f;
	public static final float X_POS_MAX = 50;
	public static final float Y_POS_MAX = 50;
	private static final float ANIM_SPEED = 1000f;

	private static final float[] SHIP_NOSE  = new float[] { 0.00f, -0.96f, -11.32f };
	private static final float[] SHIP_SIDE  = new float[] { 1.37f, -0.40f,  -1.89f };
	private static final float[] SHIP_TOP   = new float[] { 0.00f,  1.08f,   0.11f };

	private static final float[] SLIDE_NOSE = new float[] { 0.00f, -0.92f,  -8.85f };
	private static final float[] SLIDE_SIDE = new float[] { 0.69f, -0.52f,  -1.89f };
	private static final float[] SLIDE_REAR = new float[] { 0.00f, -1.22f,   3.45f };

	private static final float[] WING_TIP   = new float[] { 8.90f, -0.59f,  -0.04f };
	private static final float[] WING_FRONT = new float[] { 1.41f, -0.38f,  -1.56f };
	private static final float[] WING_REAR  = new float[] { 2.29f,  0.27f,   1.96f };
	private static final float[] WING_TOP   = new float[] { 0.28f,  0.80f,  -0.04f };

	private static final float[][] muzzleColors = new float[][] { Colors.YELLOW, Colors.WHITE, Colors.YELLOW };

	private Vector3f position;
	private float gunCooldown = GUN_COOLDOWN;
	private float roll = 0f;
	private float anim = 0f;
	private float absSinForAnim;
	private int muzzleColorIndex = 0;

	public boolean isShooting = false;

	public PlayerShip() {		
		position = new Vector3f(0.0f, 0.0f, -25.0f);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void update(float deltaTime) {
		isShooting = false;
		gunCooldown -= deltaTime;

		muzzleColorIndex += 2;
		muzzleColorIndex %= muzzleColors.length;

		anim += ANIM_SPEED * deltaTime;
		absSinForAnim = (float)Math.abs(Math.sin(anim));

		if(Keyboard.isKeyPressed(GLFW_KEY_A)){
			position.x -= SPEED * deltaTime;
			if ( position.x <= -X_POS_MAX ) { position.x = -X_POS_MAX; }

			roll += ROLL_SPEED * deltaTime;
			if (roll > ROLL_MAX) { roll = ROLL_MAX; }
		}
		else {
			if (roll > 0) {
				roll -= ROLL_SPEED * deltaTime;
				if (roll <= 0) {
					roll = 0;
				}
			}
		}

		if(Keyboard.isKeyPressed(GLFW_KEY_D)) {
			position.x += (SPEED * deltaTime);
			if ( position.x >= X_POS_MAX ) { position.x = X_POS_MAX; }

			roll -= ROLL_SPEED * deltaTime;
			if (roll < -ROLL_MAX) {	roll = -ROLL_MAX; }
		}
		else {
			if (roll < 0) {
				roll += ROLL_SPEED * deltaTime;
				if (roll >= 0) {
					roll = 0;
				}
			}
		}

		if(Keyboard.isKeyPressed(GLFW_KEY_W)) {
			position.y += (SPEED * deltaTime);
			if ( position.y >= Y_POS_MAX ) { position.y = Y_POS_MAX; }
		}
		
		if(Keyboard.isKeyPressed(GLFW_KEY_S)) {
			position.y -= (SPEED * deltaTime);
			if ( position.y <= -Y_POS_MAX ) { position.y = -Y_POS_MAX; }
		}

		if(Keyboard.isKeyPressed(GLFW_KEY_SPACE)){ 
			if (gunCooldown <= 0.0f) {
				isShooting = true;
				gunCooldown = GUN_COOLDOWN;
			}
		}
	}

	public void render(){
		glEnable(GL_LIGHTING);
		glPushMatrix(); // Save the current matrix
		glTranslatef(position.x, position.y, position.z); // Apply translation for the first object
		glRotatef(roll, 0,0,1);

		glColor4f(Colors.L_GREY[0], Colors.L_GREY[1], Colors.L_GREY[2], Colors.L_GREY[3]);

		glBegin(GL_TRIANGLES);
		// ship_right
		glNormal3f(0.6290f, 0.7653f, -0.1367f);
		glVertex3f(SHIP_NOSE[0], SHIP_NOSE[1], SHIP_NOSE[2]);
		glVertex3f(SHIP_TOP[0], SHIP_TOP[1], SHIP_TOP[2]);
		glVertex3f(SHIP_SIDE[0], SHIP_SIDE[1], SHIP_SIDE[2]);

		// ship_left
		glNormal3f(-0.6290f, 0.7653f, -0.1367f);
		glVertex3f(SHIP_NOSE[0], SHIP_NOSE[1], SHIP_NOSE[2]);
		glVertex3f(-SHIP_SIDE[0], SHIP_SIDE[1], SHIP_SIDE[2]);
		glVertex3f(SHIP_TOP[0], SHIP_TOP[1], SHIP_TOP[2]);

		// ship_bottom
		glNormal3f(0.0f, -0.9982f, 0.0592f);
		glVertex3f(SHIP_NOSE[0], SHIP_NOSE[1], SHIP_NOSE[2]);
		glVertex3f(SHIP_SIDE[0], SHIP_SIDE[1], SHIP_SIDE[2]);
		glVertex3f(-SHIP_SIDE[0], SHIP_SIDE[1], SHIP_SIDE[2]);

		glColor4f(Colors.D_BLUE[0], Colors.D_BLUE[1], Colors.D_BLUE[2], Colors.D_BLUE[3]);
	
		// ship_rear
		glNormal3f(0.0f, -0.8035f, 0.5953f);
		glVertex3f(SHIP_TOP[0], SHIP_TOP[1], SHIP_TOP[2]);
		glVertex3f(-SHIP_SIDE[0], SHIP_SIDE[1], SHIP_SIDE[2]);
		glVertex3f(SHIP_SIDE[0], SHIP_SIDE[1], SHIP_SIDE[2]);

		glColor4f(Colors.L_GREY[0], Colors.L_GREY[1], Colors.L_GREY[2], Colors.L_GREY[3]);

		// slide_right
		glNormal3f(0.6433f, -0.7654f, -0.0186f);
		glVertex3f(SLIDE_NOSE[0], SLIDE_NOSE[1], SLIDE_NOSE[2]);
		glVertex3f(SLIDE_SIDE[0], SLIDE_SIDE[1], SLIDE_SIDE[2]);
		glVertex3f(SLIDE_REAR[0], SLIDE_REAR[1], SLIDE_REAR[2]);

		// slide_left
		glNormal3f(-0.6433f, -0.7654f, -0.0186f);
		glVertex3f(SLIDE_NOSE[0], SLIDE_NOSE[1], SLIDE_NOSE[2]);
		glVertex3f(SLIDE_REAR[0], SLIDE_REAR[1], SLIDE_REAR[2]);
		glVertex3f(-SLIDE_SIDE[0], SLIDE_SIDE[1], SLIDE_SIDE[2]);

		glColor4f(Colors.D_BLUE[0], Colors.D_BLUE[1], Colors.D_BLUE[2], Colors.D_BLUE[3]);

		// slide_rear
		glNormal3f(0.00f, 0.9914f, 0.1311f);
		glVertex3f(SLIDE_REAR[0], SLIDE_REAR[1], SLIDE_REAR[2]);
		glVertex3f(SLIDE_SIDE[0], SLIDE_SIDE[1], SLIDE_SIDE[2]);
		glVertex3f(-SLIDE_SIDE[0], SLIDE_SIDE[1], SLIDE_SIDE[2]);

		glColor4f(Colors.L_GREY[0], Colors.L_GREY[1], Colors.L_GREY[2], Colors.L_GREY[3]);

		// wing_front_r
		glNormal3f(0.1340f, 0.8294f, -0.5424f);
		glVertex3f(WING_TIP[0], WING_TIP[1], WING_TIP[2]);
		glVertex3f(WING_FRONT[0], WING_FRONT[1], WING_FRONT[2]);
		glVertex3f(WING_TOP[0], WING_TOP[1], WING_TOP[2]);

		// wing_front_l
		glNormal3f(-0.1340f, 0.8294f, -0.5424f);
		glVertex3f(-WING_TIP[0], WING_TIP[1], WING_TIP[2]);
		glVertex3f(-WING_TOP[0], WING_TOP[1], WING_TOP[2]);
		glVertex3f(-WING_FRONT[0], WING_FRONT[1], WING_FRONT[2]);

		// wing_rear_r
		glNormal3f(0.1587f, 0.9823f, 0.0998f);
		glVertex3f(WING_TIP[0], WING_TIP[1], WING_TIP[2]);
		glVertex3f(WING_TOP[0], WING_TOP[1], WING_TOP[2]);
		glVertex3f(WING_REAR[0], WING_REAR[1], WING_REAR[2]);

		// wing_rear_l
		glNormal3f(-0.1587f, 0.9823f, 0.0998f);
		glVertex3f(-WING_TIP[0], WING_TIP[1], WING_TIP[2]);
		glVertex3f(-WING_REAR[0], WING_REAR[1], WING_REAR[2]);
		glVertex3f(-WING_TOP[0], WING_TOP[1], WING_TOP[2]);

		// wing_bottom_r
		glNormal3f(-0.0682f, -0.9780f, 0.1971f);
		glVertex3f(WING_TIP[0], WING_TIP[1], WING_TIP[2]);
		glVertex3f(WING_REAR[0], WING_REAR[1], WING_REAR[2]);
		glVertex3f(WING_FRONT[0], WING_FRONT[1], WING_FRONT[2]);
		
		// wing_bottom_l
		glNormal3f(0.0682f, -0.9780f, 0.1971f);
		glVertex3f(-WING_TIP[0], WING_TIP[1], WING_TIP[2]);
		glVertex3f(-WING_FRONT[0], WING_FRONT[1], WING_FRONT[2]);
		glVertex3f(-WING_REAR[0], WING_REAR[1], WING_REAR[2]);

		glColor4f(Colors.D_BLUE[0], Colors.D_BLUE[1], Colors.D_BLUE[2], Colors.D_BLUE[3]);

		// wing_inner_r
		glNormal3f(-0.4913f, -0.8268f, 0.2740f);
		glVertex3f(WING_TOP[0], WING_TOP[1], WING_TOP[2]);
		glVertex3f(WING_FRONT[0], WING_FRONT[1], WING_FRONT[2]);
		glVertex3f(WING_REAR[0], WING_REAR[1], WING_REAR[2]);

		// wing_inner_l
		glNormal3f(0.4913f, -0.8268f, 0.2740f);
		glVertex3f(-WING_TOP[0], WING_TOP[1], WING_TOP[2]);
		glVertex3f(-WING_REAR[0], WING_REAR[1], WING_REAR[2]);
		glVertex3f(-WING_FRONT[0], WING_FRONT[1], WING_FRONT[2]);

		glEnd();
		
		glDisable(GL_LIGHTING);
		
		if(isShooting){
			glPointSize(50.0f * absSinForAnim);
			glBegin(GL_POINTS);
				glColor3f(muzzleColors[muzzleColorIndex][0], muzzleColors[muzzleColorIndex][1], muzzleColors[muzzleColorIndex][2]);
				glVertex3f(SHIP_NOSE[0], SHIP_NOSE[1], SHIP_NOSE[2]);
			glEnd();
		}

		glPointSize(10.0f);
		glBegin(GL_POINTS);		
			glColor3f(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2]);
			glVertex3f(0, 0, 2f);
		glEnd();

		glPointSize(15.0f);
		glBegin(GL_POINTS);
			glColor4f(Colors.L_BLUE[0], Colors.L_BLUE[1], Colors.L_BLUE[2], 0.75f * absSinForAnim);
			glVertex3f(0, 0, 2f);
		glEnd();

		glLineWidth(10.0f);
		glBegin(GL_LINE_STRIP);
			glColor4f(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2], 1.0f);
			glVertex3f(0, 0, 2f);
			glColor4f(Colors.L_BLUE[0], Colors.L_BLUE[1], Colors.L_BLUE[2], Colors.TRANSPARENT[3]);
			glVertex3f(0, 0, 20f * absSinForAnim);
		glEnd();
				
		glPopMatrix();

	}

}
