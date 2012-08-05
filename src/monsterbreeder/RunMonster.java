package monsterbreeder;

import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;

import com.danwink.java.rpg.Map;
import com.danwink.java.rpg.MapFileHelper;
import com.danwink.java.rpg.MapObject;
import com.danwink.java.rpg.MapObject.Face;
import com.danwink.java.rpg.Tileset;
import com.phyloa.dlib.renderer.Graphics2DRenderer;
import com.phyloa.dlib.util.DGraphics;

public class RunMonster extends Graphics2DRenderer
{
	Map map;
	
	int time = 0;
	
	BufferedImage buffer;
	
	MapObject player;
	
	public void initialize()
	{
		size( 800, 600 );
		
		try {
			map = MapFileHelper.loadMap( new File( "maps/map1.xml" ) );
			Tileset tc = MapFileHelper.loadTileConfig( new File( map.configFile ) );
			map.setTileset( tc );
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		map.prepareAutoTilesState();
		
		player = new MapObject( map, null, 0, 0 );
		
		createBuffer();
		this.container.addComponentListener( new RunMonsterListener() );
	}

	public void update()
	{
		time += 1;
		if( time % 10 == 0 )
		{
			map.updateAutoTileState();
		}
		
		if( k.w )
		{
			player.move( Face.NORTH );
		}
		if( k.a )
		{
			player.move( Face.WEST );
		}
		if( k.s )
		{
			player.move( Face.SOUTH );
		}
		if( k.d )
		{
			player.move( Face.EAST );
		}
		
		player.update( time );
		
		Graphics2D g2 = buffer.createGraphics();
		//BEGIN DRAW
		
		int camx = Math.min( Math.max( player.xScreen, buffer.getWidth() / 2 ), (map.width*map.tileSize)-buffer.getWidth()/2 );
		int camy = Math.min( Math.max( player.yScreen, buffer.getHeight() / 2 ), (map.height*map.tileSize)-buffer.getHeight()/2 );
		int tx = -camx + buffer.getWidth() / 2;
		int ty = -camy + buffer.getHeight() / 2;
		g2.translate( tx, ty );
		map.render( g2 );
		player.render( g2 );
		
		//END DRAW
		g2.dispose();
		g.scale( 2, 2 );
		drawImage( buffer, 0, 0 );
	}
	
	public void createBuffer()
	{
		buffer = DGraphics.createBufferedImage( getWidth()/2, getHeight()/2 );
	}
	
	public static void main( String[] args )
	{
		RunMonster rm = new RunMonster();
		rm.begin();
	}
	
	public class RunMonsterListener implements ComponentListener
	{

		public void componentHidden( ComponentEvent e )
		{
			
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentResized( ComponentEvent e ) 
		{
			createBuffer();
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
