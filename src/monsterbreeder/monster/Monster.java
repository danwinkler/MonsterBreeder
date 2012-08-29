package monsterbreeder.monster;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point3i;

import monsterbreeder.ConfigLoader;

import org.dom4j.DocumentException;
import org.dom4j.Node;

import com.phyloa.dlib.util.DMath;

public class Monster 
{
	Part body;
	Type type;
	ArrayList<Move> possibleMoves = new ArrayList<Move>();
	Move[] moves = new Move[4];
	
	public class Part
	{
		Point3i base;
		ArrayList<Attach> attaches = new ArrayList<Attach>();
		public BufferedImage front;
		public BufferedImage side;
		public BufferedImage rear;
	}
	
	public class Attach
	{
		Point3i point;
		Part part;
	}
}
