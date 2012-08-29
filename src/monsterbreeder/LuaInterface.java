package monsterbreeder;

import java.io.IOException;

import javax.script.ScriptException;

import monsterbreeder.GUI.Textbox;

import org.dom4j.DocumentException;

import com.danwink.java.rpg.Map;
import com.danwink.java.rpg.Map.TileEvent;
import com.phyloa.dlib.lua.DLua;

public class LuaInterface 
{
	static RunMonster rm;
	private static TileEvent te;
	
	public static void setup( RunMonster rm )
	{
		LuaInterface.rm = rm;
		rm.lr.add( "setup", "li = luajava.bindClass( \"monsterbreeder.LuaInterface\" ) \n" +
				"function teleport( mapname, x, y, dir ) \n" +
				"li:teleport( mapname, x, y, dir ) \n" +
				"end \n" +
				"function showtext( text ) \n" +
				"li:showText( text ) \n" +
				"end \n" +
				"function fadeto( r, g, b, dest, speed )\n" +
				"li:fadeto( r, g, b, dest, speed )\n" +
				"end" );
		rm.lr.run( "setup" );
		rm.lr.add( "clear", 
				"onstep = nil\n" +
				"onload = nil\n" +
				"onenter = nil\n" +
				"ontick = nil\n" +
				"onmove = nil\n" );
		
		rm.lr.add( "onload", "if (onload) then \n onload() \n end" );
		rm.lr.add( "onstep", "if (onstep) then \n onstep() \n end" );
		rm.lr.add( "onenter", "if (onenter) then \n onenter() \n end" );
		rm.lr.add( "ontick", "if (ontick) then \n ontick() \n end" );
		rm.lr.add( "onmove", "if (onmove) then \n onmove() \n end" );
		
	}
	
	public static void showText( String text )
	{
		if( rm.debug )
		{
			System.out.println( "from lua: showText( \"" + text.replaceAll( "\n", "(newline)" ) + "\" ) " );
		}
		Textbox tb = rm.gui.new Textbox( text );
		rm.gui.pushElement( tb );
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
	
	public static void teleport( String name, int x, int y, String face )
	{
		synchronized( rm )
		{
			if( rm.debug )
			{
				System.out.println( "from lua: teleport( \"" + name + "\", " + x + ", " + y + ", \"" + face + "\" ) " );
			}
			rm.lr.run( "clear" );
			try {
				rm.loadMap( name, x, y, face );
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
		if( rm.debug )
		{
			System.out.println( "from lua: fadeto( " + r + ", " + g + ", " + b + ", " + dest + ", " + speed + " ) " );
		}
		rm.fadeTo( r, g, b, dest, speed );
		while( rm.opacity != rm.destOpac )
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
		rm.dataStore.put( key, Integer.toString( value ) );
	}
	
	public static int getInt( String key )
	{
		return Integer.parseInt( rm.dataStore.get( key ) );
	}
	
	public static void setFloat( String key, float value )
	{
		rm.dataStore.put( key, Float.toString( value ) );
	}
	
	public static float getFloat( String key )
	{
		return Float.parseFloat( rm.dataStore.get( key ) );
	}
	
	public static void setString( String key, String value )
	{
		rm.dataStore.put( key, value );
	}
	
	public static String getString( String key )
	{
		return rm.dataStore.get( key );
	}
	
	public static void setBoolean( String key, boolean value )
	{
		if( rm.debug )
		{
			System.out.println( "from lua: setBoolean( " + key + ", " + value + " ) " );
		}
		rm.dataStore.put( key, Boolean.toString( value ) );
	}
	
	public static boolean getBoolean( String key )
	{
		String v = rm.dataStore.get( key );
		if( rm.debug )
		{
			System.out.println( "from lua: getBoolean( " + key + " ) = " + v );
		}
		return Boolean.parseBoolean( v );
	}
	
	public static int getPlayerX()
	{
		return rm.player.xTile;
	}
	
	public static int getPlayerY()
	{
		return rm.player.yTile;
	}

	public static void prepare( TileEvent te )
	{
		LuaInterface.te = te;
	}
	
	
}
