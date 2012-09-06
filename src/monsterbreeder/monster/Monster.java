package monsterbreeder.monster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import javax.vecmath.Point3i;

import monsterbreeder.ColorTintFilter;
import monsterbreeder.ConfigLoader;

import org.dom4j.DocumentException;
import org.dom4j.Node;

import com.phyloa.dlib.util.DGraphics;
import com.phyloa.dlib.util.DMath;

public class Monster 
{
	public Part body;
	
	public BufferedImage front;
	public BufferedImage side;
	public BufferedImage rear;
	
	public Type type;
	public ArrayList<Move> possibleMoves = new ArrayList<Move>();
	public Move[] moves = new Move[4];
	public int maxhp = 100;
	public int hp = maxhp;
	
	public String name = "No Entry";
	
	public class Part
	{
		Point3i pos;
		ArrayList<Part> parts = new ArrayList<Part>();
		public BufferedImage front;
		public BufferedImage side;
		public BufferedImage rear;
		
		public ArrayList<Part> getParts()
		{
			ArrayList<Part> ret = new ArrayList<Part>();
			for( Part p : parts )
			{
				ret.add( p );
				ret.addAll( p.getParts() );
			}
			return ret;
		}

		public void renderFront( Graphics2D g, BufferedImageOp imageFilter )
		{
			AffineTransform at = g.getTransform();
			g.translate( pos.x, pos.y );
			g.drawImage( imageFilter.filter( front, null ), -front.getWidth()/2, -front.getHeight()/2, null );
			g.setTransform( at );
		}
	}
	
	public BufferedImage getFront()
	{
		if( front == null )
		{
			BufferedImageOp imageFilter = new ColorTintFilter( new Color( type.r, type.g, type.b ), .5f );
			front = DGraphics.createBufferedImage( 64, 64 );
			Graphics2D g = front.createGraphics();
			g.translate( 32, 32 );
			ArrayList<Part> parts = new ArrayList<Part>();
			parts.add( body );
			parts.addAll( body.getParts() );
			Collections.sort( parts, new Comparator<Part>() {
				public int compare( Part p1, Part p2 )
				{
					return p1.pos.z > p2.pos.z ? -1 : 1;
				} 
			});
			
			for( Part p : parts )
			{
				p.renderFront( g, imageFilter );
			}
			g.dispose();
		}
		return front;
	}
}
