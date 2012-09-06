package monsterbreeder;

import java.awt.Font;
import java.io.IOException;

import monsterbreeder.MonsterOld2.Energy;
import monsterbreeder.monster.Monster;
import monsterbreeder.monster.MonsterBuilder;

import com.phyloa.dlib.renderer.Graphics2DRenderer;
import com.phyloa.dlib.util.DFile;
import com.phyloa.dlib.util.DMath;

import monsterbreeder.monster.Type;

public class MonsterViewer extends Graphics2DRenderer
{
	Monster m;
	
	public void initialize() 
	{
		size( 800, 600 );
		m = MonsterBuilder.generateMonster( (int)(Math.random() * Integer.MAX_VALUE) );
	}

	public void update()
	{
		if( k.space )
		{
			k.space = false;
			m = MonsterBuilder.generateMonster( (int)(Math.random() * Integer.MAX_VALUE) );
		}
		color( 255, 255, 255 );
		fillRect( 0, 0, this.getWidth(), this.getHeight() );
		
		scale( 4, 4 );
		
		pushMatrix();
		translate( 20, 20 );
		g.drawImage( m.getFront(), 0, 0, null );
		popMatrix();
		
		if( k.space )
		{
			k.space = false;
		}
		
		g.setFont( new Font( "Consolas", Font.PLAIN, 4 ) );
		
		for( int i = 0; i < Type.values().length; i++ )
		{
			Type e = Type.values()[i];
			color( 0, 0, 0 );
			fillRect( (i * 20)-1, 100-1, 15+2, 15+2 );
			color( e.r, e.g, e.b );
			fillRect( i * 20, 100, 15, 15 );
			color( 255 - e.r, 255 - e.g, 255 - e.b );
			pushMatrix();
			translate( i*20, 100 );
			rotate( DMath.PIF * 1.5f );
			text( e.name(), -15, 10 );
			popMatrix();
		}
	}
	
	public static void main( String[] args ) throws IOException
	{
		MonsterViewer mv = new MonsterViewer();
		mv.begin();
	}
}
