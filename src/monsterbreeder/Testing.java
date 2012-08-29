package monsterbreeder;

import java.util.LinkedList;

import javax.script.ScriptException;

import com.phyloa.dlib.lua.DLua;

public class Testing 
{
	public static void main( String[] args ) throws ScriptException
	{
		DLua.e.eval( "function onstep()\n" +
				"print( hello )\n" +
				"end\n" +
				"onstep()\n" +
				"onstep = nil\n" +
				"" );
		LinkedList<String> hello = new LinkedList<String>();
		hello.push( "1" );
		hello.push( "2" );
		hello.push( "3" );
	}
}
