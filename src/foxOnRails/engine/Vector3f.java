package foxOnRails.engine;

public class Vector3f {
    
    public float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3f pos) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }
}
