package monsterbreeder;

import javax.script.ScriptException;

import com.phyloa.dlib.lua.DLua;

public class Testing 
{
	public static void main( String[] args )
	{
		for( int i = 0; i < 10; i++ )
		{
			new Thread( new Runnable() {
				public void run() {
					try {
						DLua.e.eval( "print( \"hello\" ) \n" +
								"print( \"hello\" ) \n" +
								"print( \"hello\" ) \n" +
								"print( \"hello\" ) \n" +
								"print( \"hello\" ) \n" +
								"print( \"hello\" ) \n" );
					} catch (ScriptException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			} ).start();
		}
	}
}
