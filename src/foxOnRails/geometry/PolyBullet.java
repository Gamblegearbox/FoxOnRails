package foxOnRails.geometry;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import foxOnRails.engine.MeshObject;
import foxOnRails.engine.VertexArray;
import foxOnRails.gameContent.Colors;

public class PolyBullet extends MeshObject
{	
	private static final int VERTS = 2;
	private static final float BULLET_SPEED = 3500f;
	private static final float BULLET_RANGE = -2000f;
	private static final float RESET_Z_POS = 100f;
	

	private boolean isActive;

	public PolyBullet(float size) {		
		isActive = false;
		modelMatrix = new Matrix4f();
		modelMatrix.setIdentity();

		position = new Vector3f(0.0f, 5.0f, 0.0f);
		setPosition(position);

		indices = new int[2];
		vertices = new float[VERTS * 3]; 
		normals = new float[VERTS * 3];
		colors = new float[VERTS * 4];

		createMeshData(size);
		

		mesh = new VertexArray(vertices, normals, colors, indices);
	}

	private void createMeshData(float size) {

		//0
		vertices[0]  = 0f;
		vertices[1]  = 0f;
		vertices[2]  = 0f;
		
		normals[0] = 0f;
		normals[1] = 0f;
		normals[2] = 0f;

		colors[0]  = Colors.RED[0];
		colors[1]  = Colors.RED[1];
		colors[2]  = Colors.RED[2];
		colors[3]  = Colors.RED[3];


		//1
		vertices[3]  = 0f;
		vertices[4]  = 0f;
		vertices[5]  = size;
		
		normals[3] = 0f;
		normals[4] = 0f;
		normals[5] = 0f;

		colors[4]  = Colors.YELLOW[0];
		colors[5]  = Colors.YELLOW[1];
		colors[6]  = Colors.YELLOW[2];
		colors[7]  = Colors.YELLOW[3];


		indices[0] = 0;
		indices[1] = 1;		
	}
	
	public void update(float deltaTime) {
		if (isActive && position.z > BULLET_RANGE) {
			position.z -= BULLET_SPEED * deltaTime; 
		}
		else {
			position.z = RESET_Z_POS;
			isActive = false;
		}
		setPosition(position);
	}

	public void setIsActive(Vector3f startPos) {
		position.set(startPos);
		this.isActive = true;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void render(int mode){
		mesh.render(mode);
	}
	
}
