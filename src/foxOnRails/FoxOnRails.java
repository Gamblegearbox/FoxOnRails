package foxOnRails;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.LinkedList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import foxOnRails.engine.Camera;
import foxOnRails.engine.CoreEngine;
import foxOnRails.gameContent.HeadsUpDisplay;
import foxOnRails.geometry.PlayerShip;
import foxOnRails.geometry.PolyBullet;
import foxOnRails.geometry.Polystrip;
import foxOnRails.graphics.ShaderProgram;
import foxOnRails.input.Keyboard;
import foxOnRails.interfaces.Game;
import foxOnRails.utils.Info;

public class FoxOnRails implements Game 
{
	private static final Vector3f CAM_START_POSITION = new Vector3f(0.0f, 20.0f, 100.0f);
	private static final Vector3f LIGHT_POSITION = new Vector3f(0f, 5f, -1f);

	// GAMEOBJECTS
	private PolyBullet[] bullets;
	private Polystrip polystrip;
	private PlayerShip playerShip;
	private HeadsUpDisplay hud;

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
	}

	private void initGameObjects() {
		polystrip = new Polystrip(1, 100, false);
		playerShip = new PlayerShip();
		bullets = new PolyBullet[1000];
		
		for (int i = 0; i < bullets.length; i++) {
			bullets[i] = new PolyBullet(10f);
		}

		hud = new HeadsUpDisplay(10, 10, "arial_nm.png");
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

		System.err.println("FPS: " + Info.fps);
	}

	@Override
	public void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		glUseProgram(shadedVertexColorShader.getId());

		shadedVertexColorShader.loadUniformMat4f(CoreEngine.projectionMatrix, "projectionMatrix", false);
		shadedVertexColorShader.loadUniform1f(0.1f, "ambientLight");
		shadedVertexColorShader.loadUniformVec3f(LIGHT_POSITION, "lightPosition");
		drawLevel();
		drawPlayer();
		shadedVertexColorShader.loadUniform1f(1.0f, "ambientLight");
		drawEffects();

		shadedVertexColorShader.loadUniformMat4f(CoreEngine.orthographicProjectionMatrix, "projectionMatrix", false);
		drawHUD();

		glUseProgram(0);
	}
	
	private void drawLevel() {
		Matrix4f.mul(Info.camera.getViewMatrix(), polystrip.getModelMatrix(), modelViewMatrix);
		Matrix4f.invert(polystrip.getModelMatrix(), normalMatrix);

		shadedVertexColorShader.loadUniformMat4f(modelViewMatrix, "modelViewMatrix", false);
		shadedVertexColorShader.loadUniformMat4f(normalMatrix, "normalMatrix", true);
		
		if(wireframe) {
			glDepthFunc(GL_LEQUAL);
			polystrip.render(GL_LINE_LOOP);
			polystrip.render(GL_POINTS);
		 	glDepthFunc(GL_LESS);
		}
		else {
			polystrip.render(GL_TRIANGLES);
		}
	}

	private void drawPlayer(){
		Matrix4f.mul(Info.camera.getViewMatrix(), playerShip.getModelMatrix(), modelViewMatrix);
		Matrix4f.invert(playerShip.getModelMatrix(), normalMatrix);

		shadedVertexColorShader.loadUniformMat4f(modelViewMatrix, "modelViewMatrix", false);
		shadedVertexColorShader.loadUniformMat4f(normalMatrix, "normalMatrix", true);

		if(wireframe) {
			glDepthFunc(GL_LEQUAL);
			playerShip.render(GL_LINE_LOOP);
			playerShip.render(GL_POINTS);
		 	glDepthFunc(GL_LESS);
		}
		else {
			playerShip.render(GL_TRIANGLES);
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
