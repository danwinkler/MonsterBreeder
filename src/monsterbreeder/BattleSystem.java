package monsterbreeder;

import java.awt.Graphics2D;

public class BattleSystem 
{	
	MonsterBattleWrapper m1;
	MonsterBattleWrapper m2;
	
	public void start( Monster m1, Monster m2 )
	{
		this.m1 = new MonsterBattleWrapper( m1 );
		this.m2 = new MonsterBattleWrapper( m2 );
	}
	
	public void update()
	{
		
	}
	
	public void render( Graphics2D g )
	{
		
	}
	
	class MonsterBattleWrapper
	{
		Monster m;
		float x;
		float y;
		float dx;
		float dy;
		
		public MonsterBattleWrapper( Monster m )
		{
			this.m = m;
		}
	}
}
