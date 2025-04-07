package foxOnRails.geometry;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class SimpleMeshFactory {
    
    public static void cube(float size, int mode) {
        glBegin(mode);
        // Top face (y = 1.0f)
        glVertex3f( size, size, -size);
		glVertex3f(-size, size, -size);
		glVertex3f(-size, size,  size);
		glVertex3f( size, size,  size);

		// Bottom face (y = -1.0f)
		glVertex3f( size, -size,  size);
		glVertex3f(-size, -size,  size);
		glVertex3f(-size, -size, -size);
		glVertex3f( size, -size, -size);

		// Front face  (z = 1.0f)
		glVertex3f( size,  size, size);
		glVertex3f(-size,  size, size);
		glVertex3f(-size, -size, size);
		glVertex3f( size, -size, size);

		// Back face (z = -1.0f)
		glVertex3f( size, -size, -size);
		glVertex3f(-size, -size, -size);
		glVertex3f(-size,  size, -size);
		glVertex3f( size,  size, -size);

		// Left face (x = -1.0f)
		glVertex3f(-size,  size,  size);
		glVertex3f(-size,  size, -size);
		glVertex3f(-size, -size, -size);
		glVertex3f(-size, -size,  size);

		// Right face (x = 1.0f)

		glVertex3f(size,  size, -size);
		glVertex3f(size,  size,  size);
		glVertex3f(size, -size,  size);
		glVertex3f(size, -size, -size);

        glEnd();
    }
}
