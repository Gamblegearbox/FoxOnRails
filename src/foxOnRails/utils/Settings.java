package foxOnRails.utils;

public class Settings
{
	public static final float far = 2000.0f;
	public static final float near = 0.1f;
	public static int[] gameRes = new int[] {1280, 700};
	public static int fov = 95;
	public static int fps;

	public static float getAspectRatio() {
		return Settings.gameRes[0] / Settings.gameRes[1];
	}

	public static float getTop() {
		return (float) Math.tan(Math.toRadians(Settings.fov / 2)) * Settings.near;
	}
}