package monsterbreeder;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
}
