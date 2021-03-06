package graphics;


import game.FPSCounter;
import game.InputReceiver;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;



public class Screen {
	
	
	protected int width, height;
	protected boolean fullscreen = false;
	
	
	
	public Screen(int width, int height) {
		
		try {
			Display.setDisplayMode( new DisplayMode( 640, 480 ));
			PixelFormat pix = new PixelFormat( Display.getDisplayMode().getBitsPerPixel(),
				     8,   // alpha
				    24,   // depth buffer
				     1,   // stencil buffer
				     0) ;
			Display.create( pix );
			setResolution( width, height );
			
		} catch ( LWJGLException e ) {

			e.printStackTrace();
		}
	}
	
	
	
	
	public void setResolution (int width, int height ) throws LWJGLException {
		
		this.width = width;
		this.height = height;
		Display.setDisplayMode( new DisplayMode( width, height ));
		Display.setFullscreen( this.fullscreen );
		initGL( width, height );
	}
	
	
	
	
	public void setFullscreen( boolean fullscreen ){
		
		try {
			Display.setFullscreen( fullscreen );
			this.fullscreen = fullscreen;
		} catch ( LWJGLException e ) {
			
			e.printStackTrace();
		}
		
		initGL( width, height );
	}
	
	
	
	
	public void setTitle( String title ) {
		
		Display.setTitle( title );
	}
	
	
	
	
	private void initGL( int width, int height ) {
		
		GL11.glEnable( GL11.GL_TEXTURE_2D );
		GL11.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
		GL11.glEnable( GL11.GL_BLEND );
		GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
		GL11.glViewport( 0, 0, width, height );
		GL11.glMatrixMode( GL11.GL_MODELVIEW );
		GL11.glMatrixMode( GL11.GL_PROJECTION );
		GL11.glLoadIdentity();
		GL11.glOrtho( 0, width, height, 0, 1, -1 );
		GL11.glMatrixMode( GL11.GL_MODELVIEW );
		GL11.glDisable( GL11.GL_DEPTH_TEST );
		
		/*
		 * GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
		 * GL11.GL_CLAMP); GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
		 * GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		 */
	}
	
	
	
	
	public boolean shallClose(){
		
		return Display.isCloseRequested();
	}
	
	
	
	
	public void close() {
		
		Display.destroy();
	}

	/**
	 * Sollten wir vielleicht anders machen. Warum muss der Input im Screen behandelt werden? Warum nicht direkt in einer eigenen Klasse?
	 * @param receiver
	 */
	@Deprecated
	public void handleInput( InputReceiver receiver ) {
		
		while ( Keyboard.next() ) {
			
			if ( Keyboard.getEventKeyState() )
				receiver.keyPressed( Keyboard.getEventKey() );
			else
				receiver.keyReleased( Keyboard.getEventKey() );
		}

		while ( Mouse.next() ) {
			
			if ( Mouse.getEventButtonState() && Mouse.getEventButton() >= 0 )
				receiver.mousePressed( Mouse.getEventButton() );
			
			else if ( Mouse.getEventButton() >= 0 ) {
				receiver.mouseReleased( Mouse.getEventButton() );
			}
			
			if ( Mouse.hasWheel() ) {
				
				receiver.mouseWheel( Mouse.getDWheel() );
			}
		}	
	}
	
	
	
	public void render() {
		
		FPSCounter.tick();
		setTitle( "FPS: "  +FPSCounter.getFPS() + " Delta: " + FPSCounter.getDelta() );
		
		Display.update( );	//swap buffers
		Display.sync( 120 );	// FPS begrenzen
		
		// Clear screen
		GL11.glClear( GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
		GL11.glMatrixMode( GL11.GL_MODELVIEW );
		GL11.glLoadIdentity();

	}
}
