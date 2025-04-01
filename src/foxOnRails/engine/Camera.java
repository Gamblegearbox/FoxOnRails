package foxOnRails.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import foxOnRails.input.Cursor;
import foxOnRails.input.Keyboard;


public class Camera
{
	private Vector3f position;
	private float angle = 0.0f;

	private static final float NORMAL_CAM_SPEED = 50.0f;
	public static final float MAX_CAM_SPEED = 150.0f;

	private static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);

	private Matrix4f view;
	
	private float velocity = MAX_CAM_SPEED;
	private boolean boostEnabled;


	public Camera(Vector3f _position) {
		position = _position;
		view = new Matrix4f();
	}
		
	public Matrix4f getViewMatrix() {
		angle = (float)Cursor.getDx() / 250.0f;
		
		view.rotate(angle, UP);
		
		view.m30 = -position.x;
		view.m31 = -position.y;
		view.m32 = -position.z;
		view.m33 = 1.0f;

		return view;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void update(float deltaTime) {
		boostEnabled = Keyboard.isKeyPressed(GLFW_KEY_LEFT_SHIFT);

		if(boostEnabled)
			velocity = MAX_CAM_SPEED;
		else
			velocity = NORMAL_CAM_SPEED;
		
		if(Keyboard.isKeyPressed(GLFW_KEY_UP))
			position.z -= velocity * deltaTime;
		
		if(Keyboard.isKeyPressed(GLFW_KEY_DOWN))
			position.z += velocity * deltaTime;
		
		if(Keyboard.isKeyPressed(GLFW_KEY_LEFT))
			position.x -= velocity * deltaTime;

		if(Keyboard.isKeyPressed(GLFW_KEY_RIGHT))
			position.x += velocity * deltaTime;
			
		if(Keyboard.isKeyPressed(GLFW_KEY_PAGE_UP))
			position.y += velocity * deltaTime;
		
		if(Keyboard.isKeyPressed(GLFW_KEY_PAGE_DOWN))
			position.y -= velocity * deltaTime;
	}

}