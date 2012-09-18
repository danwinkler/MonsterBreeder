package monsterbreeder.monster;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

import javax.vecmath.Point3i;

import org.dom4j.DocumentException;

import monsterbreeder.ConfigLoader;
import monsterbreeder.monster.Monster.Part;

public class MonsterBuilder 
{
	static ArrayList<PartSpec> parts = new ArrayList<PartSpec>();
	
	static ArrayList<Move> tempAllowedMoves = new ArrayList<Move>();
	
	public static Monster generateMonster( int seed )
	{
		tempAllowedMoves.clear();
		if( parts.size() == 0 )
		{
			try
			{
				ConfigLoader.loadParts( "monster/monsterparts.xml", parts );
			} catch( DocumentException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Random r = new Random( seed );
		Monster m = new Monster();
		m.type = Type.values()[ r.nextInt( Type.values().length )];
		
		ArrayList<PartSpec> bodies = getParts( PartType.BODY, m.type );
		PartSpec ps = bodies.get( r.nextInt( bodies.size() ) );
		m.body = ps.makePart( m, null );
		
		fillChildren( m, ps, m.body, m.body.pos, r );
		
		tempAllowedMoves.add( Move.PUNCH );
		
		for( int i = 0; i < 4; i++ )
		{
			m.moves[i] = tempAllowedMoves.remove( r.nextInt( tempAllowedMoves.size() ) );
			if( tempAllowedMoves.size() == 0 )
			{
				break;
			}
		}
		
		return m;
	}
	
	private static void fillChildren( Monster m, PartSpec parentPS, Part parent, Point3i pos, Random r )
	{
		for( int i = 0; i < parentPS.attaches.size(); i++ )
		{
			AttachSpec as = parentPS.attaches.get( i );
			ArrayList<PartSpec> pss = getParts( as, m.type );
			if( pss.size() > 0 )
			{
				PartSpec ps = null;
				if( as.group != null )
				{
					for( Part part : parent.parts )
					{
						if( as.group.equals( part.group ) )
						{
							ps = part.ps;
						}
					}
				}
				if( ps == null )
				{
					ps = pss.get( r.nextInt( pss.size() ) );
				}
				
				//Fill moves
				if( ps.allowMoves != null )
				{
					for( String move : ps.allowMoves )
					{
						Move mo = Move.getByName( move );
						if( !tempAllowedMoves.contains( mo ) )
						{
							tempAllowedMoves.add( mo );
						}
					}
				}
				
				Point3i p = new Point3i( pos );
				p.add( as.p );
				Part child = ps.makePart( m, p, as );
				parent.parts.add( child );
				fillChildren( m, ps, child, child.pos, r );
			}
		}
	}
	
	private static ArrayList<PartSpec> getParts( AttachSpec as, Type type )
	{
		ArrayList<PartSpec> ret = new ArrayList<PartSpec>();
		
		for( PartSpec p : parts )
		{
			boolean pass;
			
			//TEST PART ALLOWS TYPE
			if( p.allowType != null )
			{
				pass = false;
				for( Type t : p.allowType )
				{
					if( t == type )
					{
						pass = true;
					}
				}
				if( !pass )
				{
					continue;
				}
			}
			
			//TEST PART DISALLOWS TYPE
			if( p.disallowType != null )
			{
				pass = true;
				for( Type t : p.disallowType )
				{
					if( t == type )
					{
						pass = false;
					}
				}
				if( !pass )
				{
					continue;
				}
			}
			
			//TEST ATTACHSPEC ALLOWS PARTTYPE
			if( as.allowPartType != null )
			{
				pass = false;
				for( PartType pt : as.allowPartType )
				{
					if( pt == p.type )
					{
						pass = true;
					}
				}
				if( !pass )
				{
					continue;
				}
			}
			
			//TEST ATTACHSPEC DISALLOWS PARTTYPE
			if( as.disallowPartType != null )
			{
				pass = true;
				for( PartType pt : as.disallowPartType )
				{
					if( pt == p.type )
					{
						pass = false;
					}
				}
				if( !pass )
				{
					continue;
				}
			}
			ret.add( p );
		}
		
		return ret;
	}

	public static ArrayList<PartSpec> getParts( PartType pt, Type t )
	{
		ArrayList<PartSpec> ret = getParts( pt );
		
		for( int i = 0; i < ret.size(); i++ )
		{
			PartSpec ps = ret.get( i );
			if( ps.allowType != null )
			{
				boolean passed = false;
				for( Type tt : ps.allowType )
				{
					if( tt == t )
					{
						passed = true;
						break;
					}
				}
				if( !passed )
				{
					ret.remove( ps );
					i--;
					continue;
				}
			}
			if( ps.disallowType != null )
			{
				for( Type tt : ps.allowType )
				{
					if( tt == t )
					{
						ret.remove( ps );
						i--;
						break;
					}
				}
			}
		}
		return ret;
	}
	
	public static ArrayList<PartSpec> getParts( PartType pt )
	{
		ArrayList<PartSpec> ret = new ArrayList<PartSpec>();
		
		for( PartSpec p : parts )
		{
			if( p.type == pt )
			{
				ret.add( p );
			}
		}
		return ret;
	}
	
	public static class PartSpec
	{
		public BufferedImage front;
		public BufferedImage side;
		public BufferedImage rear;
		
		public Point3i base;
		public ArrayList<AttachSpec> attaches = new ArrayList<AttachSpec>();
		public String name;
		public PartType type;
		public Type[] allowType;
		public Type[] disallowType;
		public String[] allowMoves;
		
		public Part makePart( Monster m, AttachSpec as )
		{
			Part p = m.new Part();
			p.front = front;
			p.side = side;
			p.rear = rear;
			p.pos = new Point3i( base );
			p.ps = this;
			if( as != null )
			{
				p.flipX = as.flipX;
				p.flipY = as.flipY;
				p.flipZ = as.flipZ;
				p.group = as.group;
				p.as = as;
			}
			
			return p;
		}
		
		public Part makePart( Monster m, Point3i pos, AttachSpec as )
		{
			Part p = makePart( m, as );
			p.pos.add( pos );
			return p;
		}
	}
	
	public static class AttachSpec
	{
		public String group = null;
		public PartType[] allowPartType;
		public PartType[] disallowPartType;
		public Point3i p;
		public boolean flipX;
		public boolean flipY;
		public boolean flipZ;
	}
}
