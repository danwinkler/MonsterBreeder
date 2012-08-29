package monsterbreeder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.phyloa.dlib.util.DMath;

public class RunMonster extends Graphics2DRenderer implements TileArriveListener
{
	HashMap<String, String> dataStore = new HashMap<String, String>();
	HashMap<String, Map> maps = new HashMap<String, Map>();
	HashMap<String, Tileset> tilesets = new HashMap<String, Tileset>();
	
	Map map;
	
	int time = 0;
	
	BufferedImage buffer;
	int bufferx = 400;
	int buffery = 300;
	
	MapObject player;
	
	ArrayList<MapObject> mos = new ArrayList<MapObject>();
	
	DLuaRunner lr;
	
	GUI gui;
	
	int screenr;
	int screeng;
	int screenb;
	float opacity = 0;
	float deltaOpac = 0;
	float destOpac = 0;
	
	boolean debug = true;
	
	boolean night = false;
	
	public void initialize()
	{
		size( 800, 600 );
		
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
		setNight();
	}

	public void update()
	{
		if( lr.debug != this.debug )
		{
			lr.debug = this.debug;
		}
		synchronized( this )
		{
			time += 1;
			if( time % 10 == 0 )
			{
				map.updateAutoTileState();
				if( lr.queue.size() == 0 )
				{
					for( TileEvent e : map.events )
					{
						String name = map.name + e.xTile + "," + e.yTile;
						lr.add( name, e.code );
						lr.run( name );
						LuaInterface.prepare( e );
						lr.run( "ontick" );
					}
				}
			}
			
			if( gui.elestack.size() == 0 )
			{
				if( k.up || k.w )
				{
					player.move( Face.NORTH );
				}
				if( k.left || k.a )
				{
					player.move( Face.WEST );
				}
				if( k.down || k.s )
				{
					player.move( Face.SOUTH );
				}
				if( k.right || k.d )
				{
					player.move( Face.EAST );
				}
				if( k.n )
				{
					night = !night;
					k.n = false;
				}
			}
			
			player.update( time );
			
			if( opacity != destOpac )
			{
				if( Math.abs( opacity - destOpac ) < Math.abs( deltaOpac ) )
				{
					opacity = destOpac;
					deltaOpac = 0;
				}
				else
				{
					opacity += deltaOpac;
				}
			}
			
			Graphics2D g2 = buffer.createGraphics();
			//BEGIN DRAW
			
			int bw = buffer.getWidth();
			int bh = buffer.getHeight();
			
			g2.setColor( Color.WHITE );
			g2.fillRect( 0, 0, bw, bh );
			
			int camx = Math.min( Math.max( player.xScreen + map.tileSize/2, bw / 2 ), (map.width*map.tileSize)-bw/2 );
			int camy = Math.min( Math.max( player.yScreen + map.tileSize/2, bh / 2 ), (map.height*map.tileSize)-bh/2 );
			int tx = -camx + bw / 2;
			int ty = -camy + bh / 2;
			g2.translate( tx, ty );
			map.render( g2, mos, night );
			g2.translate( -tx, -ty );
			
			g2.setColor( new Color( screenr/255f, screeng/255f, screenb/255f, DMath.bound( opacity, 0, 1 ) ) );
			g2.fillRect( 0, 0, bw, bh );
			
			gui.render( g2, k, bw, bh );
			
			//END DRAW
			g2.dispose();
			drawImage( buffer, 0, 0, getWidth(), getHeight(), 0, 0, bw, bh );
			g.drawString( "X: " + player.xTile + ", Y: " + player.yTile, 20, 20 );
		}
	}
	
	public void fadeTo( int r, int g, int b, float opac, float speed )
	{
		this.screenr = r;
		this.screeng = g;
		this.screenb = b;
		this.deltaOpac = speed * (opac > this.opacity ? 1 : -1);
		this.destOpac = opac;
	}
	
	public void onMapLoad( Map m )
	{
		for( TileEvent e : m.events )
		{
			String name = m.name + e.xTile + "," + e.yTile;
			lr.add( name, e.code );
			lr.run( name );
			LuaInterface.prepare( e );
			lr.run( "onload" );
		}
		setNight();
	}
	
	public void createBuffer()
	{
		buffer = DGraphics.createBufferedImage( bufferx, buffery );
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
	
	public void setNight()
	{
		@SuppressWarnings("deprecation")
		int hour = new Date( System.currentTimeMillis() ).getHours();
		night = hour <= 6 || hour >= 20;
	}

	public void onMapObjectArrive( MapObject mo ) 
	{
		if( debug )
		{
			System.out.println( "onMapObject arrive: X:" + mo.xTile + ", Y: " + mo.yTile );
		}
		TileEvent te = map.getEvent( mo.xTile, mo.yTile );
		if( te != null && mo == player )
		{
			lr.run( map.name + te.xTile + "," + te.yTile );
			LuaInterface.prepare( te );
			lr.run( "onstep" );		
		}
		if( mo == player )
		{
			for( TileEvent e : map.events )
			{
				String name = map.name + e.xTile + "," + e.yTile;
				lr.add( name, e.code );
				lr.run( name );
				LuaInterface.prepare( te );
				lr.run( "onmove" );
			} 
		}
	}
}
