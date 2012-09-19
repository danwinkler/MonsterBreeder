package monsterbreeder.monster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.vecmath.Point3i;

import monsterbreeder.ColorTintFilter;
import monsterbreeder.monster.MonsterBuilder.AttachSpec;
import monsterbreeder.monster.MonsterBuilder.PartSpec;

import com.phyloa.dlib.util.DGraphics;

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
	public int accuracy = 95;
	public int attack = 10;
	public int defense = 10;
	public int speed = 10;
	
	public String name = "No Entry";
	
	public class Part
	{
		Point3i pos;
		ArrayList<Part> parts = new ArrayList<Part>();
		public BufferedImage front, side, rear;
		public boolean flipX, flipY, flipZ;
		String group;
		PartSpec ps;
		AttachSpec as;
		
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

		public void renderFront( Graphics2D g, BufferedImageOp imageFilter, Part p )
		{
			if( front != null )
			{
				AffineTransform at = g.getTransform();
				g.translate( pos.x, pos.y );
				g.drawImage( imageFilter.filter( front, null ), -front.getWidth()/2 * (p.flipX ? -1 : 1), -front.getHeight()/2 * (p.flipY ? -1 : 1), 
																front.getWidth()/2 * (p.flipX ? -1 : 1), front.getHeight()/2 * (p.flipY ? -1 : 1),
																0, 0, 
																front.getWidth(), front.getHeight(), 
																null );
				g.setTransform( at );
			}
		}
		
		public void renderRear( Graphics2D g, BufferedImageOp imageFilter, Part p )
		{
			if( rear != null )
			{
				AffineTransform at = g.getTransform();
				g.translate( -pos.x, pos.y );
				g.drawImage( imageFilter.filter( rear, null ), -rear.getWidth()/2 * (p.flipX ? -1 : 1), -rear.getHeight()/2 * (p.flipY ? -1 : 1), 
																rear.getWidth()/2 * (p.flipX ? -1 : 1), rear.getHeight()/2 * (p.flipY ? -1 : 1),
																0, 0, 
																rear.getWidth(), rear.getHeight(), 
																null );
				g.setTransform( at );
			}
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
				p.renderFront( g, imageFilter, p );
			}
			g.dispose();
		}
		return front;
	}
	
	public BufferedImage getRear()
	{
		if( rear == null )
		{
			BufferedImageOp imageFilter = new ColorTintFilter( new Color( type.r, type.g, type.b ), .5f );
			rear = DGraphics.createBufferedImage( 64, 64 );
			Graphics2D g = rear.createGraphics();
			g.translate( 32, 32 );
			ArrayList<Part> parts = new ArrayList<Part>();
			parts.add( body );
			parts.addAll( body.getParts() );
			Collections.sort( parts, new Comparator<Part>() {
				public int compare( Part p1, Part p2 )
				{
					return p1.pos.z > p2.pos.z ? 1 : -1;
				} 
			});
			
			for( Part p : parts )
			{
				p.renderRear( g, imageFilter, p );
			}
			g.dispose();
		}
		return rear;
	}
	
	public String[] getMoveNames()
	{
		String[] ms = new String[4];
		for( int i = 0; i < 4; i++ )
		{
			if( moves[i] != null )
				ms[i] = moves[i].getName();
		}
		return ms;
	}
}
