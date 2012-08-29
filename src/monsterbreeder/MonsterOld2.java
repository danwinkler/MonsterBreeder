package monsterbreeder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point2i;
import javax.vecmath.Point3i;

import com.phyloa.dlib.util.DGraphics;
import com.phyloa.dlib.util.DMath;

public class MonsterOld2 
{
	Body body;
	Head head;
	int r, g, b;
	
	Limb[] limbs;
	
	Energy type;
	
	BufferedImage front, side, back;
	
	String name;
	
	String[] moves;
	
	public void renderFront( Graphics2D g2 )
	{
		BufferedImageOp imageFilter = new ColorTintFilter( new Color( r, g, b ), .5f );
		
		MonsterRenderer.add( imageFilter.filter( body.frontTex, null ), new Point3i( 0, 0, 0 ) );
		MonsterRenderer.add( imageFilter.filter( head.frontTex, null ), body.head.pos );
		for( int i = 0; i < limbs.length; i++ )
		{
			if( limbs[i] != null )
			{
				Attach attach = body.attaches.get( i );
				BufferedImage tFront = imageFilter.filter( limbs[i].frontTex, null );
				MonsterRenderer.add( tFront, attach.pos );
				
				if( attach.mirror == Axis.X )
				{
					Point3i pos = new Point3i( attach.pos );
					pos.x *= -1;
					MonsterRenderer.add( DGraphics.flip( tFront, true, false ), pos );
				} 
				else if( attach.mirror == Axis.Y )
				{
					Point3i pos = new Point3i( attach.pos );
					pos.y *= -1;
					MonsterRenderer.add( DGraphics.flip( tFront, false, true ), pos );
				}
			}
		}
		MonsterRenderer.render( g2, Side.FRONT );
		MonsterRenderer.clear();
	}
	
	public void renderSide( Graphics2D g2 )
	{
		BufferedImageOp imageFilter = new ColorTintFilter( new Color( r, g, b ), 0.5f);
		
		MonsterRenderer.add( imageFilter.filter( head.sideTex, null ), body.head.pos );
		MonsterRenderer.add( imageFilter.filter( body.sideTex, null ), new Point3i( 0, 0, 0 ) );
		for( int i = 0; i < limbs.length; i++ )
		{
			if( limbs[i] != null )
			{
				Attach attach = body.attaches.get( i );
				BufferedImage t = imageFilter.filter( limbs[i].sideTex, null );
				MonsterRenderer.add( t, attach.pos );
				
				if( attach.mirror == Axis.Z )
				{
					Point3i pos = new Point3i( attach.pos );
					pos.z *= -1;
					MonsterRenderer.add( DGraphics.flip( t, true, false ), pos );
				} 
				else if( attach.mirror == Axis.Y )
				{
					Point3i pos = new Point3i( attach.pos );
					pos.y *= -1;
					MonsterRenderer.add( DGraphics.flip( t, false, true ), pos );
				}
			}
		}
		MonsterRenderer.render( g2, Side.SIDE );
		MonsterRenderer.clear();
	}
	
	public void renderBack( Graphics2D g2 )
	{
		BufferedImageOp imageFilter = new ColorTintFilter( new Color( r, g, b ), 0.5f );
		
		MonsterRenderer.add( imageFilter.filter( body.backTex, null ), new Point3i( 0, 0, 0 ) );
		MonsterRenderer.add( imageFilter.filter( head.backTex, null ), body.head.pos );
		for( int i = 0; i < limbs.length; i++ )
		{
			if( limbs[i] != null )
			{
				Attach attach = body.attaches.get( i );
				BufferedImage t = imageFilter.filter( limbs[i].backTex, null );
				MonsterRenderer.add( t, attach.pos );
				
				if( attach.mirror == Axis.X )
				{
					Point3i pos = new Point3i( attach.pos );
					pos.x *= -1;
					MonsterRenderer.add( DGraphics.flip( t, true, false ), pos );
				} 
				else if( attach.mirror == Axis.Y )
				{
					Point3i pos = new Point3i( attach.pos );
					pos.y *= -1;
					MonsterRenderer.add( DGraphics.flip( t, false, true ), pos );
				}
			}
		}
		MonsterRenderer.render( g2, Side.BACK );
		MonsterRenderer.clear();
	}
	
	public static MonsterOld2 generate( long seed )
	{
		MonsterOld2 m = new MonsterOld2();
		Random r = new Random( seed );
		m.body = Body.getRandom( r );
		m.head = Head.getRandom( r );
		
		m.type = Energy.getRandom( r );
		
		m.r = DMath.bound( (r.nextInt( 10 ) - 5) + m.type.r, 0, 255 );
		m.g = DMath.bound( (r.nextInt( 10 ) - 5) + m.type.g, 0, 255 );
		m.b = DMath.bound( (r.nextInt( 10 ) - 5) + m.type.b, 0, 255 );
		
		m.limbs = new Limb[ m.body.attaches.size() ];
		
		for( int i = 0; i < m.limbs.length; i++ )
		{
			if( r.nextFloat() > .25f )
			{
				m.limbs[i] = Limb.getRandom( r );
				int counter = 0;
				while( !m.body.attaches.get( i ).allows( m.limbs[i] ) || !m.limbs[i].allows( m.type ) )
				{
					m.limbs[i] = Limb.getRandom( r );
					counter++;
					if( counter > 20 )
					{
						m.limbs[i] = null;
						break;
					}
				}	
			}
		}
		
		m.name = MonsterNamer.getName( m );
		
		m.front = DGraphics.createBufferedImage( 50, 50 );
		Graphics2D g2 = m.front.createGraphics();
		g2.translate( 25, 25 );
		m.renderFront( g2 );
		g2.dispose();
		
		m.side = DGraphics.createBufferedImage( 50, 50 );
		g2 = m.side.createGraphics();
		g2.translate( 25, 25 );
		m.renderSide( g2 );
		g2.dispose();
		
		m.back = DGraphics.createBufferedImage( 50, 50 );
		g2 = m.back.createGraphics();
		g2.translate( 25, 25 );
		m.renderBack( g2 );
		g2.dispose();
		
		return m;
	}

	public static MonsterOld2 breed( MonsterOld2 ped, MonsterOld2 alt )
	{
		MonsterOld2 m = new MonsterOld2();
		Random r = new Random();
		
		
		return m;
	}
	
	public static enum Body
	{
		A( "A", 30, 30, 30, new Attach( 0, 0, -13 ), new Attach( 10, 11, -1, Axis.X, "leg" ), new Attach( 13, 0, -1, Axis.X ) ),
		B( "B", 20, 30, 20, new Attach( 0, -7, -8 ), new Attach( 7, 11, -1, Axis.X, "leg" ), new Attach( 8, 0, -1, Axis.X ) ),
		C( "C", 20, 20, 40, new Attach( 0, 0, -18 ), new Attach( 7, 7, -15, Axis.X, "leg" ), new Attach( 7, 7, 15, Axis.X, "leg" ), new Attach( 7, -7, 0, Axis.X, "wing" ) );
		
		Body( String name, int width, int height, int length, Attach head, Attach... attaches  )
		{
			this.width = width;
			this.height = height;
			this.length = length;
			frontTex = TextureHandler.get( "monster/body/" + name + "/front.png" );
			sideTex = TextureHandler.get( "monster/body/" + name + "/side.png" );
			backTex = TextureHandler.get( "monster/body/" + name + "/back.png" );
			this.head = head;
			for( int i = 0; i < attaches.length; i++ )
			{
				this.attaches.add( attaches[i] );
			}
		}
		
		int width, height, length;
		BufferedImage frontTex;
		BufferedImage sideTex;
		BufferedImage backTex;
		Attach head;
		ArrayList<Attach> attaches = new ArrayList<Attach>();
		
		public static Body getRandom( Random r )
		{
			int pick = r.nextInt(values().length);
		    return values()[pick];
		}
	}
	
	public static class Attach
	{
		Point3i pos;
		Point2i front;
		Point2i side;
		Point2i back;
		Axis mirror;
		String[] allowLimb;
		
		public Attach( int x, int y, int z, Axis mirror, String allowLimb )
		{
			pos = new Point3i( x, y, z );
			this.mirror = mirror;
			front = new Point2i( x, y );
			side = new Point2i( z, y );
			back = new Point2i( -x, y );
			this.allowLimb = allowLimb == null ? new String[] {} : allowLimb.split( "," );
		}
		
		public Attach( int x, int y, int z, Axis mirror )
		{
			this( x, y, z, mirror, null );
		}
		
		public Attach( int x, int y, int z )
		{
			this( x, y, z, null );
		}
		
		public Point2i getFront()
		{
			return front;
		}
		
		public Point2i getSide()
		{
			return side;
		}
		
		public Point2i getBack()
		{
			return back;
		}
		
		public boolean allows( Limb l )
		{
			if( allowLimb.length == 0 ) return true;
			for( int i = 0; i < allowLimb.length; i++ )
			{
				if( l.toString().startsWith( allowLimb[i] ) )
				{
					return true;
				}
			}
			return false;
		}
	}
	
	public static enum Head
	{
		A( "A", 10, 10, 8 ),
		B( "B", 30, 30, 30 );
		
		Head( String name, int width, int height, int length )
		{
			this.width = width;
			this.height = height;
			this.length = length;
			frontTex = TextureHandler.get( "monster/head/" + name + "/front.png" );
			sideTex = TextureHandler.get( "monster/head/" + name + "/side.png" );
			backTex = TextureHandler.get( "monster/head/" + name + "/back.png" );
		}
		
		int width, height, length;
		BufferedImage frontTex;
		BufferedImage sideTex;
		BufferedImage backTex;
		
		public static Head getRandom( Random r )
		{
			int pick = r.nextInt(values().length);
		    return values()[pick];
		}
	}
	
	enum Limb 
	{
		legA( "legA", 28, 20, 28 ),
		wingA( "wingA", 28, 20, 28, "AIR,FIRE,ARCANE" );
		
		Limb( String name, int width, int height, int length, String allowType )
		{
			this.width = width;
			this.height = height;
			this.length = length;
			frontTex = TextureHandler.get( "monster/limb/" + name + "/front.png" );
			sideTex = TextureHandler.get( "monster/limb/" + name + "/side.png" );
			backTex = TextureHandler.get( "monster/limb/" + name + "/back.png" );
			this.allowType = allowType == null ? new String[] {} : allowType.split( "," );
		}
		
		Limb( String name, int width, int height, int length )
		{
			this( name, width, height, length, null );
		}
		
		int width, height, length;
		BufferedImage frontTex;
		BufferedImage sideTex;
		BufferedImage backTex;
		String[] allowType;
		
		public static Limb getRandom( Random r )
		{
			int pick = r.nextInt(values().length);
		    return values()[pick];
		}
		
		public boolean allows( Energy e )
		{
			if( allowType.length == 0 ) return true;
			for( int i = 0; i < allowType.length; i++ )
			{
				if( e.toString().startsWith( allowType[i].toUpperCase() ) )
				{
					return true;
				}
			}
			return false;
		}
	}
	
	public static enum Axis
	{
		X,
		Y,
		Z;
	}
	
	public static enum Side
	{
		FRONT,
		SIDE,
		BACK;
	}
	
	public static enum Energy
	{
		FIRE( 230, 20, 10 ),
		ELECTRIC( 255, 255, 0 ),
		WATER( 0, 10, 128 ),
		AIR( 158, 168, 255),
		EARTH( 120, 60, 20),
		SHADOW( 30, 10, 50 ),
		PLANT( 30, 150, 50 ),
		LIFE( 235, 255, 255 ),
		ARCANE( 100, 20, 150 );
		
		Energy( int r, int g, int b )
		{
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		int r, g, b;
		
		public static Energy getRandom( Random r )
		{
			int pick = r.nextInt(values().length);
		    return values()[pick];
		}
	}
}
