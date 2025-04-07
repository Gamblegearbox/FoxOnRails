package foxOnRails.geometry;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import foxOnRails.engine.MeshObject;
import foxOnRails.engine.VertexArray;
import foxOnRails.gameContent.Colors;

public class Polystrip extends MeshObject
{	
	private static final int VERTS = 4;

	public Polystrip(float width, float length, boolean deforms) {
	
		this.position = new Vector3f(0.0f, 0.0f, 0.0f);
		this.modelMatrix = new Matrix4f().translate(position);

		vertices = new float[VERTS * 3];
		normals = new float[VERTS * 3]; 
		colors = new float[VERTS * 4];
		indices = new int[6];
		
		createVertices(width, length);
		createNormals();
		//applyMeshModifications(radius, deforms);

		mesh = new VertexArray(vertices, normals, colors, indices);
	}

	private void createVertices(float width, float length) {
		float xPos = width * 0.5f;

		//0
		vertices[0]  = -xPos;
		vertices[1]  = 0f;
		vertices[2]  = -length;
		normals[0]  = 0f;
		normals[1]  = 1f;
		normals[2]  = 0f;

		//1
		vertices[3]  = xPos;
		vertices[4]  = 0f;
		vertices[5]  = -length;
		normals[3]  = 0f;
		normals[4]  = 1f;
		normals[5]  = 0f;

		//2
		vertices[6]  = -xPos;
		vertices[7]  = 0f;
		vertices[8]  = 100f;
		normals[6]  = 0f;
		normals[7]  = 1f;
		normals[8]  = 0f;

		//3
		vertices[9]  = xPos;
		vertices[10] = 0f;
		vertices[11] = 100f;
		normals[9]  = 0f;
		normals[10]  = 1f;
		normals[11]  = 0f;

		indices[0] = 0;
		indices[1] = 2;
		indices[2] = 1;
		indices[3] = 2;
		indices[4] = 3;
		indices[5] = 1;
		
		for (int i = 0; i < colors.length; i += 4) {
			colors[i+0] = Colors.L_GREEN[0];
			colors[i+1] = Colors.L_GREEN[1];
			colors[i+2] = Colors.L_GREEN[2];
			colors[i+3] = Colors.L_GREEN[3];
		}

	}

	public void createNormals() {
		normals = new float[4 * 3];

		for (int i = 0; i < 4; i++) {
			normals[i * 3    ] = 0.0f;
			normals[i * 3 + 1] = 1.0f;
			normals[i * 3 + 2] = 0.0f;
		}
	}

	public void applyMeshModifications(float radius, boolean deforms) {

	}

	public void updateMeshData(){
		mesh.update(vertices, normals, colors, indices);
	}
	
	public void render(int mode){
		mesh.render(mode);
	}
	
}
