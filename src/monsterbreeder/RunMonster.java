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
import com.phyloa.dlib.renderer.DScreenHandler;
import com.phyloa.dlib.renderer.Graphics2DRenderer;
import com.phyloa.dlib.util.DFile;
import com.phyloa.dlib.util.DGraphics;
import com.phyloa.dlib.util.DMath;

public class RunMonster extends Graphics2DRenderer
{
	HashMap<String, String> dataStore = new HashMap<String, String>();
	
	BufferedImage buffer;
	int bufferx = 200;
	int buffery = 150;
	
	DScreenHandler<RunMonster> dsh;
	
	Graphics2D g2;
	
	WorldScreen ws;
	BattleSystem bs;
	
	public void initialize()
	{
		size( 800, 600 );
		
		createBuffer();
		this.container.addComponentListener( new RunMonsterListener() );
		
		dsh = new DScreenHandler<RunMonster>();
		ws = new WorldScreen( this );
		bs = new BattleSystem( this );
		dsh.register( "world", ws );
		dsh.register( "battle", bs );
	}

	public void update()
	{
		g2 = buffer.createGraphics();
		//BEGIN DRAW
		
		int bw = buffer.getWidth();
		int bh = buffer.getHeight();
		
		dsh.update( this );
		
		g2.dispose();
		drawImage( buffer, 0, 0, getWidth(), getHeight(), 0, 0, bw, bh );
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
			//createBuffer();
		}

		public void componentShown(ComponentEvent arg0) {
			
		}
		
	}
}
