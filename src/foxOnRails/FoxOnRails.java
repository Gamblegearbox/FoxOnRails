package foxOnRails;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;

import foxOnRails.gameContent.Colors;
import foxOnRails.geometry.Level;
import foxOnRails.geometry.PlayerShip;
import foxOnRails.geometry.PolyBullet;
import foxOnRails.input.Keyboard;
import foxOnRails.interfaces.Game;
import foxOnRails.utils.Settings;

public class FoxOnRails implements Game 
{
	private static final FloatBuffer LIGHT_AMBIENT = BufferUtils.createFloatBuffer(4); // TODO: use for day night
	private static final FloatBuffer LIGHT_DIFFUSE = BufferUtils.createFloatBuffer(4); // TODO: use for day night
	
	// GAMEOBJECTS
	private PolyBullet[] bullets;
	private PlayerShip playerShip;
	private Level levelStatic;
	
	// CONTROLS
	private boolean wireframe = false;
	private boolean isIsometric = true;
	private float ambient = 0;
	
	@Override
	public void init() {
		printVersionInfo();
		
		initOpenGL();
		initGameObjects();
	}

	private void initOpenGL() {
        glEnable(GL_CULL_FACE);
        
		glEnable(GL_LINE_SMOOTH);		
        glEnable(GL_POINT_SMOOTH);
        
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		initGlobalMaterial();
		initLight();
	}

	private void initLight() {
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		
		FloatBuffer lightPos = BufferUtils.createFloatBuffer(4); 
		lightPos.put(0).put(1000).put(200).put(1).flip();
		glLightfv(GL_LIGHT0, GL_POSITION, lightPos);
		
		LIGHT_DIFFUSE.put(1).put(1).put(1).put(1).flip();
		glLightfv(GL_LIGHT0, GL_DIFFUSE, LIGHT_DIFFUSE);

		LIGHT_AMBIENT.put(ambient).put(ambient).put(ambient).put(ambient).flip();
		glLightfv(GL_LIGHT0, GL_AMBIENT, LIGHT_AMBIENT);
	}

	private void initGlobalMaterial() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
	}

	private void initGameObjects() {
		playerShip = new PlayerShip();
		levelStatic = new Level();
		bullets = new PolyBullet[1000];
		
		for (int i = 0; i < bullets.length; i++) {
			bullets[i] = new PolyBullet(10f);
		}
	}

	@Override
	public void update(float deltaTime) {		
		if(Keyboard.isKeyPressedWithReset(GLFW.GLFW_KEY_TAB))	{ wireframe = !wireframe; }
		if(Keyboard.isKeyPressedWithReset(GLFW.GLFW_KEY_ENTER)) { isIsometric = !isIsometric; }
		playerShip.update(deltaTime);
		if (playerShip.isShooting) {
			shoot(playerShip.getPosition());
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].update(deltaTime);
		}

	}

	@Override
	public void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_BLEND);
		
		if (wireframe) {
			glDisable(GL_LIGHTING);
			glLineWidth(1f);
			glPointSize(3.5f);
			glPolygonMode( GL_FRONT, GL_LINE );
		}
		else {
			glEnable(GL_LIGHTING);
			glPolygonMode( GL_FRONT, GL_FILL );
		}
		
		setOtho();
		levelStatic.renderSky();

		if (isIsometric) {
			setIsometric();	
		}
		else {
			setPerspective();	
		}

		playerShip.render();

		glDisable(GL_LIGHTING);
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].render();
		}

		levelStatic.renderGameVolume(isIsometric);
		
		// HUD -------------------------------------------------------
		setOtho();
		
		glDisable(GL_BLEND);
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glBegin(GL_QUADS);
			glColor3f(Colors.D_BLUE[0], Colors.D_BLUE[1], Colors.D_BLUE[2]);
			glVertex3f( 20, 20, -1);
			glVertex3f( 100, 20, -1);
			glVertex3f( 100, 100, -1);
			glVertex3f( 20, 100, -1);
		glEnd();

		glBegin(GL_QUADS);
			glColor3f(Colors.L_BLUE[0], Colors.L_BLUE[1], Colors.L_BLUE[2]);
			glVertex3f( 30, 30, -1);
			glVertex3f( 90, 30, -1);
			glVertex3f( 90, 90, -1);
			glVertex3f( 30, 90, -1);
		glEnd();
		
		System.out.println("FPS: " + Settings.fps);
	}

	private void setOtho() {
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
		glOrtho (0.0f, Settings.gameRes[0], 0.0f, Settings.gameRes[1], Settings.near, Settings.far);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	private void setIsometric() {
		glEnable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
		glOrtho (-100f, 100f, -100f, 100f, -1000f, 1000f);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glRotatef( 45f, 1, 0, 0);
		glRotatef(-45f, 0, 1, 0);
		glTranslatef(0, 25, 100);
	}

	private void setPerspective(){
		glEnable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
		glFrustum(-Settings.getTop() * Settings.getAspectRatio(), Settings.getTop() * Settings.getAspectRatio(), -Settings.getTop(), Settings.getTop(), Settings.near, Settings.far);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glRotatef( 5, 1, 0, 0);
		glRotatef(-15f, 0, 1, 0);
		glTranslatef( -20, -10, -50);
	}
			
	private void printVersionInfo() {
		System.out.println("GPU Vendor: " + glGetString(GL_VENDOR));
		System.out.println("OpenGL version: " + glGetString(GL_VERSION));
		System.out.println("GLSL version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
	}
	
	private void shoot(Vector3f startPos) {
		for (int i = 0; i < bullets.length; i++) {
			
			if (bullets[i].getIsActive() == false) {
				bullets[i].setIsActive(startPos);
				return;
			}
		}
	}
}
