package monsterbreeder;

import java.io.IOException;

import monsterbreeder.MonsterOld2.Energy;
import monsterbreeder.monster.Monster;
import monsterbreeder.monster.MonsterBuilder;

import com.phyloa.dlib.renderer.Graphics2DRenderer;
import com.phyloa.dlib.util.DFile;

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
		
		for( int i = 0; i < Energy.values().length; i++ )
		{
			Energy e = Energy.values()[i];
			color( 0, 0, 0 );
			fillRect( (20 + i * 20)-1, 100-1, 15+2, 15+2 );
			color( e.r, e.g, e.b );
			fillRect( 20 + i * 20, 100, 15, 15 );
		}
	}
	
	public static void main( String[] args ) throws IOException
	{
		MonsterViewer mv = new MonsterViewer();
		mv.begin();
	}
}
