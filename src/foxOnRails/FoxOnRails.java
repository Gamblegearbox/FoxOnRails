package foxOnRails;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.BufferUtils;

import foxOnRails.engine.Camera;
import foxOnRails.engine.CoreEngine;
import foxOnRails.engine.MeshObject;
import foxOnRails.engine.ShaderProgram;
import foxOnRails.gameContent.Colors;
import foxOnRails.gameContent.HeadsUpDisplay;
import foxOnRails.geometry.PlayerShip;
import foxOnRails.geometry.PolyBullet;
import foxOnRails.geometry.Polystrip;
import foxOnRails.geometry.Rectangle;
import foxOnRails.input.Keyboard;
import foxOnRails.interfaces.Game;
import foxOnRails.utils.Info;

public class FoxOnRails implements Game 
{
	private static final Vector3f CAM_START_POSITION = new Vector3f(0.0f, 20.0f, 100.0f);
	private static final float AMBIENT_LIGHT = 0.2f;
	private static final float AMBIENT_LIGHT_EFFECT = 1.0f;


	// GAMEOBJECTS
	private Rectangle sky;
	private PolyBullet[] bullets;
	private Polystrip polystrip;
	private PlayerShip playerShip;
	private HeadsUpDisplay hud;

	private float cnt = 0f;

	// SHADERS
	private ShaderProgram shadedVertexColorShader;

	// MATRICES // VECTORS
	private Matrix4f modelViewMatrix;
	private Matrix4f normalMatrix;
	
	// CONTROLS
	private boolean wireframe = false;
	
	@Override
	public void init() {
		printVersionInfo();
		
		Info.camera = new Camera(new Vector3f(CAM_START_POSITION.x, CAM_START_POSITION.y, CAM_START_POSITION.z));
		
		modelViewMatrix = new Matrix4f();
		normalMatrix = new Matrix4f();
		
		initOpenGL();
		shadedVertexColorShader = new ShaderProgram("shadedVertexColor");
		initGameObjects();
	}

	private void initOpenGL() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glEnable(GL_LINE_SMOOTH);		
        //glEnable(GL_POINT_SMOOTH);
        glPointSize(3.5f);


		glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        float aspectRatio = 800.0f / 600.0f; // Example aspect ratio
        float fov = 45.0f; // Field of view in degrees
        float near = 0.1f; // Near clipping plane
        float far = 100.0f; // Far clipping plane
        float top = (float) Math.tan(Math.toRadians(fov / 2)) * near;
        float bottom = -top;
        float right = top * aspectRatio;
        float left = -right;
        glFrustum(left, right, bottom, top, near, far);

		glMatrixMode (GL_MODELVIEW);
		glLoadIdentity ();

		
		FloatBuffer light_ambient = BufferUtils.createFloatBuffer(4);
		light_ambient.put(0).put(0).put(0).put(1).flip();

		FloatBuffer light_diffuse = BufferUtils.createFloatBuffer(4);
		light_diffuse.put(1).put(1).put(1).put(1).flip();

		FloatBuffer light_specular = BufferUtils.createFloatBuffer(4);
		light_specular.put(1).put(1).put(1).put(1).flip();

		FloatBuffer light_position = BufferUtils.createFloatBuffer(4);	
		light_position.put(1).put(1).put(3).put(0).flip();

		FloatBuffer lightModel_ambient = BufferUtils.createFloatBuffer(4);	
		lightModel_ambient.put(0.2f).put(0.2f).put(0.2f).put(1.0f).flip();

		FloatBuffer material_ambient = BufferUtils.createFloatBuffer(4);	
		material_ambient.put(0.2f).put(0.2f).put(0.2f).put(1.0f).flip();

		FloatBuffer material_diffuse = BufferUtils.createFloatBuffer(4);	
		material_diffuse.put(0.8f).put(0.8f).put(0.8f).put(1.0f).flip();

		FloatBuffer material_specular = BufferUtils.createFloatBuffer(4);	
		material_specular.put(1f).put(1f).put(1f).put(1f).flip();

		FloatBuffer material_emission = BufferUtils.createFloatBuffer(4);	
		material_emission.put(0f).put(0f).put(0f).put(1.0f).flip();

		glLightfv (GL_LIGHT0, GL_AMBIENT, light_ambient);
		glLightfv (GL_LIGHT0, GL_DIFFUSE, light_diffuse);
		glLightfv (GL_LIGHT0, GL_SPECULAR, light_specular);
		glLightfv (GL_LIGHT0, GL_POSITION, light_position);
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, lightModel_ambient);
		glMaterialfv(GL_FRONT, GL_AMBIENT, material_ambient);
		glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
		glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
		glMaterialfv(GL_FRONT, GL_EMISSION, material_emission);
		glMaterialf(GL_FRONT, GL_SHININESS, 10.0f);               /* NOT default value	*/
		glEnable (GL_LIGHTING);
		glEnable (GL_LIGHT0);
		glEnable (GL_NORMALIZE);
		glEnable (GL_DEPTH_TEST);
		glClearColor(.5f,.5f,.5f,1.f);
	}

	private void initGameObjects() {
		float[][] bgColors = new float[][] {
			Colors.D_BLUE,
			Colors.D_BLUE,
			Colors.D_BLUE,
			Colors.YELLOW
		};

		sky = new Rectangle(0f, 350f, -1500f, 2000f, 700f, bgColors, false);

		polystrip = new Polystrip(2000, 2000, false);
		playerShip = new PlayerShip();
		bullets = new PolyBullet[1000];
		
		for (int i = 0; i < bullets.length; i++) {
			bullets[i] = new PolyBullet(10f);
		}

		hud = new HeadsUpDisplay(20, 20, "arial_nm.png");
	}

	@Override
	public void update(float deltaTime) {

		Info.camera.update(deltaTime);
		playerShip.update(deltaTime);
		
		if(Keyboard.isKeyPressedWithReset(GLFW.GLFW_KEY_TAB)){ wireframe = !wireframe; }
		if(Keyboard.isKeyPressed(GLFW.GLFW_KEY_SPACE)){ shoot(); } //TODO: move to ship
		
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].update(deltaTime);
		}
	}

	@Override
	public void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		glUseProgram(shadedVertexColorShader.getId());

		shadedVertexColorShader.loadUniformMat4f(CoreEngine.projectionMatrix, "projectionMatrix", false);
		shadedVertexColorShader.loadUniform1f(AMBIENT_LIGHT, "ambientLight");
		drawElement(polystrip);
		drawElement(playerShip);
		
		shadedVertexColorShader.loadUniform1f(AMBIENT_LIGHT_EFFECT, "ambientLight");
		drawEffects();
		drawElement(sky);

		shadedVertexColorShader.loadUniformMat4f(CoreEngine.orthographicProjectionMatrix, "projectionMatrix", false);
		drawHUD();

		glUseProgram(0);

		


        // Switch to modelview matrix mode
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Apply the translation to move the object 10 units along the -z axis
        glTranslatef(playerShip.getPosition().x, playerShip.getPosition().y, playerShip.getPosition().z - 50f);
		glRotatef(cnt%360, 1,0,0);
		glRotatef(45.f, 0,0,1);
        // Your rendering code here
        // For example, drawing a simple object:
		glBegin(GL_QUADS);                // Begin drawing the color cube with 6 quads
		// Top face (y = 1.0f)
		glNormal3f( 0.0f, 1.0f,  0.0f);
		glVertex3f( 1.0f, 1.0f, -1.0f);
		glVertex3f(-1.0f, 1.0f, -1.0f);
		glVertex3f(-1.0f, 1.0f,  1.0f);
		glVertex3f( 1.0f, 1.0f,  1.0f);
		// Bottom face (y = -1.0f)
		glNormal3f( 0.0f, -1.0f,  0.0f);
		glVertex3f( 1.0f, -1.0f,  1.0f);
		glVertex3f(-1.0f, -1.0f,  1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f( 1.0f, -1.0f, -1.0f);
		// Front face  (z = 1.0f)
		glNormal3f( 0.0f,  0.0f, 1.0f);
		glVertex3f( 1.0f,  1.0f, 1.0f);
		glVertex3f(-1.0f,  1.0f, 1.0f);
		glVertex3f(-1.0f, -1.0f, 1.0f);
		glVertex3f( 1.0f, -1.0f, 1.0f);
		// Back face (z = -1.0f)
		glNormal3f( 0.0f,  0.0f, -1.0f);
		glVertex3f( 1.0f, -1.0f, -1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f(-1.0f,  1.0f, -1.0f);
		glVertex3f( 1.0f,  1.0f, -1.0f);
		// Left face (x = -1.0f)
		glNormal3f(-1.0f,  0.0f,  0.0f);
		glVertex3f(-1.0f,  1.0f,  1.0f);
		glVertex3f(-1.0f,  1.0f, -1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f(-1.0f, -1.0f,  1.0f);
		// Right face (x = 1.0f)
		glNormal3f(1.0f,  0.0f,  0.0f);
		glVertex3f(1.0f,  1.0f, -1.0f);
		glVertex3f(1.0f,  1.0f,  1.0f);
		glVertex3f(1.0f, -1.0f,  1.0f);
		glVertex3f(1.0f, -1.0f, -1.0f);
		glEnd();  // End of drawing color-cube
		cnt ++;

		System.out.println("FPS: " + Info.fps);
	}
	
	private void drawElement(MeshObject mesh) {
		Matrix4f.mul(Info.camera.getViewMatrix(), mesh.getModelMatrix(), modelViewMatrix);
		Matrix4f.invert(mesh.getModelMatrix(), normalMatrix);

		shadedVertexColorShader.loadUniformMat4f(modelViewMatrix, "modelViewMatrix", false);
		shadedVertexColorShader.loadUniformMat4f(normalMatrix, "normalMatrix", true);
		
		if(wireframe) {
			glDepthFunc(GL_LEQUAL);
			mesh.render(GL_LINE_LOOP);
			mesh.render(GL_POINTS);
		 	glDepthFunc(GL_LESS);
		}
		else {
			mesh.render(GL_TRIANGLES);
		}
	}

	private void drawEffects() {
		Matrix4f viewMatrix4f = Info.camera.getViewMatrix();

		for (int i = 0; i < bullets.length; i++) {
			Matrix4f.mul(viewMatrix4f, bullets[i].getModelMatrix(), modelViewMatrix);
			shadedVertexColorShader.loadUniformMat4f(modelViewMatrix, "modelViewMatrix", false);	
			
			bullets[i].render(GL_POINTS);
			bullets[i].render(GL_LINES);
		}
	}
	
	private void drawHUD() {
		shadedVertexColorShader.loadUniformMat4f(hud.getModelMatrix(), "modelViewMatrix", false);
		hud.getBackgroundMesh().render(GL_TRIANGLES);
	}
			
	private void printVersionInfo() {
		System.out.println("GPU Vendor: " + glGetString(GL_VENDOR));
		System.out.println("OpenGL version: " + glGetString(GL_VERSION));
		System.out.println("GLSL version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
	}
	
	private void shoot() {
		for (int i = 0; i < bullets.length; i++) {
			
			if (bullets[i].getIsActive() == false) {
				bullets[i].setIsActive(playerShip.getPosition());
				return;
			}
		}
	}
}
