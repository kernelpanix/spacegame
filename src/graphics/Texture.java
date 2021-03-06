package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import org.lwjgl.opengl.GL11;



public class Texture {
	
	
	
	private static int idCounter = 1;
	private int id, textureEdge;

	
	
	public void bind() {
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	
	
	public Texture( BufferedImage image ) {
		
		id = idCounter++;
		
		textureEdge = next_powerOfTwo_square( image.getWidth(), image.getHeight() );
		
		
		
		BufferedImage texture_image = new BufferedImage( textureEdge, textureEdge,
				BufferedImage.TYPE_INT_ARGB );
		texture_image.getGraphics().drawImage( image, 0, 0, null );
		
		ByteBuffer buffer = convertImageData( texture_image );
		
		GL11.glBindTexture( GL11.GL_TEXTURE_2D, id );
		GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR );
		GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR );
		GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureEdge, textureEdge, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
	}
	
	
	
	private static ByteBuffer convertImageData( BufferedImage bufferedImage ) {
		
		ByteBuffer imageBuffer;
		WritableRaster raster;
		BufferedImage texImage;

		ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance( ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE );

		raster = Raster.createInterleavedRaster( DataBuffer.TYPE_BYTE, bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null );
		texImage = new BufferedImage( glAlphaColorModel, raster, true, new Hashtable<Object, Object>() );

		// copy the source image into the produced image
		Graphics g = texImage.getGraphics();
		g.setColor( new Color(0f, 0f, 0f, 0f) );
		g.fillRect( 0, 0, 256, 256 );
		g.drawImage( bufferedImage, 0, 0, null );

		// build a byte buffer from the temporary image
		// that be used by OpenGL to produce a texture.
		byte[] data = ( (DataBufferByte) texImage.getRaster().getDataBuffer() )
				.getData();

		imageBuffer = ByteBuffer.allocateDirect( data.length );
		imageBuffer.order( ByteOrder.nativeOrder() );
		imageBuffer.put( data, 0, data.length );
		imageBuffer.flip( );

		return imageBuffer;
	}
	
	
	
	public static int next_powerOfTwo_square( int width, int height ) {

		int longest_edge = width > height ? width : height;
		int sprite_edge = (int) Math.pow( 2, Math.round(( Math.log(longest_edge ) / Math.log( 2 )) + 0.499999 ));
		return sprite_edge;
		
	}
	
	
	
	public int getEdgeLength() {
	
		return textureEdge;
	}
	
}
