package monsterbreeder.monster;

import java.util.Random;

public enum Type 
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
	
	Type( int r, int g, int b )
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	int r, g, b;
	
	public static Type getRandom( Random r )
	{
		int pick = r.nextInt(values().length);
	    return values()[pick];
	}
}
