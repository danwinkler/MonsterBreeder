package monsterbreeder.monster;

import com.phyloa.dlib.util.DMath;

import monsterbreeder.BattleSystem;

public enum Move
{
	PUNCH( new PhysicalDamageEffect( 1 ) );
	
	MoveEffect me;
	
	Move( MoveEffect me )
	{
		this.me = me;
	}
	
	public String use( Monster user, Monster target, BattleSystem bs, boolean player )
	{
		return me.use( user, target, bs, player );
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