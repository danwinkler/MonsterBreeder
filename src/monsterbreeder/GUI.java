package monsterbreeder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Stack;

import com.phyloa.dlib.util.KeyHandler;

public class GUI 
{
	boolean active = false;
	Stack<GUIElement> elestack = new Stack<GUIElement>();
	
	public void render( Graphics2D g, KeyHandler k, int width, int height )
	{
		for( int i = 0; i < elestack.size(); i++ )
		{
			GUIElement ge = elestack.get( i );
			ge.render( g, k, width, height );
		}
		if( elestack.size() > 0 && !elestack.peek().alive )
		{
			elestack.pop();
		}
	}
	
	public void pushElement( GUIElement e )
	{
		elestack.push( e );
	}
	
	class Textbox extends GUIElement
	{
		String text;
		
		public Textbox( String text )
		{
			this.text = text;
		}
		
		public void render( Graphics2D g, KeyHandler k, int width, int height )
		{
			if( k.space && elestack.peek() == this )
			{
				this.alive = false;
				k.space = false;
			}
			
			g.setColor( Color.white );
			g.fillRect( 10, height-100, width-30, 70 );
			g.setColor( Color.black );
			String[] lines = text.split( "\n" );
			for( int i = 0; i < lines.length; i++ )
			{
				g.drawString( lines[i], 20, height - 80 + i * 12 );
			}
		}
	}
	
	abstract class GUIElement
	{
		boolean alive = true;
		public abstract void render( Graphics2D g, KeyHandler k, int width, int height );
	}
}
