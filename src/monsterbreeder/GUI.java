package monsterbreeder;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import com.phyloa.dlib.util.KeyHandler;

public class GUI 
{
	static Font font;
	static Font font8;
	
	static {
		try {
			font = Font.createFont( Font.TRUETYPE_FONT, new File( "04B_03__.TTF" ) );
			font8 = font.deriveFont( 8.f );
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	boolean active = false;
	Stack<GUIElement> elestack = new Stack<GUIElement>();
	
	public void render( Graphics2D g, KeyHandler k, int width, int height )
	{
		for( int i = 0; i < elestack.size(); i++ )
		{
			GUIElement ge = elestack.get( i );
			ge.render( g, width, height );
		}
		if( elestack.size() > 0 && !elestack.peek().alive )
		{
			elestack.pop();
		}
		active = elestack.size() > 0;
		if( active )
		{
			elestack.peek().update( k );
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
		
		public void update( KeyHandler k )
		{
			if( k.space )
			{
				this.alive = false;
				k.space = false;
			}
		}
		
		public void render( Graphics2D g,  int width, int height )
		{
			g.setFont( font8 );
			
			g.setColor( Color.white );
			g.fillRoundRect( 10, height-35, width-20, 25, 3, 3 );
			g.setColor( Color.black );
			g.drawRoundRect( 10, height-35, width-20, 25, 3, 3 );
			
			String[] lines = text.split( "\n" );
			for( int i = 0; i < lines.length; i++ )
			{
				g.drawString( lines[i], 20, height - 25 + i * 8 );
			}
		}
	}
	
	class SelectBox extends GUIElement
	{
		public String text;
		public String[] choices;
		public int choice;
		
		public SelectBox( String text, String[] choices )
		{
			this( choices );
			this.text = text;
		}
		
		public SelectBox( String[] choices )
		{
			this.choices = choices;
		}
		
		public void update( KeyHandler k )
		{
			if( k.space )
			{
				this.alive = false;
				k.space = false;
			}
			
			int lastChoice = choice;
			if( k.up )
			{
				if( choice > 1 )
				{
					choice -= 2;
				}
				k.up = false;
			}
			if( k.down )
			{
				if( choice < 2 )
				{
					choice += 2;
				}
				k.down = false;
			}
			if( k.left )
			{
				if( choice % 2 == 1 )
				{
					choice -= 1;
				}
				k.left = false;
			}
			if( k.right )
			{
				if( choice % 2 == 0 )
				{
					choice += 1;
				}
				k.right = false;
			}
			if( choice >= choices.length || choices[choice] == null )
			{
				choice = lastChoice;
			}
		}
		
		public void render( Graphics2D g, int width, int height )
		{
			g.setFont( font8 );
			
			g.setColor( Color.white );
			g.fillRoundRect( 10, height-35, width-20, 25, 3, 3 );
			g.setColor( Color.black );
			g.drawRoundRect( 10, height-35, width-20, 25, 3, 3 );
			
			for( int i = 0; i < choices.length; i++ )
			{
				if( choices[i] == null )
				{
					continue;
				}
				int xx = 0, yy = 0;
				switch( i )
				{
				case 0:
					xx = 20;
					yy = height - 25;
					break;
				case 1:
					xx = 100;
					yy = height - 25;
					break;
				case 2:
					xx = 20;
					yy = height - 17;
					break;
				case 3:
					xx = 100;
					yy = height - 17;
					break;
				}
				g.drawString( choices[i], xx, yy );
				if( i == choice )
				{
					g.drawLine( xx, yy+1, xx + 20, yy+1 );
				}
			}
			
		}		
	}
	
	abstract class GUIElement
	{
		boolean alive = true;
		public abstract void render( Graphics2D g, int width, int height );
		public abstract void update( KeyHandler k );
	}
}
