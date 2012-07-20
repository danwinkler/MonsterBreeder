package monsterbreeder;

import java.util.HashMap;
import java.util.Random;

import monsterbreeder.Monster.Energy;
public class MonsterNamer 
{
	static HashMap<Energy, String[]> ePrefs = new HashMap<Energy, String[]>();
	
	static 
	{
		ePrefs.put( Energy.FIRE, new String[] { "Fir", "Flam", "Flar", "Smelt", "Burn" } );
		ePrefs.put( Energy.ELECTRIC, new String[] { "Shock", "Bolt" } );
		ePrefs.put( Energy.WATER, new String[] { "Vapor", "Wat", "Gush", "Liqui" } );
		ePrefs.put( Energy.AIR, new String[] { "Gust", "Wind" } );
		ePrefs.put( Energy.EARTH, new String[] { "Rock", "Hard" } );
		ePrefs.put( Energy.SHADOW, new String[] { "Dark", "Nil" } );
		ePrefs.put( Energy.PLANT, new String[] { "Grow", "Leaf", "Ivy", "Vin" } );
		ePrefs.put( Energy.LIFE, new String[] { "Heal", "Lif" } );
		ePrefs.put( Energy.ARCANE, new String[] { "Mag", "Power" } );
	}
	
	static String[] leg2Suf = { "eman", "boy", "en" };
	static String[] leg4Suf = { "adon" };
	static String[] wingSuf = { "bird", "sky" };
	static String[] leg0Suf = { "ball", "worm" };
	
	public static String getName( Monster m )
	{
		String[] prefs = ePrefs.get( m.type );
		boolean hasWings = false;
		int numLegs = 0;
		int numArms = 0;
		String seed = m.head.toString() + m.body.toString();
		for( int i = 0; i < m.limbs.length; i++ )
		{
			if( m.limbs[i] != null )
			{
				if( m.limbs[i].toString().startsWith( "wing" ) )
				{
					hasWings = true;
				}
				if( m.limbs[i].toString().startsWith( "leg" ) )
				{
					numLegs += 2;
				}
				if( m.limbs[i].toString().startsWith( "arm" ) )
				{
					numArms += 2;
				}
			}
		}
		Random r = new Random( seed.hashCode() );
		
		if( hasWings )
		{
			return prefs[r.nextInt(prefs.length)] + wingSuf[r.nextInt(wingSuf.length)];
		}
		if( numLegs == 0 )
		{
			return prefs[r.nextInt(prefs.length)] + leg0Suf[r.nextInt(leg0Suf.length)];
		}
		if( numLegs == 2 )
		{
			return prefs[r.nextInt(prefs.length)] + leg2Suf[r.nextInt(leg2Suf.length)];
		}
		if( numLegs == 4 )
		{
			return prefs[r.nextInt(prefs.length)] + leg4Suf[r.nextInt(leg4Suf.length)];
		}
		
		return "NONAME";
	}

}
