package monsterbreeder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.script.ScriptException;

import monsterbreeder.GUI.SelectBox;
import monsterbreeder.GUI.Textbox;
import monsterbreeder.monster.MonsterBuilder;

import org.dom4j.DocumentException;

import com.danwink.java.rpg.Map;
import com.danwink.java.rpg.Map.TileEvent;
import com.danwink.java.rpg.MapObject.Face;
import com.phyloa.dlib.lua.DLua;

public class LuaInterface 
{
	static WorldScreen ws;
	private static HashMap<String, TileEvent> te = new HashMap<String, TileEvent>();
	
	public static void setup( WorldScreen worldScreen )
	{
		LuaInterface.ws = worldScreen;
		worldScreen.lr.add( "setup", "li = luajava.bindClass( \"monsterbreeder.LuaInterface\" ) \n" +
				"function teleport( mapname, x, y, dir ) \n" +
				"li:teleport( mapname, x, y, dir ) \n" +
				"end \n" +
				"function showtext( text ) \n" +
				"li:showText( text ) \n" +
				"end \n" +
				"function showchoice( text, ... ) \n" +
				"local num = select( '#', ... )\n" +
				"if (num == 2) then return li:showChoice( text, select( 1, ... ), select( 2, ... ) )\n" +
				"elseif (num == 3) then return li:showChoice( text, select( 1, ... ), select( 2, ... ), select( 3, ... ) )\n" +
				"elseif (num == 4) then return li:showChoice( text, select( 1, ... ), select( 2, ... ), select( 3, ... ), select( 4, ... ) ) end\n" +
				"end\n" +
				"function fadeto( r, g, b, dest, speed )\n" +
				"li:fadeto( r, g, b, dest, speed )\n" +
				"end\n" +
				"function setsprite( name ) \n" +
				"li:setSprite( name, en )\n" +
				"end\n" +
				"function setpassable( passable )\n" +
				"li:setPassable( passable, en )\n" +
				"end\n" +
				"function faceplayer()\n" +
				"li:facePlayer( en )\n" +
				"end\n" +
				"function up() li:up( en ) end\n" +
				"function down() li:down( en ) end\n" +
				"function left() li:left( en ) end\n" +
				"function right() li:right( en ) end\n" 
				);
		worldScreen.lr.run( "setup" );
		worldScreen.lr.add( "clear", 
				"onstep = nil\n" +
				"onload = nil\n" +
				"onenter = nil\n" +
				"ontick = nil\n" +
				"onmove = nil\n" );
		
		worldScreen.lr.add( "onload", "if (onload) then \n onload() \n end" );
		worldScreen.lr.add( "onstep", "if (onstep) then \n onstep() \n end" );
		worldScreen.lr.add( "onenter", "if (onenter) then \n onenter() \n end" );
		worldScreen.lr.add( "ontick", "if (ontick) then \n ontick() \n end" );
		worldScreen.lr.add( "onmove", "if (onmove) then \n onmove() \n end" );
		
	}
	
	public static void showText( String text )
	{
		if( ws.debug )
		{
			System.out.println( "from lua: showText( \"" + text.replaceAll( "\n", "(newline)" ) + "\" ) " );
		}
		Textbox tb = ws.gui.new Textbox( text );
		ws.gui.pushElement( tb );
		while( tb.alive )
		{
			try {
				Thread.sleep( 15 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static int showChoice( String text, String choice1, String choice2 )
	{
		return showChoiceA( text, choice1, choice2 );
	}
	
	public static int showChoice( String text, String choice1, String choice2, String choice3 )
	{
		return showChoiceA( text, choice1, choice2, choice3 );
	}
	
	public static int showChoice( String text, String choice1, String choice2, String choice3, String choice4 )
	{
		return showChoiceA( text, choice1, choice2, choice3, choice4 );
	}
	
	public static int showChoiceA( String text, String... choices )
	{
		if( ws.debug )
		{
			System.out.println( "from lua: showChoice( \"" + text.replaceAll( "\n", "(newline)" ) + "\" ) " );
			for( int i = 0; i < choices.length; i++ )
			{
				System.out.println( choices[i] );
			}
		}
		SelectBox tb = ws.gui.new SelectBox( text, choices );
		ws.gui.pushElement( tb );
		while( tb.alive )
		{
			try {
				Thread.sleep( 15 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tb.choice;
	}
	
	public static void setSprite( String name, String event )
	{
		te.get( event ).setImage( TextureHandler.get( "Characters/" + name ) );
	}
	
	public static void setPassable( boolean passable, String event )
	{
		te.get( event ).passable = passable;
	}
	
	public static void facePlayer( String event )
	{
		te.get( event ).face( ws.player );
	}
	
	public static float random()
	{
		return (float)Math.random();
	}
	
	public static void up( String event )
	{
		te.get( event ).move( Face.NORTH );
	}
	
	public static void down( String event )
	{
		te.get( event ).move( Face.SOUTH );
	}
	
	public static void left( String event )
	{
		te.get( event ).move( Face.WEST );
	}
	
	public static void right( String event )
	{
		te.get( event ).move( Face.EAST );
	}
	
	public static void teleport( String name, int x, int y, String face )
	{
		synchronized( ws )
		{
			if( ws.debug )
			{
				System.out.println( "from lua: teleport( \"" + name + "\", " + x + ", " + y + ", \"" + face + "\" ) " );
			}
			ws.lr.run( "clear" );
			try {
				ws.loadMap( name, x, y, face );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void fadeto( int r, int g, int b, float dest, float speed )
	{
		if( ws.debug )
		{
			System.out.println( "from lua: fadeto( " + r + ", " + g + ", " + b + ", " + dest + ", " + speed + " ) " );
		}
		ws.fadeTo( r, g, b, dest, speed );
		while( ws.opacity != ws.destOpac )
		{
			try {
				Thread.sleep( 15 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void setInt( String key, int value )
	{
		ws.rm.dataStore.put( key, Integer.toString( value ) );
	}
	
	public static int getInt( String key )
	{
		return Integer.parseInt( ws.rm.dataStore.get( key ) );
	}
	
	public static void setFloat( String key, float value )
	{
		ws.rm.dataStore.put( key, Float.toString( value ) );
	}
	
	public static float getFloat( String key )
	{
		return Float.parseFloat( ws.rm.dataStore.get( key ) );
	}
	
	public static void setString( String key, String value )
	{
		ws.rm.dataStore.put( key, value );
	}
	
	public static String getString( String key )
	{
		return ws.rm.dataStore.get( key );
	}
	
	public static void setBoolean( String key, boolean value )
	{
		if( ws.debug )
		{
			System.out.println( "from lua: setBoolean( " + key + ", " + value + " ) " );
		}
		ws.rm.dataStore.put( key, Boolean.toString( value ) );
	}
	
	public static boolean getBoolean( String key )
	{
		String v = ws.rm.dataStore.get( key );
		if( ws.debug )
		{
			System.out.println( "from lua: getBoolean( " + key + " ) = " + v );
		}
		return Boolean.parseBoolean( v );
	}
	
	public static int getPlayerX()
	{
		return ws.player.xTile;
	}
	
	public static int getPlayerY()
	{
		return ws.player.yTile;
	}
	
	public static void startBattle( String key )
	{
		if( key.equals( "random" ) )
		{
			ws.rm.bs.start( MonsterBuilder.generateMonster( new Random().nextInt() ), MonsterBuilder.generateMonster( new Random().nextInt() ) );
			ws.rm.dsh.activate( "battle" );
		}
	}
	
	public static void sleep( int time )
	{
		try
		{
			Thread.sleep( time );
		} catch( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void prepare( TileEvent te )
	{	
		ws.lr.run( "clear" ); 
		ws.lr.add( te.toString()+"setup", "en = '" + te.toString() + "'\n" );
		ws.lr.run( te.toString()+"setup" );
		LuaInterface.te.put( te.toString(), te );
	}
	
	
}
