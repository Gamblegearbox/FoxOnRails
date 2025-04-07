package foxOnRails.geometry;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import foxOnRails.engine.MeshObject;
import foxOnRails.engine.VertexArray;

public class StaticSphere extends MeshObject
{
	private static final Vector3f FRONT = new Vector3f(0, 0, 1);
	private static final Vector3f BACK = new Vector3f(0, 0, -1);
	private static final Vector3f UP = new Vector3f(0, 1, 0);
	private static final Vector3f DOWN = new Vector3f(0, -1, 0);
	private static final Vector3f LEFT = new Vector3f(-1, 0, 0);
	private static final Vector3f RIGHT = new Vector3f(1, 0, 0);

	private Vector3f[] verticesVec;
	
	private static Vector3f[] directions = { LEFT, BACK, RIGHT, FRONT };

	public StaticSphere(int subdivisions, float radius, boolean deforms) {
		int resolution = 1 << subdivisions;
		
		this.position = new Vector3f(0.0f, 0.0f, 0.0f);
		this.modelMatrix = new Matrix4f().translate(position);
		verticesVec = new Vector3f[(resolution + 1) * (resolution + 1) * 4 - (resolution * 2 - 1) * 3];
		indices = new int[(1 << (subdivisions * 2 + 3)) * 3];
		vertices = new float[verticesVec.length * 3]; 
		colors = new float[vertices.length];

		
		createOctahedron(resolution);
		normalizeVerticesAndCreateNormals();
		applyMeshModifications(radius, deforms);

		mesh = new VertexArray(vertices, normals, colors, indices);
	}

	private void createOctahedron(int resolution) {
		int v = 0;
		int vBottom = 0;
		int t = 0;
		for (int i = 0; i < 4; i++) {
			verticesVec[v++] = DOWN;
		}
		// LOWERSPHERE
		for (int i = 1; i <= resolution; i++) {
			float progress = (float) i / resolution;
			Vector3f from;
			Vector3f to;
			verticesVec[v++] = to = lerp(DOWN,
					FRONT, progress);
			for (int d = 0; d < 4; d++) {
				from = to;
				to = lerp(DOWN, directions[d], progress);
				t = createLowerStrip(i, v, vBottom, t);
				v = createVertexLine(from, to, i, v);
				vBottom += i > 1 ? (i - 1) : 1;
			}
			vBottom = v - 1 - i * 4;
		}
		// UPPERSPHERE
		for (int i = resolution - 1; i >= 1; i--) {
			float progress = (float) i / resolution;
			Vector3f from;
			Vector3f to;
			verticesVec[v++] = to = lerp(UP, FRONT,
					progress);
			for (int d = 0; d < 4; d++)
			{
				from = to;
				to = lerp(UP, directions[d], progress);
				t = createUpperStrip(i, v, vBottom, t);
				v = createVertexLine(from, to, i, v);
				vBottom += i + 1;
			}
			vBottom = v - 1 - i * 4;
		}
		for (int i = 0; i < 4; i++) {
			indices[t++] = vBottom;
			indices[t++] = v;
			indices[t++] = ++vBottom;
			verticesVec[v++] = UP;
		}
	}

	private int createVertexLine(Vector3f from, Vector3f to, int steps, int v)
	{
		for (int i = 1; i <= steps; i++)
		{
			verticesVec[v++] = lerp(from, to, (float) i / steps);
		}
		return v;
	}

	private int createUpperStrip(int steps, int vTop, int vBottom, int t)
	{
		indices[t++] = vBottom;
		indices[t++] = vTop - 1;
		indices[t++] = ++vBottom;
		for (int i = 1; i <= steps; i++)
		{
			indices[t++] = vTop - 1;
			indices[t++] = vTop;
			indices[t++] = vBottom;
			indices[t++] = vBottom;
			indices[t++] = vTop++;
			indices[t++] = ++vBottom;
		}
		return t;
	}

	private int createLowerStrip(int steps, int vTop, int vBottom, int t)
	{
		for (int i = 1; i < steps; i++)
		{
			indices[t++] = vBottom;
			indices[t++] = vTop - 1;
			indices[t++] = vTop;
			indices[t++] = vBottom++;
			indices[t++] = vTop++;
			indices[t++] = vBottom;
		}
		indices[t++] = vBottom;
		indices[t++] = vTop - 1;
		indices[t++] = vTop;
		return t;
	}

	public void normalizeVerticesAndCreateNormals()
	{
		normals = new float[verticesVec.length * 3];

		for (int i = 0; i < verticesVec.length; i++) {
			verticesVec[i].normalise();
			normals[i * 3    ] = verticesVec[i].x;
			normals[i * 3 + 1] = verticesVec[i].y;
			normals[i * 3 + 2] = verticesVec[i].z;
		}
	}
	
	public void applyMeshModifications(float radius, boolean deforms) {
		//TODO: just to get started, propert terrain creation might go here

		if (deforms) {
			float maxHeight = 4.0f;
			float maxSine = 20f;
			
			for(int i = 0; i < verticesVec.length; i++){
				float sinMod = (float) (Math.random()) * maxSine;
				float heightMod = (float) (Math.random()) * maxHeight;
				float xMod = (float) Math.sin(verticesVec[i].x * sinMod) * heightMod;
				float yMod = (float) Math.sin(verticesVec[i].y * sinMod) * heightMod;
				float zMod =(float) Math.sin(verticesVec[i].z * sinMod) * heightMod;

				vertices[i * 3    ] = verticesVec[i].x * (radius + xMod + yMod + zMod);
				vertices[i * 3 + 1] = verticesVec[i].y * (radius + xMod + yMod + zMod);
				vertices[i * 3 + 2] = verticesVec[i].z * (radius + xMod + yMod + zMod);
			}
		} 
		else {
			for(int i = 0; i < verticesVec.length; i++) {
				vertices[i * 3    ] = verticesVec[i].x * radius;
				vertices[i * 3 + 1] = verticesVec[i].y * radius;
				vertices[i * 3 + 2] = verticesVec[i].z * radius;
			}
		}
	}

	public void updateMeshData(){
		mesh.update(vertices, normals, colors, indices);
	}

	
	public void render(int mode){
		mesh.render(mode);
	}
	
	public static Vector3f lerp(Vector3f a, Vector3f b, float step)
	{
		float x = a.x + (b.x - a.x) * step; 
		float y = a.y + (b.y - a.y) * step; 
		float z = a.z + (b.z - a.z) * step; 
		
		return new Vector3f(x, y, z);
	}

}
