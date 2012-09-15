package monsterbreeder;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import com.phyloa.dlib.renderer.DScreen;
import com.phyloa.dlib.renderer.Graphics2DRenderer;
import com.phyloa.dlib.util.DMath;
import com.phyloa.dlib.util.KeyHandler;

import monsterbreeder.GUI.SelectBox;
import monsterbreeder.GUI.Textbox;
import monsterbreeder.monster.Monster;
import monsterbreeder.monster.Move;

public class BattleSystem implements DScreen<RunMonster>
{	
	Battler player;
	Battler enemy;
	Monster m1;
	Monster m2;
	
	GUI gui;
	
	public void start( Monster m1, Monster m2 )
	{
		this.m1 = m1;
		this.m2 = m2;
		new Thread( new BattleThread() ).start();
		gui = new GUI();
	}
	
	public void update( RunMonster rm )
	{
		render( rm );
		gui.render( rm.g2, rm.k, rm.bufferx, rm.buffery );
	}
	
	public void render( RunMonster rm )
	{
		Graphics2D g = rm.g2;
		
		g.setFont( GUI.font8 );
		
		g.setColor( Color.WHITE );
		g.fillRect( 0, 0, 200, 150 );
		
		//ENEMY
		g.drawImage( m2.getFront(), 136, 0, null );
		g.setColor( Color.BLACK );
		g.drawString( m2.name, 145, 57 );
		g.drawLine( 140, 60, 190, 60 );
		int lineLength = DMath.bound( (int)((((float)m2.hp)/m2.maxhp) * 51), 0, 51 );
		g.setColor( lineLength > 15 ? Color.GREEN : Color.red );
		if( lineLength > 0 )
			g.drawLine( 140, 61, 139 + lineLength, 61 );
		
		//PLAYER
		g.drawImage( m1.getRear(), 10, 50, null );
		g.setColor( Color.BLACK );
		g.drawString( m1.name, 19, 107 );
		g.drawLine( 14, 110, 64, 110 );
		lineLength = DMath.bound( (int)((((float)m1.hp)/m1.maxhp) * 51), 0, 51 );
		g.setColor( lineLength > 15 ? Color.GREEN : Color.red );
		if( lineLength > 0 )
			g.drawLine( 14, 111, 13 + lineLength, 111 );
	}
	
	class BattleThread implements Runnable
	{
		BattleState s = BattleState.START;
		boolean battling = true;
		
		public void run()
		{
			while( battling )
			{
				switch( s )
				{
				case START:
					showMessage( "Battle has begun!" );
					s = BattleState.FIGHTING;
					break;
				case FIGHTING:
					int mainMenu = showChoices( "FIGHT", "PARTY", "ITEMS", "RUN" );
					switch( mainMenu )
					{
					case 0:
						int playerAttack = playerChooseMove();
						
						//Choose who goes first based on speed;
						Monster b1 = null, b2 = null;
						if( m1.speed > m2.speed )
						{
							b1 = m1;
							b2 = m2;
						} else if( m2.speed > m1.speed )
						{
							b1 = m2;
							b2 = m1;
						}
						else
						{
							if( Math.random() > .5 )
							{
								b1 = m1;
								b2 = m2;
							} else
							{
								b1 = m2;
								b2 = m1;
							}
						}
						
						//Monster1 attack
						int moves = b1 == m1 ? playerAttack : enemyChooseMove();
						String message = b1.moves[moves].use( b1, b2, BattleSystem.this, b1 == m1 );
						showMessage( message );
						
						if( b2.hp < 0 )
						{
							s = b2 == m1 ? BattleState.DEADPLAYER : BattleState.DEADENEMY;
							break;
						}
						if( b1.hp < 0 )
						{
							s = b1 == m1 ? BattleState.DEADPLAYER : BattleState.DEADENEMY;
							break;
						}
						
						//Monster2 attack
						moves = b2 == m1 ? playerAttack : enemyChooseMove();
						message = b2.moves[moves].use( b2, b1, BattleSystem.this, b2 == m1 );
						showMessage( message );
						
						if( b2.hp < 0 )
						{
							s = b2 == m1 ? BattleState.DEADPLAYER : BattleState.DEADENEMY;
							break;
						}
						if( b1.hp < 0 )
						{
							s = b1 == m1 ? BattleState.DEADPLAYER : BattleState.DEADENEMY;
							break;
						}
						
						break;
					}
					break;
				case DEADPLAYER:
					showMessage( m1.name + " was knocked out!" );
					s = BattleState.LOSS;
					break;
				case DEADENEMY:
					showMessage( "Enemy " + m2.name + " was knocked out!" );
					s = BattleState.WIN;
					break;
				case LOSS:
					showMessage( "You lost the battle." );
					break;
				case WIN:
					showMessage( "You won the battle!" );
				}
			}
		}
		
		public int playerChooseMove()
		{
			return showChoices( m1.getMoveNames() );
		}
		
		public int enemyChooseMove()
		{
			int m;
			do
			{
				m = DMath.randomi( 0, m2.moves.length );
			} while( m2.moves[m] == null );
			return m;
		}
		
		public int showChoices( String... c )
		{
			SelectBox se = gui.new SelectBox( c );
			gui.pushElement( se );
			while( se.alive )
			{
				try
				{
					Thread.sleep( 15 );
				} catch( InterruptedException e )
				{
					e.printStackTrace();
				}
			}
			return se.choice;
		}
		
		public void showMessage( String msg )
		{
			Textbox se = gui.new Textbox( msg );
			gui.pushElement( se );
			while( se.alive )
			{
				try
				{
					Thread.sleep( 15 );
				} catch( InterruptedException e )
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	enum BattleState
	{
		START, 
		FIGHTING, 
		DEADPLAYER, 
		DEADENEMY,
		LOSS,
		WIN;
	}
}
