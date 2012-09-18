package monsterbreeder.monster;

import com.phyloa.dlib.util.DMath;

import monsterbreeder.BattleSystem;

public enum Move
{
	PUNCH( "PUNCH", new PhysicalDamageEffect( 1 ) ),
	HORNDRILL( "HORN DRILL", new PhysicalDamageEffect( 2 ) );
	
	MoveEffect me;
	String name;
	
	Move( String name, MoveEffect me )
	{
		this.name = name;
		this.me = me;
	}
	
	public String use( Monster user, Monster target, BattleSystem bs, boolean player )
	{
		return me.use( user, target, bs, player );
	}
	
	public String getName()
	{
		return name;
	}
	
	public static Move getByName( String name )
	{
		for( Move m : values() )
		{
			if( m.getName().equalsIgnoreCase( name ) )
			{
				return m;
			}
		}
		return null;
	}
}

interface MoveEffect
{
	public String use( Monster user, Monster target, BattleSystem bs, boolean player );
}

class PhysicalDamageEffect implements MoveEffect
{
	int power = 10;
	
	public PhysicalDamageEffect( int power )
	{
		this.power = power;
	}
	
	public String use( Monster user, Monster target, BattleSystem bs, boolean player )
	{
		if( DMath.randomi( 0, 100 ) < user.accuracy )
		{
			int damage = (user.attack*power) - (target.defense/5);
			target.hp -= damage;
			return (player ? "" : "Enemy ") + user.name + " hit " + (!player ? "" : "enemy ") + target.name + " \nfor " + damage + " damage!";
		}
		else
		{
			return (player ? "" : "Enemy ") + user.name + " missed!";
		}
	}
}