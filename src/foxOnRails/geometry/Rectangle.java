package foxOnRails.geometry;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import foxOnRails.engine.MeshObject;
import foxOnRails.engine.VertexArray;

public class Rectangle extends MeshObject
{	
	public Rectangle(float x, float y, float z, float width, float height, float[][] vertexColors, boolean is2d) {	
		position = new Vector3f(x,y,z);
		modelMatrix = new Matrix4f();
		Matrix4f.setIdentity(modelMatrix);
		modelMatrix.translate(position);

		vertices = new float[12];
		indices = new int[6];	
		normals = new float[4*3];
		colors = new float[16];
		createMesh(width, height, is2d);
		setColors(vertexColors);
		mesh = new VertexArray(vertices, normals, colors, indices);
	}
	
	private void createMesh(float width, float height, boolean is2d) {
		float xMod, yMod;
		
		if(!is2d) {
			xMod = width * 0.5f;
			yMod = height * 0.5f;
		}
		else {
			xMod = 0;
			yMod = 0;
		}

		// 0
		vertices[0] = -xMod;
		vertices[1] = -yMod + height;
		vertices[2] = 0f;
		normals[0] = 0f;
		normals[1] = 0f;
		normals[2] = 1f;
		
		// 1
		vertices[3] = -xMod + width;
		vertices[4] = -yMod + height;
		vertices[5] = 0;
		normals[3] = 0f;
		normals[4] = 0f;
		normals[5] = 1f;
		
		// 2
		vertices[6] = -xMod + width;
		vertices[7] = -yMod;
		vertices[8] = 0;
		normals[6] = 0f;
		normals[7] = 0f;
		normals[8] = 1f;
		
		// 3
		vertices[9] = -xMod;
		vertices[10] = -yMod;
		vertices[11] = 0;
		normals[9] = 0f;
		normals[10] = 0f;
		normals[11] = 1f;
		
		indices[0] = 2;
		indices[1] = 1;
		indices[2] = 0;

		indices[3] = 0;
		indices[4] = 3;
		indices[5] = 2;		
	}
		
	private void setColors(float[][] vertexColors) {
		colors[0]  = vertexColors[0][0];
		colors[1]  = vertexColors[0][1];
		colors[2]  = vertexColors[0][2];
		colors[3]  = vertexColors[0][3];
 
		colors[4]  = vertexColors[1][0];
		colors[5]  = vertexColors[1][1];
		colors[6]  = vertexColors[1][2];
		colors[7]  = vertexColors[1][3];
 
		colors[8]  = vertexColors[2][0];
		colors[9]  = vertexColors[2][1];
		colors[10] = vertexColors[2][2];
		colors[11] = vertexColors[2][3];

		colors[12] = vertexColors[3][0];
		colors[13] = vertexColors[3][1];
		colors[14] = vertexColors[3][2];
		colors[15] = vertexColors[3][3];
	}

}
