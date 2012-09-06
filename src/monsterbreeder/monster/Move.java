package monsterbreeder.monster;

import monsterbreeder.BattleSystem;

public enum Move
{
	PUNCH( new MoveEffect() { public void use( Monster user, Monster target, BattleSystem bs ) { attack( user, target, Type.NORMAL, 10 ); } } );
	
	MoveEffect me;
	
	Move( MoveEffect me )
	{
		this.me = me;
	}
	
	static void attack( Monster user, Monster target, Type t, int power )
	{
		target.hp -= power;
	}
}

interface MoveEffect
{
	public void use( Monster user, Monster target, BattleSystem bs );
}