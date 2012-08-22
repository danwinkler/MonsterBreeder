package monsterbreeder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptException;

import org.dom4j.DocumentException;

import com.danwink.java.rpg.Map;
import com.danwink.java.rpg.Map.TileEvent;
import com.danwink.java.rpg.MapFileHelper;
import com.danwink.java.rpg.MapObject;
import com.danwink.java.rpg.MapObject.Face;
import com.danwink.java.rpg.MapObject.TileArriveListener;
import com.danwink.java.rpg.Tileset;
import com.phyloa.dlib.lua.DLua;
import com.phyloa.dlib.lua.DLuaRunner;
import com.phyloa.dlib.renderer.Graphics2DRenderer;
import com.phyloa.dlib.util.DFile;
import com.phyloa.dlib.util.DGraphics;

public class RunMonster extends Graphics2DRenderer implements TileArriveListener
{
	HashMap<String, Map> maps = new HashMap<String, Map>();
	HashMap<String, Tileset> tilesets = new HashMap<String, Tileset>();
	
	Map map;
	
	int time = 0;
	
	BufferedImage buffer;
	
	MapObject player;
	
	ArrayList<MapObject> mos = new ArrayList<MapObject>();
	
	DLuaRunner lr;
	
	GUI gui;
	
	public void initialize()
	{
		size( 1024, 768 );
		
		createBuffer();
		this.container.addComponentListener( new RunMonsterListener() );
		
		gui = new GUI();
		
		lr = new DLuaRunner();
		
		LuaInterface.setup( this );
		
		try {
			player = new MapObject( map, DFile.loadImage( "Characters/001-Fighter01.png" ), 0, 0 );
			player.addTileArriveListener( this );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ConfigLoader.loadConfig( this, "game.xml" );
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		mos.add( player );
	}

	public void update()
	{
		time += 1;
		if( time % 10 == 0 )
		{
			map.updateAutoTileState();
		}
		
		if( gui.elestack.size() == 0 )
		{
			if( k.up )
			{
				player.move( Face.NORTH );
			}
			if( k.left )
			{
				player.move( Face.WEST );
			}
			if( k.down )
			{
				player.move( Face.SOUTH );
			}
			if( k.right )
			{
				player.move( Face.EAST );
			}
		}
		
		player.update( time );
		
		Graphics2D g2 = buffer.createGraphics();
		//BEGIN DRAW
		
		int bw = buffer.getWidth();
		int bh = buffer.getHeight();
		
		g2.setColor( Color.WHITE );
		g2.fillRect( 0, 0, bw, bh );
		
		int camx = Math.min( Math.max( player.xScreen, bw / 2 ), (map.width*map.tileSize)-bw/2 );
		int camy = Math.min( Math.max( player.yScreen, bh / 2 ), (map.height*map.tileSize)-bh/2 );
		int tx = -camx + bw / 2;
		int ty = -camy + bh / 2;
		g2.translate( tx, ty );
		map.render( g2, mos );
		g2.translate( -tx, -ty );
		
		gui.render( g2, k, buffer.getWidth(), buffer.getHeight() );
		
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
			lr.run( name );
			lr.run( "onload" );
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

		public void componentMoved(ComponentEvent arg0) {
			
		}

		public void componentResized( ComponentEvent e ) 
		{
			createBuffer();
		}

		public void componentShown(ComponentEvent arg0) {
			
		}
		
	}

	public void loadMap( String mapname, int x, int y, String face ) throws IOException, DocumentException
	{
		map = maps.get( mapname );
		
		Tileset tc = tilesets.get( map.configFile );
		if( tc == null )
		{
			tc = MapFileHelper.loadTileConfig( new File( "tileconfigs/" + map.configFile ) );
			tilesets.put( map.configFile, tc );
		}
		map.setTileset( tc );
		
		map.prepareAutoTilesState();
		
		player.teleport( map, x, y, Face.getByName( face ) );
		
		onMapLoad( map );
	}

	public void onMapObjectArrive( MapObject mo ) 
	{
		TileEvent te = map.getEvent( mo.xTile, mo.yTile );
		if( te != null && mo == player )
		{
			lr.run( map.name + te.x + "," + te.y );
			lr.run( "onstep" );		
		}
	}
}
