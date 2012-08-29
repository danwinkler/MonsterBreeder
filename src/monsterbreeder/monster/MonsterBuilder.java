package monsterbreeder.monster;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point3i;

import org.dom4j.DocumentException;

import monsterbreeder.ConfigLoader;
import monsterbreeder.monster.Monster.Part;

public class MonsterBuilder 
{
	static ArrayList<PartSpec> parts = new ArrayList<PartSpec>();
	
	static {
		try {
			ConfigLoader.loadParts( "monster/monsterparts.xml", parts );
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Monster generateMonster( int seed )
	{
		Random r = new Random( seed );
		Monster m = new Monster();
		m.type = Type.values()[ r.nextInt( Type.values().length )];
		
		ArrayList<PartSpec> bodies = getParts( PartType.BODY, m.type );
		
		m.body = bodies.get( r.nextInt( bodies.size() ) ).makePart( m );
		
		return m;
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
		
		public Part makePart( Monster m )
		{
			Part p = m.new Part();
			p.front = front;
			p.side = side;
			p.rear = rear;
			return p;
		}
	}
	
	public static class AttachSpec
	{
		public int group = -1;
		public PartType[] allowPartType;
		public PartType[] disallowPartType;
		public Point3i p;
		public boolean flipX;
		public boolean flipY;
		public boolean flipZ;
	}
}
