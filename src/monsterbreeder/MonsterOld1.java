package monsterbreeder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import com.phyloa.dlib.util.DGraphics;
import com.phyloa.dlib.util.DMath;

public class MonsterOld1 
{
	int bodyHeight;
	int bodyLength;
	int bodyWidth;
	
	int headWidth;
	int headHeight;
	int headLength;
	
	float headPosAngle;
	
	int r,g,b;
	
	int legCount;
	
	float legRotA;
	int legLengthA;
	float legRotB;
	int legLengthB;
	
	int armCount;
	
	float armRotA;
	float armlengthA;
	
	boolean wings;
	
	String name;
	
	public MonsterOld1()
	{
		
	}
	
	public void renderFront( Graphics2D g )
	{
		Color main = new Color( DGraphics.rgb( this.r, this.g, this.b ) );
		g.setColor( main.darker() );
		g.fillOval( -((int)(bodyWidth/2) + 1), -((int)(bodyHeight/2) + 1), (int)bodyWidth + 2, (int)bodyHeight + 2 );
		g.setColor( main );
		g.fillOval( -((int)(bodyWidth/2)), -((int)(bodyHeight/2)), (int)bodyWidth, (int)bodyHeight );
		AffineTransform at = g.getTransform();
			g.translate( 0, -DMath.sinf( this.headPosAngle ) * (bodyHeight-3) );
			g.setColor( main.darker() );
			g.fillOval( -((int)(headWidth/2) + 1), -((int)(headHeight/2) + 1), (int)headWidth + 2, (int)headHeight + 2 );
			g.setColor( main );
			g.fillOval( -((int)(headWidth/2)), -((int)(headHeight/2)), (int)headWidth, (int)headHeight );
		g.setTransform( at );
		
		for( int i = 0; i < 2; i++ )
		{
			at = g.getTransform();
			g.scale( i == 0 ? -1 : 1, 1 );
			g.translate( (bodyWidth/2.f), 0 );
			g.rotate( this.legRotA );
			g.setColor( main.darker() );
			g.fillRect( -1, -2, legLengthA+2, 4 );
			g.setColor( main );
			g.fillRect( 0, -1, legLengthA, 2 );
			g.setTransform( at );
		}
	}
	
	public void renderSide( Graphics2D g )
	{
		Color main = new Color( DGraphics.rgb( this.r, this.g, this.b ) );
		g.setColor( main.darker() );
		g.fillOval( -((int)(bodyLength/2) + 1), -((int)(bodyHeight/2) + 1), (int)bodyLength + 2, (int)bodyHeight + 2 );
		g.setColor( main );
		g.fillOval( -((int)(bodyLength/2)), -((int)(bodyHeight/2)), (int)bodyLength, (int)bodyHeight );
		AffineTransform at = g.getTransform();
			g.translate( DMath.cosf( this.headPosAngle ) * (bodyLength-3), -DMath.sinf( this.headPosAngle ) * (bodyHeight-3) );
			g.setColor( main.darker() );
			g.fillOval( -((int)(headLength/2) + 1), -((int)(headHeight/2) + 1), (int)headLength + 2, (int)headHeight + 2 );
			g.setColor( main );
			g.fillOval( -((int)(headLength/2)), -((int)(headHeight/2)), (int)headLength, (int)headHeight );
		g.setTransform( at );
	}
	
	public static MonsterOld1 generate( long seed )
	{
		MonsterOld1 m = new MonsterOld1();
		Random r = new Random( seed );
		
		m.bodyHeight = (r.nextInt( 9 ) + 12);
		m.bodyWidth = (r.nextInt( 9 ) + 12);
		m.bodyLength = (r.nextInt( 18 ) + 6);
		
		m.r = r.nextInt( 255 );
		m.g = r.nextInt( 255 );
		m.b = r.nextInt( 255 );
		
		m.headHeight = m.bodyHeight / 2;
		m.headWidth = m.bodyWidth / 2;
		m.headLength = m.bodyLength / 2;
		
		m.headPosAngle = DMath.lerp( r.nextFloat(), 0, DMath.PIF/3.f );
		
		m.legCount = r.nextInt( 3 );
		m.legRotA = DMath.lerp( r.nextFloat(), -DMath.PIF/4.f, DMath.PIF/4.f );
		m.legLengthA = (int)DMath.lerp( r.nextFloat(), 5, 10 );
		m.legRotB = (DMath.PIF/2.f) + m.legRotA;
		m.legLengthB = (int)DMath.lerp( r.nextFloat(), 2, 4 ) + m.legLengthA;
		
		return m;
	}
}
