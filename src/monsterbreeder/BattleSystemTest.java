package monsterbreeder;

import java.util.Random;

import monsterbreeder.monster.MonsterBuilder;

public class BattleSystemTest
{
	public static void main( String[] args )
	{
		final RunMonster rm = new RunMonster();
		new Thread( new Runnable() {
			public void run()
			{
				try
				{
					Thread.sleep( 1000 );
				} catch( InterruptedException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((BattleSystem)rm.dsh.get( "battle" )).start( MonsterBuilder.generateMonster( new Random().nextInt() ), MonsterBuilder.generateMonster( new Random().nextInt() ) );
				rm.dsh.activate( "battle" );
				while( true )
				{
					try
					{
						Thread.sleep( 100 );
					} catch( InterruptedException e )
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if( rm.k.r )
					{
						((BattleSystem)rm.dsh.get( "battle" )).start( MonsterBuilder.generateMonster( new Random().nextInt() ), MonsterBuilder.generateMonster( new Random().nextInt() ) );
					}
				}
			} } ).start();
		rm.begin();
	}
}
