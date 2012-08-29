package monsterbreeder;

import java.awt.Graphics2D;

public class BattleSystem 
{	
	MonsterBattleWrapper m1;
	MonsterBattleWrapper m2;
	
	public void start( MonsterOld2 mm1, MonsterOld2 mm2 )
	{
		this.m1 = new MonsterBattleWrapper( mm1 );
		this.m2 = new MonsterBattleWrapper( mm2 );
		m1.x = 100;
	}
	
	public void update()
	{
		m1.update();
		m2.update();
	}
	
	public void render( Graphics2D g )
	{
		if( m1.y > m2.y )
		{
			m1.render( g );
			m2.render( g );
		} else
		{
			m2.render( g );
			m1.render( g );
		}
	}
	
	class MonsterBattleWrapper
	{
		MonsterOld2 m;
		float x;
		float y;
		float dx;
		float dy;
		
		public MonsterBattleWrapper( MonsterOld2 m )
		{
			this.m = m;
		}

		public void update() 
		{
			
		}
		
		public void render( Graphics2D g )
		{
			
		}
	}
}
