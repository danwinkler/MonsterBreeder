package monsterbreeder;

import monsterbreeder.MonsterOld2.Energy;
import monsterbreeder.monster.Monster;

import com.phyloa.dlib.renderer.Graphics2DRenderer;

public class MonsterViewer extends Graphics2DRenderer
{
	Monster m;
	
	public void initialize() 
	{
		size( 800, 600 );
	}

	public void update()
	{
		color( 255, 255, 255 );
		fillRect( 0, 0, this.getWidth(), this.getHeight() );
		
		scale( 4, 4 );
		
		//drawImage( m.front, 0, 0 );
		//drawImage( m.side, 50, 0 );
		//drawImage( m.rear, 100, 0 );
		
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
	
	public static void main( String[] args )
	{
		MonsterViewer mv = new MonsterViewer();
		mv.begin();
	}
}
