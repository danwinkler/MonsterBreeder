package monsterbreeder;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import monsterbreeder.monster.Monster;

public class BattleSystem 
{	
	static Font font;
	Monster m1;
	Monster m2;
	
	static {
		try {
			font = Font.createFont( Font.TRUETYPE_FONT, new File( "04B_03__.TTF" ) );
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void start( Monster m1, Monster m2 )
	{
		this.m1 = m1;
		this.m2 = m2;
	}
	
	public void update()
	{
		
	}
	
	public void render( Graphics2D g )
	{
		g.setColor( Color.WHITE );
		g.fillRect( 0, 0, 200, 150 );
		
		g.setFont( font.deriveFont( 8.f ) );
		g.drawImage( m2.getFront(), 136, 0, null );
		g.setColor( Color.BLACK );
		g.drawString( m2.name, 145, 57 );
		g.drawLine( 140, 60, 190, 60 );
		
	}
}
