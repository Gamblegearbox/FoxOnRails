package foxOnRails.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Matrix4f;

import foxOnRails.input.Cursor;
import foxOnRails.input.Keyboard;
import foxOnRails.interfaces.Game;
import foxOnRails.utils.Settings;
import foxOnRails.utils.MatrixUtils;
import foxOnRails.utils.Timer;

public class CoreEngine
{
    private final Game game;

    public boolean running;
    public long windowHandle;
  
    boolean fullscreen = false;
    
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorCallback;

    public Timer timer;
    
    public CoreEngine(Game game) {
        this.game = game;
    }

    public void start() {
        running = true;

        init();

        while(running) {
            update();

            if(glfwWindowShouldClose(windowHandle) == GL_TRUE) {
                running = false;
            }
        }

        keyCallback.release();
        cursorCallback.release();
    }

    public void init() {
        if(glfwInit() != GL_TRUE) {
            System.err.println("can't initialize GLFW");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
    	GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);

    	if(fullscreen) {
    		long monitor = glfwGetPrimaryMonitor();
    		GLFWvidmode mode = new GLFWvidmode(glfwGetVideoMode(monitor));		
    		windowHandle = glfwCreateWindow(mode.getWidth(), mode.getHeight(), "Stare into it device: " + windowHandle, monitor, NULL);
       	} 
    	else {
    		windowHandle = glfwCreateWindow(Settings.gameRes[0], Settings.gameRes[1], "Stare into it device: " + windowHandle, NULL, NULL);	
    	}
        
        if(windowHandle == NULL) {
            System.err.println("Window creation failed");
        }
        
        glfwSetWindowPos(windowHandle, 100, 100);

        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1); //vSync

        glfwShowWindow(windowHandle);

        GLContext.createFromCurrent();

        glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetKeyCallback(windowHandle, keyCallback = new Keyboard());
        glfwSetCursorPosCallback(windowHandle, cursorCallback = new Cursor());

        game.init();
        
        timer = new Timer(); //not sure if best here
    }

    public void update() {
        glfwPollEvents();
   
        Settings.fps = timer.getFPS();
        game.update(timer.getDeltaTime());
        game.render();
        
        if(Keyboard.isKeyPressed(GLFW_KEY_ESCAPE))
        	glfwSetWindowShouldClose(windowHandle, GL_TRUE);
        
        glfwSwapBuffers(windowHandle);
    }

}
