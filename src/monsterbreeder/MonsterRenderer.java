package monsterbreeder;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.vecmath.Point3i;

import monsterbreeder.MonsterOld2.Side;

public class MonsterRenderer 
{
	static ArrayList<Part> parts = new ArrayList<Part>();
	
	public static void render( Graphics2D g, Side side )
	{
		for( Part p : parts )
		{
			p.side = side;
		}
		
		Collections.sort( parts );
		
		for( Part p : parts )
		{
			switch( side )
			{
			case FRONT:
				g.drawImage( p.im, p.p.x - (p.im.getWidth()/2), p.p.y - (p.im.getHeight()/2), null );
				break;
			case SIDE:
				g.drawImage( p.im, p.p.z - (p.im.getWidth()/2), p.p.y - (p.im.getHeight()/2), null );
				break;
			case BACK:
				AffineTransform at = g.getTransform();
				g.translate( -p.p.x, p.p.y );
				g.drawImage( p.im, -(p.im.getWidth()/2), -(p.im.getHeight()/2), null );
				g.setTransform( at );
				break;
			}
		}
	}
	
	public static void clear()
	{
		parts.clear();
	}
	
	public static void add( BufferedImage im, Point3i p )
	{
		parts.add( new Part( im, p ) );
	}
	
	static class Part implements Comparable<Part> 
	{
		BufferedImage im;
		Point3i p;
		Side side;
		
		Part( BufferedImage im, Point3i p )
		{
			this.im = im;
			this.p = p;
		}

		public int compareTo( Part o ) 
		{
			switch( side )
			{
			case FRONT:
				return o.p.z > p.z ? 1 : -1;
			case SIDE:
				return o.p.x > p.x ? -1 : 1;
			case BACK:
				return o.p.z < p.z ? 1 : -1;
			}
			return 0;
		}
	}
}
