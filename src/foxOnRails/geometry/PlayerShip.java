package foxOnRails.geometry;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import foxOnRails.engine.MeshObject;
import foxOnRails.engine.VertexArray;
import foxOnRails.gameContent.Colors;
import foxOnRails.input.Keyboard;

public class PlayerShip extends MeshObject
{	
	private static final int VERTS = 3;
	private static final float SPEED = 100.0f;
	private static final float GUN_COOLDOWN = 250f;
	private float gunCooldown = 0.0f;

	public PlayerShip() {		
		position = new Vector3f(0.0f, 10.0f, 0.0f);
		modelMatrix = new Matrix4f().translate(position);

		indices = new int[3];
		vertices = new float[VERTS * 3]; 
		normals = new float[VERTS * 3];
		colors = new float[VERTS * 4];

		createVertices();

		mesh = new VertexArray(vertices, normals, colors, indices);
	}

	private void createVertices() {

		// 0
		vertices[0]  = 0f;
		vertices[1]  = -0.964116f;
		vertices[2]  = -11.3165f;
		normals[0]  = 0f;
		normals[1]  = 0f;
		normals[2]  = -1f;
		colors[0]  = Colors.L_GREY[0];
		colors[1]  = Colors.L_GREY[1];
		colors[2]  = Colors.L_GREY[2];
		colors[3]  = Colors.L_GREY[3];

		// 1
		vertices[3]  = 1.36827f;
		vertices[4]  = -0.404788f;
		vertices[5]  = 1.88934f;
		normals[3]  = 0f;
		normals[4]  = 0f;
		normals[5]  = -1f;
		colors[4]  = Colors.L_GREY[0];
		colors[5]  = Colors.L_GREY[1];
		colors[6]  = Colors.L_GREY[2];
		colors[7]  = Colors.L_GREY[3];

		// 2		
		vertices[6]  = 0f;
		vertices[7]  = 1.07716f;
		vertices[8]  = -0.11066f;
		normals[6]  = 0f;
		normals[7]  = 0f;
		normals[8]  = -1f;
		colors[8]  = Colors.L_GREY[0];
		colors[9]  = Colors.L_GREY[1];
		colors[10]  = Colors.L_GREY[2];
		colors[11]  = Colors.L_GREY[3];

		// TOP LEFT
		indices[0] = 0;
		indices[1] = 2;
		indices[2] = 1;



		
	}
	
	public void update(float deltaTime) {

		if(Keyboard.isKeyPressed(GLFW_KEY_A))
			position.x -= (SPEED * deltaTime);

		if(Keyboard.isKeyPressed(GLFW_KEY_D))
			position.x += (SPEED * deltaTime);

		if(Keyboard.isKeyPressed(GLFW_KEY_W))
			position.y += (SPEED * deltaTime);
		
		if(Keyboard.isKeyPressed(GLFW_KEY_S))
			position.y -= (SPEED * deltaTime);

		setPosition(position);

		if(Keyboard.isKeyPressed(GLFW_KEY_SPACE)){ 
			gunCooldown -= deltaTime;
			if (gunCooldown <= 0.0f) {
				//shoot();
				gunCooldown = GUN_COOLDOWN;
			}
		}
	}

	public void render(int mode){
		mesh.render(mode);
	}
	
}
