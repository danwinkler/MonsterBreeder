package monsterbreeder;

import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.script.ScriptException;

import org.dom4j.DocumentException;

import com.danwink.java.rpg.Map;
import com.danwink.java.rpg.Map.TileEvent;
import com.danwink.java.rpg.MapFileHelper;
import com.danwink.java.rpg.MapObject;
import com.danwink.java.rpg.MapObject.Face;
import com.danwink.java.rpg.Tileset;
import com.phyloa.dlib.lua.DLua;
import com.phyloa.dlib.lua.DLuaRunner;
import com.phyloa.dlib.renderer.Graphics2DRenderer;
import com.phyloa.dlib.util.DFile;
import com.phyloa.dlib.util.DGraphics;

public class RunMonster extends Graphics2DRenderer
{
	Map map;
	
	int time = 0;
	
	BufferedImage buffer;
	
	MapObject player;
	
	ArrayList<MapObject> mos = new ArrayList<MapObject>();
	
	DLuaRunner lr;
	
	public void initialize()
	{
		size( 800, 600 );
		
		try {
			map = MapFileHelper.loadMap( new File( "maps/test.xml" ) );
			Tileset tc = MapFileHelper.loadTileConfig( new File( "tileconfigs/" + map.configFile ) );
			map.setTileset( tc );
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		map.prepareAutoTilesState();
		
		try {
			player = new MapObject( map, DFile.loadImage( "Characters/001-Fighter01.png" ), 0, 0 );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mos.add( player );
		
		createBuffer();
		this.container.addComponentListener( new RunMonsterListener() );
		lr = new DLuaRunner();
		
		onMapLoad( map );
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
		
		int bw = buffer.getWidth();
		int bh = buffer.getHeight();
		
		int camx = Math.min( Math.max( player.xScreen, bw / 2 ), (map.width*map.tileSize)-bw/2 );
		int camy = Math.min( Math.max( player.yScreen, bh / 2 ), (map.height*map.tileSize)-bh/2 );
		int tx = -camx + bw / 2;
		int ty = -camy + bh / 2;
		g2.translate( tx, ty );
		map.render( g2, mos );
		
		//END DRAW
		g2.dispose();
		g.scale( 2, 2 );
		drawImage( buffer, 0, 0 );
	}
	
	public void onMapLoad( Map m )
	{
		for( TileEvent e : m.events )
		{
			String name = m.name + e.x + "," + e.y;
			lr.add( name, e.code );
			try {
				lr.run( name );
				DLua.e.eval( "onload()" );
			} catch (ScriptException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
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
