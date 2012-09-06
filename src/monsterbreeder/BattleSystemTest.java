package monsterbreeder;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import monsterbreeder.monster.MonsterBuilder;

import com.phyloa.dlib.renderer.Graphics2DRenderer;
import com.phyloa.dlib.util.DGraphics;

public class BattleSystemTest extends Graphics2DRenderer
{
	BattleSystem bs;
	
	BufferedImage buffer;
	
	public void initialize()
	{
		size( 800, 600 );
		bs = new BattleSystem();
		bs.start( MonsterBuilder.generateMonster( new Random().nextInt() ), MonsterBuilder.generateMonster( new Random().nextInt() ) );
		
		buffer = DGraphics.createBufferedImage( 200, 150 );
	}

	public void update() 
	{
		bs.update();
		
		Graphics2D g2 = buffer.createGraphics();
		bs.render( g2 );
		g2.dispose();
		
		g.drawImage( buffer, 0, 0, 800, 600, 0, 0, 200, 150, null );
	}
	
	
	public static void main( String[] args )
	{
		BattleSystemTest bst = new BattleSystemTest();
		bst.begin();
	}
}
