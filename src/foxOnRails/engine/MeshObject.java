package foxOnRails.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;


public abstract class MeshObject 
{
	protected float[] vertices;
	protected float[] normals;
	protected float[] colors;
	
	protected int[] indices;
	
	protected Matrix4f modelMatrix;
	protected Vector3f position;
	
	protected VertexArray mesh;
	
	public void render(int mode) {
		mesh.render(mode);
	}
	
	public Matrix4f getModelMatrix(){
		return modelMatrix;
	}
	
	public Vector3f getPosition(){
		return position;
	}
	
	public void setPosition(Vector3f position){
		this.position = position;
		modelMatrix.m30 = position.x;
		modelMatrix.m31 = position.y;
		modelMatrix.m32 = position.z;
	}	
}
