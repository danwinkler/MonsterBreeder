package monsterbreeder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import com.phyloa.dlib.util.DFile;

public class TextureHandler 
{
	static HashMap<String, BufferedImage> ims = new HashMap<String, BufferedImage>();
	
	public static BufferedImage get( String name )
	{
		BufferedImage im = ims.get( name );
		if( im == null )
		{
			try {
				im = DFile.loadImage( name );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ims.put( name, im );
		}
		return im;
	}
}
