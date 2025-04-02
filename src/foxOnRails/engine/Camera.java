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
	private Matrix4f rot;
	private Matrix4f trans;
	
	private float velocity = MAX_CAM_SPEED;
	private boolean boostEnabled;


	public Camera(Vector3f _position) {
		position = _position;
		view = new Matrix4f();
		rot = new Matrix4f();
		trans = new Matrix4f();

		view.setIdentity();
		rot.setIdentity();
		trans.setIdentity();
	}
		
	public Matrix4f getViewMatrix() {
		angle = (float)Cursor.getDx() / 250.0f;
		rot.rotate(angle, UP);

		trans.m30 = -position.x;
		trans.m31 = -position.y;
		trans.m32 = -position.z;
		trans.m33 = 1.0f;

		Matrix4f.mul(rot, trans, view);
		
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
		angle = 0f;

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