package monsterbreeder;

import java.io.IOException;

import javax.script.ScriptException;

import monsterbreeder.GUI.Textbox;

import org.dom4j.DocumentException;

import com.phyloa.dlib.lua.DLua;

public class LuaInterface 
{
	static RunMonster rm;
	
	public static void setup( RunMonster rm )
	{
		LuaInterface.rm = rm;
		rm.lr.add( "setup", "li = luajava.bindClass( \"monsterbreeder.LuaInterface\" ) \n" +
				"function teleport( mapname, x, y, dir ) \n" +
				"li:teleport( mapname, x, y, dir ) \n" +
				"end \n" +
				"function showtext( text ) \n" +
				"li:showText( text ) \n" +
				"end \n" );
		rm.lr.run( "setup" );
		
		rm.lr.add( "onload", "if (onload) then \n onload() \n end" );
		rm.lr.add( "onstep", "if (onstep) then \n onstep() \n end" );
	}
	
	public static void showText( String text )
	{
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
