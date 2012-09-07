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
					Thread.sleep( 500 );
				} catch( InterruptedException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rm.dsh.activate( "battle" );
				((BattleSystem)rm.dsh.get( "battle" )).start( MonsterBuilder.generateMonster( new Random().nextInt() ), MonsterBuilder.generateMonster( new Random().nextInt() ) );
			} } ).start();
		rm.begin();
	}
}
