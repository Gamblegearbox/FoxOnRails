package foxOnRails.geometry;

import static org.lwjgl.opengl.GL11.*;

import foxOnRails.gameContent.Colors;
import foxOnRails.utils.Settings;


public class Level
{	
	private static final float MOVE_BOX_MAX = PlayerShip.X_POS_MAX + 0.1f;

	public Level() {	
	}

	public void update(float deltaTime) {
	}

	public void renderGameVolume(boolean isIso){
		int lines = 11;
		float startPos = 100f;
		float lineLength = 500f;
		float xyGap = MOVE_BOX_MAX * 2.0f / (lines -1);
		float zGap = 25f;
		float dotOpacity = 0.0f;

		glPointSize(2f);
		glLineWidth(2f);
		
		glDisable(GL_LIGHTING);
		glPushMatrix();
		glBegin(GL_LINES);

			for (int i = 0; i < lines; i++) {
				float x = -MOVE_BOX_MAX + (xyGap * i);

				glColor4f(Colors.PINK[0], Colors.PINK[1], Colors.PINK[2], 0.5f);
				glVertex3f( x, -MOVE_BOX_MAX, startPos);
				glColor4f(Colors.TRANSPARENT[0], Colors.TRANSPARENT[1], Colors.TRANSPARENT[2],Colors.TRANSPARENT[3]);
				glVertex3f( x, -MOVE_BOX_MAX, -lineLength);
				if(!isIso) {
					glColor4f(Colors.YELLOW[0], Colors.YELLOW[1], Colors.YELLOW[2], 0.5f);
					glVertex3f( x, MOVE_BOX_MAX, startPos);
					glColor4f(Colors.TRANSPARENT[0], Colors.TRANSPARENT[1], Colors.TRANSPARENT[2],Colors.TRANSPARENT[3]);
					glVertex3f( x, MOVE_BOX_MAX, -lineLength);
				}
			}
		glEnd();

		if(isIso) {
			glBegin(GL_POINTS);
			
			for (int z = 0; z < lines; z++) {
				
				glColor4f(Colors.WHITE[0], Colors.WHITE[1], Colors.WHITE[2], dotOpacity);
				dotOpacity += 0.1;
				
				for (int y = 0; y < lines; y++) {
					float yPos = -MOVE_BOX_MAX + (xyGap * y);
					
					for (int x = 0; x < lines; x++) {
						float xPos = -MOVE_BOX_MAX + (xyGap * x);
						glVertex3f( xPos, yPos, -z * zGap - 50f);
					}	
				}	
			}
			glEnd();
		}

		glPopMatrix();
	}

	public void renderSky () {
		int width = Settings.gameRes[0];
		int height = Settings.gameRes[1];
		int focalPoint = 300;

		glDisable(GL_LIGHTING);
		glBegin(GL_QUADS);
			glColor4f(Colors.D_BLUE[0], Colors.D_BLUE[1], Colors.D_BLUE[2], 0.1f);
			glVertex3i( 0, focalPoint, -1);
			glColor4f(Colors.BLACK[0], Colors.BLACK[1], Colors.BLACK[2], Colors.BLACK[3]);
			glVertex3i( width, focalPoint, -1);
			glColor4f(Colors.YELLOW[0], Colors.YELLOW[1], Colors.YELLOW[2], 0.1f);
			glVertex3i( width, height, -1);
			glColor4f(Colors.PINK[0], Colors.PINK[1], Colors.PINK[2], 0.5f);
			glVertex3i( 0, height, -1);

			glColor4f(Colors.BLACK[0], Colors.BLACK[1], Colors.BLACK[2], Colors.BLACK[3]);
			glVertex3i( 0, 0, -1);
			glVertex3i( width, 0, -1);
			glVertex3i( width, focalPoint, -1);
			glColor4f(Colors.D_BLUE[0], Colors.D_BLUE[1], Colors.D_BLUE[2], 0.1f);
			glVertex3i( 0, focalPoint, -1);
		glEnd();
	}
}
