package monsterbreeder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import monsterbreeder.monster.MonsterBuilder.AttachSpec;
import monsterbreeder.monster.MonsterBuilder.PartSpec;
import monsterbreeder.monster.PartType;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.danwink.java.rpg.Map;
import com.danwink.java.rpg.Map.TileEvent;
import com.danwink.java.rpg.MapFileHelper;

public class ConfigLoader 
{
	public static void loadConfig( RunMonster rm, String file ) throws DocumentException, IOException
	{
		SAXReader reader = new SAXReader();
		Document doc = reader.read( file );
		Node game = doc.selectSingleNode( "//game" );
		Node maps = game.selectSingleNode( "maps" );
		List<? extends Node> mapList = maps.selectNodes( "map" );
		
		for( Node n : mapList )
		{
			Map m = MapFileHelper.loadMap( new File( "maps/" + n.getText() + ".xml" ) );
			rm.maps.put( m.name, m );
		}
		
		Node firstmap = game.selectSingleNode( "firstmap" );
		rm.loadMap( firstmap.getText(), Integer.parseInt( firstmap.valueOf( "@x" ) ), Integer.parseInt( firstmap.valueOf( "@y" ) ), "South" );
	}

	public static void loadParts( String file, ArrayList<PartSpec> parts ) throws DocumentException
	{
		SAXReader reader = new SAXReader();
		Document doc = reader.read( new File( file ) );
		Node partsNode = doc.selectSingleNode( "//parts" );
		List<? extends Node> partsList = partsNode.selectNodes( "part" );
		
		for( Node n : partsList )
		{
			PartSpec p = new PartSpec();
			p.name = n.selectSingleNode( "name" ).getName();
			p.type = PartType.get( n.selectSingleNode( "type" ).getName() );
			
			Node front = n.selectSingleNode( "front" );
			if( front != null )
			{
				p.front = TextureHandler.get( front.getText() );
			}
			
			Node side = n.selectSingleNode( "side" );
			if( side != null )
			{
				p.side = TextureHandler.get( side.getText() );
			}
			
			Node rear = n.selectSingleNode( "rear" );
			if( rear != null )
			{
				p.rear = TextureHandler.get( rear.getText() );
			}
			
			List<? extends Node> attachList = n.selectSingleNode( "attaches" ).selectNodes( "attach" );
			if( attachList != null )
			{
				for( int i = 0; i < attachList.size(); i++ )
				{
					Node m = attachList.get( i );
					AttachSpec a = new AttachSpec();
					a.p = new Point3i();
					a.p.x = Integer.parseInt( m.selectSingleNode( "x" ).getText() );
					a.p.y = Integer.parseInt( m.selectSingleNode( "y" ).getText() );
					a.p.z = Integer.parseInt( m.selectSingleNode( "z" ).getText() );
					Node allowNode = m.selectSingleNode( "allow" );
					if( allowNode != null )
					{
						String[] allowed = allowNode.getText().split( "," );
						a.allowPartType = new PartType[allowed.length];
						for( int j = 0; j < allowed.length; j++ )
						{
							a.allowPartType[j] = PartType.get( allowed[j] );
						}
					}
					p.attaches.add( a );
				}
			}
		}
	}
}
